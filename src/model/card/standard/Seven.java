package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Seven extends Standard {

    public Seven(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 7, suit, boardManager, gameManager);
    }
  
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        // Seven can operate on either 1 or 2 marbles:
        //  - 1 marble => move 7 steps
        //  - 2 marbles => split 7 steps between them
        return marbles.size() == 1 || marbles.size() == 2;
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        // All selected marbles must belong to the active player
        if (marbles.isEmpty()) {
            return false;
        }
        Colour activeColour = gameManager.getActivePlayerColour();
        for (Marble m : marbles) {
            if (!m.getColour().equals(activeColour)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
    	try {
    	    if (marbles.size() == 1) {
    	        boardManager.moveBy(marbles.get(0), 7, false);
    	    } else {
    	        int splitDistance = boardManager.getSplitDistance(); // If this doesn't throw it, remove from catch
    	        boardManager.moveBy(marbles.get(0), splitDistance, false);
    	        boardManager.moveBy(marbles.get(1), 7 - splitDistance, false);
    	    }
    	} catch (IllegalMovementException | IllegalDestroyException e) {
    	    throw new StandardActionException(e.getMessage());
    	}

    }


}
