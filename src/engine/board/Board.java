package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import exception.IllegalMovementException;
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
        if (colour == null) {
            return -1;
        }

        ArrayList<Colour> colourOrder = new ArrayList<>();
        colourOrder.add(Colour.GREEN);
        colourOrder.add(Colour.RED);
        colourOrder.add(Colour.YELLOW);
        colourOrder.add(Colour.BLUE);

        int index = colourOrder.indexOf(colour);
        if (index == -1) {
            return -1;
        }

        int basePosition = index * 25;

        if (basePosition < 0 || basePosition >= track.size()) {
            return -1; 
        }
        Cell cell = track.get(basePosition);
        if (cell.getCellType() != CellType.BASE) {
            return -1; 
        }

        return basePosition;
    }
    private int getEntryPosition(Colour colour) {
        if (colour == null) {
            return -1;
        }
        int basePosition = getBasePosition(colour);
        if (basePosition == -1) {
            return -1;
        }
        int entryPosition = (basePosition - 2 + 100) % 100; 
        if (entryPosition >= 0 && entryPosition < track.size()) {
            Cell entryCell = track.get(entryPosition);
            if (entryCell.getCellType() == CellType.ENTRY) {
                return entryPosition;
            }
        }
        return -1;
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
                    for (int i = 0; i <= remainingSteps && i < safeZoneSize; i++) {
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
