package model.card.standard;

import model.card.*;
import engine.GameManager;
import engine.board.BoardManager;
public class Standard extends Card{
	private int rank;
	private final Suit suit;
	
	public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
	    super(name, description, boardManager, gameManager); // Pass these to the Card superclass
	    this.rank = rank;
	    this.suit = suit;
	}

	public int getRank() {
		return this.rank;
	}
	public Suit getSuit() {
		return suit;
	}
}
