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

public class King extends Standard {

    public King(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 13, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && (marbles.size()==0 ||marbles.size() == 1); 
    }
  

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles == null || marbles.isEmpty()) {
            try {
                gameManager.fieldMarble();
            } catch (CannotFieldException | IllegalDestroyException e) {
                throw new StandardActionException(e.getMessage());
            }
            return;
        }

        if (!validateMarbleSize(marbles)) {
            throw new InvalidMarbleException("King requires 0 or 1 marble.");
        }

        if (!validateMarbleColours(marbles)) {
            throw new InvalidMarbleException("Invalid marble colours for King.");
        }

        try {
            boardManager.moveBy(marbles.get(0), 13, true);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }

}
