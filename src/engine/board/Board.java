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
		if (colour == null)
			return -1;

		for (int i = 0; i < safeZones.size(); i++) {
			if (safeZones.get(i).getColour() == colour)
				return i * 25;
		}

		return -1;
	}

    private void move(Marble marble, ArrayList<Cell> fullPath, boolean destroy) throws IllegalDestroyException {
        if (marble == null || fullPath == null || fullPath.isEmpty()) 
            return;
            
        Cell start = fullPath.get(0);
        Cell target = fullPath.get(fullPath.size() - 1);

        if (destroy) {
            for (int i = 1; i < fullPath.size(); i++) {
                Cell cell = fullPath.get(i);
                if (cell.getMarble() != null) {
                    if (cell.getCellType() == CellType.SAFE || 
                        (cell.getCellType() == CellType.BASE && 
                         cell.getMarble().getColour() == cell.getMarble().getColour()))
                        throw new IllegalDestroyException("Cannot destroy marble in protected cell.");
                    gameManager.sendHome(cell.getMarble());
                    cell.setMarble(null);
                }
            }
        }

        if (target.getMarble() != null) {
            if (target.getCellType() == CellType.SAFE || 
                (target.getCellType() == CellType.BASE && 
                 target.getMarble().getColour() == target.getMarble().getColour()))
                throw new IllegalDestroyException("Cannot destroy marble in protected cell.");
            gameManager.sendHome(target.getMarble());
            target.setMarble(null);
        }

        start.setMarble(null);
        target.setMarble(marble);

        if (target.isTrap()) {
            assignTrapCell();
            target.setTrap(false);
            gameManager.sendHome(marble);
            target.setMarble(null);
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
        
        // Get active player's colour from the game manager
        Colour activeColour = gameManager.getActivePlayerColour();

        // If a marble is in a Base cell and does NOT belong to the active player then it is illegal.
        if (cell1.getCellType() == CellType.BASE && !marble1.getColour().equals(activeColour)) {
            throw new IllegalSwapException("The opponent's marble is safe in its own Base Cell.");
        }
        if (cell2.getCellType() == CellType.BASE && !marble2.getColour().equals(activeColour)) {
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
    private void validatePath(Marble movingMarble, ArrayList<Cell> path, boolean destroy)
            throws IllegalMovementException, IllegalDestroyException {

        if (path == null || path.isEmpty()) {
            throw new IllegalMovementException("Invalid path: path cannot be null or empty");
        }

        Colour activePlayerColour = gameManager.getActivePlayerColour();
        int encounteredMarbles = 0;
        Cell finalCell = path.get(path.size() - 1);
        boolean movingToSafeZone = finalCell.getCellType() == CellType.SAFE;

        for (int step = 1; step < path.size(); step++) {
            Cell currentCell = path.get(step);
            Marble currentMarble = currentCell.getMarble();

            if (currentMarble != null) {
                if (currentCell.getCellType() == CellType.SAFE) {
                    throw new IllegalMovementException("Invalid move: cannot pass through safe zone marble.");
                }

                boolean marbleAtItsBase = getPositionInPath(track, currentMarble) == getBasePosition(currentMarble.getColour());
                if (marbleAtItsBase && currentCell.getCellType() == CellType.BASE) {
                    throw new IllegalMovementException("Invalid move: marble in base cell cannot be bypassed.");
                }

                boolean samePlayerMarble = currentMarble.getColour() == activePlayerColour;

                if (!destroy) {
                    if (samePlayerMarble) {
                        throw new IllegalMovementException("Invalid move: your marble blocks this path.");
                    }

                    if (step < path.size() - 1) {
                        encounteredMarbles++;
                        if (encounteredMarbles > 1) {
                            throw new IllegalMovementException("Invalid move: multiple marbles obstruct the way.");
                        }
                    }

                    if (movingToSafeZone && currentCell.getCellType() == CellType.ENTRY) {
                        throw new IllegalMovementException("Cannot enter safe zone: entry cell is occupied.");
                    }
                }
            }
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
        if (cell.getCellType() == CellType.BASE && marble.getColour().equals(marble.getColour())) {
            throw new IllegalDestroyException("Cannot destroy marble in its own base cell.");
        }

        cell.setMarble(null);
        gameManager.sendHome(marble);
    }
    @Override
    public void sendToBase(Marble marble) throws CannotFieldException {
        if (marble == null)
            throw new CannotFieldException("Cannot field null marble.");

        int basePos = getBasePosition(marble.getColour());
        if (basePos == -1)
            throw new CannotFieldException("Invalid base position for marble colour.");

        Cell baseCell = track.get(basePos);

        //handle occupant without calling destroyMarble()  
        Marble occupant = baseCell.getMarble();
        if (occupant != null) {
            if (occupant.getColour() == marble.getColour()) {
                throw new CannotFieldException("Base cell already occupied by same-colour marble.");
            } else {
                gameManager.sendHome(occupant);   
                baseCell.setMarble(null);         
            }
        }
        int trackPos = getPositionInPath(track, marble);
        if (trackPos != -1 && trackPos != basePos)
            track.get(trackPos).setMarble(null);

        ArrayList<Cell> safeZone = getSafeZone(marble.getColour());
        int safePos = (safeZone != null) ? getPositionInPath(safeZone, marble) : -1;
        if (safePos != -1)
            safeZone.get(safePos).setMarble(null);

        // finally place the marble in its base
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
            fullPath.add(track.get(trackPos));

            int entryPosition = getEntryPosition(marble.getColour());
            if (entryPosition == -1) {
                throw new IllegalMovementException("No valid entry cell for this colour.");
            }

            int distanceToEntry = (entryPosition >= trackPos)
                    ? entryPosition - trackPos
                    : (entryPosition + track.size()) - trackPos;

            int safeZoneSize = (marbleSafeZone != null) ? marbleSafeZone.size() : 0;

            if (steps > distanceToEntry + safeZoneSize) {
                throw new IllegalMovementException("The rank of the card played is too high; cannot move that far.");
            }

            if (steps > distanceToEntry) {
                if (trackPos != entryPosition) {
                    int currentPos = (trackPos + 1) % track.size();
                    while (currentPos != entryPosition) {
                        fullPath.add(track.get(currentPos));
                        currentPos = (currentPos + 1) % track.size();
                    }
                    fullPath.add(track.get(entryPosition));
                }

                int remainingSteps = steps - distanceToEntry;
                Colour activeColour = gameManager.getActivePlayerColour();
                if (!marble.getColour().equals(activeColour)) {
                    int posOnTrack = entryPosition;
                    for (int i = 0; i < remainingSteps; i++) {
                        posOnTrack = (posOnTrack + 1) % track.size();
                        fullPath.add(track.get(posOnTrack));
                    }
                } else {
                    for (int i = 0; i < remainingSteps && i < safeZoneSize; i++) {
                        fullPath.add(marbleSafeZone.get(i));
                    }
                }
            } else {
                int currentPos = trackPos;
                if (steps < 0) {
                    for (int i = 0; i < Math.abs(steps); i++) {
                        currentPos = (currentPos - 1 + track.size()) % track.size();
                        fullPath.add(track.get(currentPos));
                    }
                } else {
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
