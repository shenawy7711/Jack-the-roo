package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Ace extends Standard {

    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 1, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && (marbles.size()==0 ||marbles.size() == 1); 
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {// max 1 marble to send back to Home Zone 
    	if (marbles == null) {
            return false;
        }
        if (marbles.size() == 0) {
            return true;
        }
        if (marbles.size() == 1) {
            Colour activeColour = gameManager.getActivePlayerColour();
            Marble Chosen = marbles.get(0);
            return Chosen.getColour() == activeColour;
        }
        
        return false;
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
       
    	if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException(
                "Ace requires 0 or 1 marble to act."
            );
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException(
                "Marble colours invalid for Ace."
            );
        }
        if (marbles.isEmpty()) {
            try {
                
                gameManager.fieldMarble();
            } catch (CannotFieldException  e) {
                throw new StandardActionException(e.getMessage()) ;
            }
            return;
        }
        Marble marble = marbles.get(0);
        try {
            boardManager.moveBy(marble,1 , false);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }
}
