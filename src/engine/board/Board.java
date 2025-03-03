package engine.board;

import java.util.ArrayList;
import java.util.Random;

import engine.GameManager;
import model.Colour;

public class Board implements BoardManager{
	private final GameManager gameManager;
	private ArrayList<Cell> track;
	private ArrayList<SafeZone> safeZones;
	private int splitDistance;
	
	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.gameManager=gameManager;
		track=new ArrayList<>();
		safeZones=new ArrayList<>();
		splitDistance=3;
		for (int i = 0; i < 100; i++) {
	        CellType type;
	        if (i % 25 == 0) {
	            type = CellType.BASE;
	        } else if (i == 23 || i == 48 || i==73||i==98){
	            type = CellType.ENTRY;
	        } else {
	            type = CellType.NORMAL;
	        }
	        track.add(new Cell(type));
		}
		for (int i = 0; i < 8; i++) {
	        assignTrapCell();
	    }
		for (Colour colour : colourOrder) {
	        this.safeZones.add(new SafeZone(colour));
	    }
	}
	public int getSplitDistance() {
		return splitDistance;
	}
	public void setSplitDistance(int splitDistance) {
		this.splitDistance = splitDistance;
	}
	public ArrayList<Cell> getTrack() {
		return track;
	}
	public ArrayList<SafeZone> getSafeZones() {
		return safeZones;
	}
	public void assignTrapCell() {
	    Random rand = new Random();
	    int index = rand.nextInt(track.size());

	    while (track.get(index).getCellType() != CellType.NORMAL || track.get(index).isTrap()) {
	        index = rand.nextInt(track.size());
	    }

	    track.get(index).setTrap(true);
	}
	public GameManager getGameManager() {
		return gameManager;
	}
}
