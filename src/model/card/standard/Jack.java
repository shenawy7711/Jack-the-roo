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
        return marbles != null && marbles.size() == 2; // jack swaps 2 cards 
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.size() != 2) {
            return false;
        }
        Colour currentPlayer = gameManager.getActivePlayerColour();
        int countActive = 0;
        for (Marble m : marbles) {
            if (m.getColour() == currentPlayer) {
            	countActive++;
            }
        }
        return (countActive == 1);
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Seven card requires one or two marbles.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Invalid marble colours.");
        }

        try {
            if (marbles.size() == 1) {
                boardManager.moveBy(marbles.get(0), 7, false);
            } else {
                int split = boardManager.getSplitDistance();
                if (split < 1 || split > 6) {
                    throw new SplitOutOfRangeException("Split value must be between 1 and 6.");
                }

                boardManager.moveBy(marbles.get(0), split, false);       // First marble
                boardManager.moveBy(marbles.get(1), 7 - split, false);   // Second marble
            }
        } catch (IllegalMovementException | IllegalDestroyException | SplitOutOfRangeException e) {
            throw new StandardActionException(e.getMessage());
        }
    }


}
