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
    

    
    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1;
    }

    
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null || marbles.size() != 1) {
            throw new InvalidMarbleException("Five card requires exactly one marble.");
        }

        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Invalid marble colours for Five card.");
        }

        try {
        	Marble marble1 =marbles.get(0);
            boardManager.moveBy(marble1, 5, false);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }

}
