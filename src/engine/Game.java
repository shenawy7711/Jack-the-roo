package engine;

import java.io.IOException;
import java.util.ArrayList;

import engine.board.Board;
import model.card.Card;
import model.player.Player;

public class Game implements GameManager{
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex;
	private int turn;
	public Game(String playerName) throws IOException{
		//constructer not finished
		

	}
	public Board getBoard() {
		return board;
	}
	public ArrayList<Card> getFirePit() {
		return firePit;
	}
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
}
