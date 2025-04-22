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

public class Burner extends Wild {

    public Burner(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager); 
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.isEmpty()) {
            return false;
        }

        Colour activeColour = gameManager.getActivePlayerColour();

        for (Marble marble : marbles) {
            if (marble.getColour().equals(activeColour)) {
                return false; // found a marble with same colour, invalid
            }
        }

        return true; // all marbles are of different colour (opponent)
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("Burner requires exactly one marble.");
        }
        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Burner can only burn an opponent’s marble on the track.");
        }
        try {
            boardManager.destroyMarble(marbles.get(0));
        } catch (IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }

}
