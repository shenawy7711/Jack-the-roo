package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.player.Marble;

public class Five extends Standard {

    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }

    
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return marbles != null && !marbles.isEmpty();
    }

    
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        try {
            boardManager.moveBy(marbles.get(0), 5, false);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }
}
