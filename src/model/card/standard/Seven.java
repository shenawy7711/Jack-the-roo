package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.GameException;
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
        if (!validateMarbleSize(marbles)) throw new InvalidMarbleException("Invalid marble count.");
        if (!validateMarbleColours(marbles)) throw new InvalidMarbleException("Invalid marble colours.");
        try {
            if (marbles.size() == 1) {
                boardManager.moveBy(marbles.get(0), 7, false);
            } else {
                int split =  boardManager.getSplitDistance();
                if (split < 1 || split > 6) throw new SplitOutOfRangeException("Split out of range.");
                boardManager.moveBy(marbles.get(0), split, false);
                boardManager.moveBy(marbles.get(1), 7 - split, false);
            }
        } catch (GameException e) {
            throw new StandardActionException(e.getMessage());
        }
    }

    }



