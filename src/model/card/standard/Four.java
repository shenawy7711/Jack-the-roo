package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Four  extends Standard {

    public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 4, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        
        return marbles.size() == 1;
    }

    

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
    	if (marbles == null || marbles.size() != 1) {
            throw new InvalidMarbleException("four card requires exactly one marble.");
        }

        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Invalid marble colours for four card.");
        }
    	try {
            boardManager.moveBy(marbles.get(0), -4, false);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new InvalidMarbleException(e.getMessage());
        }
    }
}
