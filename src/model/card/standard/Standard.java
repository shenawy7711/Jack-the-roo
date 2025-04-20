package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.card.Card;
import model.player.Marble;

public class Standard extends Card {
    private final int rank;
    private final Suit suit;

    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
     
	

	@Override
	public boolean validateMarbleColours(ArrayList<Marble> marbles) {
		
		return super.validateMarbleColours(marbles);
	}

	@Override
	public void act(ArrayList<Marble> marbles) 
	        throws ActionException, InvalidMarbleException {
	    
	    if (!validateMarbleSize(marbles)) {
	        throw new InvalidMarbleException(
	            "Invalid number of marbles for a Standard card."
	        );
	    }
	    
	    if (!validateMarbleColours(marbles)) {
	        throw new InvalidMarbleException(
	            "Marble colour(s) are invalid for this Standard card."
	        );
	    }

	    Marble marble = marbles.get(0);

	    try {
	        boardManager.moveBy(marble, rank, false);
	    } catch (IllegalMovementException | IllegalDestroyException e) {
	        throw new StandardActionException(e.getMessage());
	    }
	}

}
