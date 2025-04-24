package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

@SuppressWarnings("unused")
public class Board implements BoardManager {
    private final ArrayList<Cell> track;
    private final ArrayList<SafeZone> safeZones;
	private final GameManager gameManager;
    private int splitDistance;

    public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
        this.track = new ArrayList<>();
        this.safeZones = new ArrayList<>();
        this.gameManager = gameManager;
        
        for (int i = 0; i < 100; i++) {
            this.track.add(new Cell(CellType.NORMAL));
            
            if (i % 25 == 0) 
                this.track.get(i).setCellType(CellType.BASE);
            
            else if ((i+2) % 25 == 0) 
                this.track.get(i).setCellType(CellType.ENTRY);
        }

        for(int i = 0; i < 8; i++)
            this.assignTrapCell();

        for (int i = 0; i < 4; i++)
            this.safeZones.add(new SafeZone(colourOrder.get(i)));

        splitDistance = 3;
    }

    public ArrayList<Cell> getTrack() {
        return this.track;
    }

    public ArrayList<SafeZone> getSafeZones() {
        return this.safeZones;
    }
    
    @Override
    public int getSplitDistance() {
        return this.splitDistance;
    }

    public void setSplitDistance(int splitDistance) {
        this.splitDistance = splitDistance;
    }
   
    private void assignTrapCell() {
        int randIndex = -1;
        
        do
            randIndex = (int)(Math.random() * 100); 
        while(this.track.get(randIndex).getCellType() != CellType.NORMAL || this.track.get(randIndex).isTrap());
        
        this.track.get(randIndex).setTrap(true);
    }
    private ArrayList<Cell> getSafeZone(Colour colour){
    	for (SafeZone safezone : safeZones) {
            if (safezone.getColour().equals(colour)) {
                return safezone.getCells(); 
            }
        }
        return null;
    }
    private int getPositionInPath(ArrayList<Cell> path, Marble marble) {
        if (path == null || marble == null) {
            return -1;
        }
        for (int i = 0; i < path.size(); i++) {
            Cell currentCell = path.get(i);
            if (currentCell.getMarble() == marble) {
                return i;
            }
        }
        return -1;
    }
    private int getBasePosition(Colour colour) {
        if (colour == null) return -1;
        
        switch (colour) {
            case RED:
                return 0;
            case BLUE:
                return 25;
            case YELLOW:
                return 50;
            case GREEN:
                return 75;
            default:
                return -1;
        }
    }

    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
        Cell start = fullPath.get(0);
        Cell target = fullPath.get(fullPath.size() - 1);

        if (destroy) {
            for (int i = 1; i < fullPath.size(); i++) {
                Cell cell = fullPath.get(i);
                Marble occupant = cell.getMarble();
                if (occupant != null) {
                    if (cell.getCellType() == CellType.SAFE) {
                        throw new IllegalDestroyException("Cannot destroy marble in Safe Zone.");
                    }
                    gameManager.sendHome(occupant);
                    cell.setMarble(null);
                }
            }
        } else {
            Marble occupant = target.getMarble();
            if (occupant != null) {
                if (target.getCellType() == CellType.SAFE) {
                    throw new IllegalDestroyException("Cannot destroy marble in Safe Zone.");
                }
                gameManager.sendHome(occupant);
                target.setMarble(null);
            }
        }

        start.setMarble(null);
        target.setMarble(marble);

        if (target.isTrap()) {
            target.setMarble(null);
            gameManager.sendHome(marble);
            assignTrapCell();
        }
    }


    private void validateSwap(Marble marble1, Marble marble2) throws IllegalSwapException {
        int pos1 = getPositionInPath(track, marble1);
        int pos2 = getPositionInPath(track, marble2);
        
        if (pos1 == -1 || pos2 == -1) {
            throw new IllegalSwapException("The two marbles aren't on the track.");
        }

        Cell cell1 = track.get(pos1);
        Cell cell2 = track.get(pos2);
        
        if (cell1.getCellType() == CellType.SAFE || cell2.getCellType() == CellType.SAFE) {
            throw new IllegalSwapException("The opponent's marble is safe in its own Base Cell.");
        }
    }
    private void validateDestroy(int positionInPath) throws IllegalDestroyException {
        if (positionInPath < 0 || positionInPath >= track.size()) {
            throw new IllegalDestroyException("Destroying a marble that isn't on the track.");
        }
        
        Cell cell = track.get(positionInPath);
        if (cell.getCellType() == CellType.SAFE) {
            throw new IllegalDestroyException("Destroying a marble that is safe in their Base Cell.");
        }
    }
    private void validateFielding(Cell occupiedBaseCell) throws CannotFieldException {
        if (occupiedBaseCell.getMarble() != null && 
            occupiedBaseCell.getMarble().getColour() == gameManager.getActivePlayerColour()) {
            throw new CannotFieldException("A marble of the same colour as the player is already in the Base Cell.");
        }
    }

    private void validateSaving(int positionInSafeZone, int positionInTrack) throws InvalidMarbleException {
        if (positionInSafeZone != -1 || positionInTrack == -1) {
            throw new InvalidMarbleException("The marble was already in the Safe Zone or it wasn't on the track.");
        }
    }
    @Override
    public void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException {
        ArrayList<Cell> fullPath = validateSteps(marble, steps);
        validatePath(marble, fullPath, destroy);
        move(marble, fullPath, destroy);
    }
    private void validatePath(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalMovementException {
        if (fullPath == null || fullPath.size() < 2) {
            throw new IllegalMovementException("Path is invalid or too short.");
        }

        Cell start = fullPath.get(0);
        Cell target = fullPath.get(fullPath.size() - 1);
        Colour playerColour = marble.getColour();
        int blockageCount = 0;

        for (int i = 1; i < fullPath.size(); i++) {
            Cell cell = fullPath.get(i);
            Marble occupant = cell.getMarble();
            CellType type = cell.getCellType();

            boolean isTarget = (i == fullPath.size() - 1);

            // Rule (d) – Safe zone immunity
            if (type == CellType.SAFE && occupant != null) {
                throw new IllegalMovementException("Cannot land on or bypass marbles in Safe Zone.");
            }

            if (occupant != null) {
                boolean sameOwner = occupant.getColour().equals(playerColour);

                if (sameOwner) {
                    throw new IllegalMovementException("Cannot land on or bypass own marbles.");
                }

                if (!destroy || !isTarget) {
                    blockageCount++;
                }
            }

            // Rule (b) – Safe Zone Entry block applies to the entire path
            if (type == CellType.ENTRY && occupant != null && !isTarget) {
                throw new IllegalMovementException("Safe Zone Entry is blocked.");
            }
        }

        // Rule (a) – Cannot pass more than one opponent marble (even if not destroyed)
        if (!destroy && blockageCount > 1) {
            throw new IllegalMovementException("Cannot move: more than one marble blocking the path.");
        }
    }

    @Override
    public void destroyMarble(Marble marble) throws IllegalDestroyException {
        if (marble == null) {
            throw new IllegalDestroyException("Cannot destroy null marble.");
        }

        int trackPos = getPositionInPath(track, marble);
        ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
        int safePos = (safeZone != null) ? getPositionInPath(safeZone, marble) : -1;

        if (trackPos == -1 && safePos == -1) {
            throw new IllegalDestroyException("Marble not found on board.");
        }

        if (safePos != -1) {
            throw new IllegalDestroyException("Cannot destroy marble in safe zone.");
        }

        Cell cell = track.get(trackPos);
        cell.setMarble(null);
        gameManager.sendHome(marble);
    }
    @Override
    public void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException {
        if (marble == null) {
            throw new CannotFieldException("Cannot field null marble.");
        }

        int basePosition = getBasePosition(marble.getColour());
        if (basePosition == -1) {
            throw new CannotFieldException("Invalid base position for marble colour.");
        }

        Cell baseCell = track.get(basePosition);
        if (baseCell.getMarble() != null) {
            if (baseCell.getMarble().getColour() != marble.getColour()) {
                destroyMarble(baseCell.getMarble());
            } else {
                throw new CannotFieldException("Base occupied by same colour marble.");
            }
        }

        int trackPos = getPositionInPath(track, marble);
        if (trackPos != -1) {
            track.get(trackPos).setMarble(null);
        }

        ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
        int safePos = (safeZone != null) ? getPositionInPath(safeZone, marble) : -1;
        if (safePos != -1) {
            safeZone.get(safePos).setMarble(null);
        }

        baseCell.setMarble(marble);
    }
    @Override
    public void sendToSafe(Marble marble) throws InvalidMarbleException {
        int trackPos = getPositionInPath(track, marble);
        ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
        int safePos = getPositionInPath(safeZone, marble);
        
        validateSaving(safePos, trackPos);
        
        for (Cell cell : safeZone) {
            if (cell.getMarble() == null) {
                track.get(trackPos).setMarble(null);
                cell.setMarble(marble);
                return;
            }
        }
    }

    @Override
    public ArrayList<Marble> getActionableMarbles() {
        ArrayList<Marble> actionableMarbles = new ArrayList<>();
        Colour activeColour = gameManager.getActivePlayerColour();

        for (Cell cell : track) {
            Marble marble = cell.getMarble();
            if (marble != null && marble.getColour() == activeColour) {
                actionableMarbles.add(marble);
            }
        }

        ArrayList<Cell> safeZone = getSafeZone(activeColour);
        if (safeZone != null) {
            for (Cell cell : safeZone) {
                Marble marble = cell.getMarble();
                if (marble != null) {
                    actionableMarbles.add(marble);
                }
            }
        }

        return actionableMarbles;
    }

    @Override
    public void swap(Marble marble1, Marble marble2) throws IllegalSwapException {
        if (marble1 == null || marble2 == null) {
            throw new IllegalSwapException("Cannot swap with null marble.");
        }

        int pos1 = getPositionInPath(track, marble1);
        int pos2 = getPositionInPath(track, marble2);

        if (pos1 == -1 || pos2 == -1) {
            throw new IllegalSwapException("Both marbles must be on the track to swap.");
        }

        Cell cell1 = track.get(pos1);
        Cell cell2 = track.get(pos2);

        if (cell1.getCellType() != CellType.NORMAL || cell2.getCellType() != CellType.NORMAL) {
            throw new IllegalSwapException("Can only swap marbles on normal cells.");
        }

        cell1.setMarble(marble2);
        cell2.setMarble(marble1);

        if (cell1.isTrap()) {
            gameManager.sendHome(marble2);
        }
        if (cell2.isTrap()) {
            gameManager.sendHome(marble1);
        }
    }
    private int getEntryPosition(Colour colour) {
        if (colour == null) {
            return -1;
        }
        int basePosition = getBasePosition(colour);
        if (basePosition == -1) {
            return -1;
        }
        return (basePosition - 2 + 100) % 100;
    }

    private ArrayList<Cell> validateSteps(Marble marble, int steps) throws IllegalMovementException {
        ArrayList<Cell> fullPath = new ArrayList<>();

        if (marble == null) {
            throw new IllegalMovementException("Marble is null. Cannot move.");
        }

        int trackPos = getPositionInPath(track, marble);
        ArrayList<Cell> marbleSafeZone = getSafeZone(marble.getColour());
        int safePos = (marbleSafeZone != null) ? getPositionInPath(marbleSafeZone, marble) : -1;

        boolean isOnTrack = (trackPos != -1);
        boolean isInSafe = (safePos != -1);

        if (!isOnTrack && !isInSafe) {
            throw new IllegalMovementException("Marble cannot be moved if it's not on track or in safe zone.");
        }

        if (isOnTrack) {
            int entryPosition = getEntryPosition(marble.getColour());
            if (entryPosition == -1) {
                throw new IllegalMovementException("No valid entry cell for this colour.");
            }

            int distanceToEntry;
            if (entryPosition >= trackPos) {
                distanceToEntry = entryPosition - trackPos;
            } else {
                distanceToEntry = (entryPosition + track.size()) - trackPos;
            }

            int safeZoneSize = (marbleSafeZone != null) ? marbleSafeZone.size() : 0;

            if (steps > distanceToEntry + safeZoneSize) {
                throw new IllegalMovementException("The rank of the card played is too high; cannot move that far.");
            }

            if (steps > distanceToEntry) {
                int currentPos = trackPos;
                while (currentPos != entryPosition) {
                    fullPath.add(track.get(currentPos));
                    currentPos = (currentPos + 1) % track.size();
                }
                fullPath.add(track.get(entryPosition));

                int remainingSteps = steps - distanceToEntry;

                Colour activeColour = gameManager.getActivePlayerColour();
                if (steps == 5 && !marble.getColour().equals(activeColour)) {
                    int posOnTrack = entryPosition;
                    for (int i = 0; i < remainingSteps; i++) {
                        posOnTrack = (posOnTrack + 1) % track.size();
                        fullPath.add(track.get(posOnTrack));
                    }
                } else {
                    for (int i = 0; i <remainingSteps && i < safeZoneSize; i++) {
                        fullPath.add(marbleSafeZone.get(i));
                    }
                }
            } else {
                if (steps < 0) {
                    int currentPos = trackPos;
                    for (int i = 0; i < Math.abs(steps); i++) {
                        currentPos = (currentPos - 1 + track.size()) % track.size();
                        fullPath.add(track.get(currentPos));
                    }
                } else {
                    int currentPos = trackPos;
                    for (int i = 0; i < steps; i++) {
                        currentPos = (currentPos + 1) % track.size();
                        fullPath.add(track.get(currentPos));
                    }
                }
            }

        } else {
            if (steps < 0) {
                throw new IllegalMovementException("Cannot move backwards in the Safe Zone.");
            }
            if (safePos + steps >= marbleSafeZone.size()) {
                throw new IllegalMovementException("The rank of the card played is too high within the Safe Zone.");
            }
            for (int i = 0; i <= steps; i++) {
                fullPath.add(marbleSafeZone.get(safePos + i));
            }
        }

        return fullPath;
    }





    
}
