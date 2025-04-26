package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.CannotDiscardException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Ten extends Standard {

    public Ten(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 10, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles.size() == 0 || marbles.size() == 1;
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles.size() == 1) {
            Colour activeColour = gameManager.getActivePlayerColour();
            return marbles.get(0).getColour().equals(activeColour);
        }
        return true;
    }
    // Discard a random card from the next player's hand.
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
    	if (marbles == null || marbles.isEmpty()) {
            try {
                Colour nextPlayerColour = gameManager.getNextPlayerColour();
                gameManager.discardCard(nextPlayerColour);
            } catch (CannotDiscardException e) {
                throw e; 
            }
            return;
        }

        if (marbles.size() == 1) {
            try {
                boardManager.moveBy(marbles.get(0), 10, false);
            } catch (IllegalMovementException | IllegalDestroyException e) {
                throw new StandardActionException(e.getMessage());
            }
        } else {
            throw new InvalidMarbleException("Ten card requires either zero or one marble selected.");
        }
    }


}
