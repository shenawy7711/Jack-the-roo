package model.card.wild;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Saver extends Wild {

    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }
   
    
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.isEmpty()) {
            return false;
        }
        Colour activeColour = gameManager.getActivePlayerColour();
        return marbles.get(0).getColour().equals(activeColour);
    }

   
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
    	try {
    	    boardManager.sendToSafe(marbles.get(0));
    	} catch (InvalidMarbleException e) {
    	    // or whatever exception(s) sendToSafe actually throws
    	    throw new StandardActionException(e.getMessage());
    	}
    }
}
