package model.card.standard;


import engine.GameManager;
import engine.board.BoardManager;
public class Queen extends Standard {//rank12
	public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
		super(name, description,12,suit, boardManager, gameManager);
	}
}
