package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.Player;

public class Game implements GameManager{
	private final Board board;
	private final ArrayList<Player> players;
	private final ArrayList<Card> firePit;
	private int currentPlayerIndex;
	private int turn;
	public Game(String playerName) throws IOException {
	    ArrayList<Colour> colourOrder =new ArrayList<>(Arrays.asList(Colour.values())); 
	    Collections.shuffle(colourOrder);
	    board = new Board(colourOrder, this);
	    Deck.loadCardPool(board,this );
	    firePit = new ArrayList<>();
	    players = new ArrayList<>();
	    players.add(new Player(playerName, colourOrder.get(0)));

	    for (int i = 1; i < colourOrder.size(); i++) {
	        players.add(new Player("CPU " + i, colourOrder.get(i)));
	    }
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
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
