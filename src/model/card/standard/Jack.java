package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.IllegalSwapException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Jack extends Standard {

    public Jack(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 11, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && (marbles.size() == 1 || marbles.size() == 2);
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null) {
            return false;
        }
        
        Colour currentPlayer = gameManager.getActivePlayerColour();
        
        if (marbles.size() == 1) {
            return marbles.get(0).getColour() == currentPlayer;
        }
        
        if (marbles.size() != 2) {
            return false;
        }
        
        int countActive = 0;
        for (Marble m : marbles) {
            if (m.getColour() == currentPlayer) {
                countActive++;
            }
        }
        return countActive == 1;
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null) {
            throw new InvalidMarbleException("Marbles cannot be null.");
        }

        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Invalid number of marbles.");
        }

        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Invalid marble colours.");
        }

        try {
            if (marbles.size() == 1) {
                Marble marble = marbles.get(0);
                boardManager.moveBy(marble, 11, false);
            } else {
                Marble activeMarble = null;
                Marble otherMarble = null;
                
                for (Marble m : marbles) {
                    if (m.getColour() == gameManager.getActivePlayerColour()) {
                        activeMarble = m;
                    } else {
                        otherMarble = m;
                    }
                }
                
                boardManager.swap(activeMarble, otherMarble);
            }
        } catch (IllegalMovementException | IllegalDestroyException | IllegalSwapException e) {
            throw new StandardActionException(e.getMessage());
        }
    }
}
