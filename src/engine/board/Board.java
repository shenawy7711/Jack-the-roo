package engine.board;

import java.util.ArrayList;

import engine.GameManager;
import model.Colour;

public class Board implements BoardManager {
	private final GameManager gameManager;
	private ArrayList<Cell> track;
	private ArrayList<SafeZone> safeZones;
	private int splitDistance;
	
	public Board(ArrayList<Colour> colourOrder, GameManager gameManager) {
		this.gameManager=gameManager;
		track=new ArrayList<>();
		safeZones=new ArrayList<>();
		splitDistance=3;
		
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
		
	}
}
