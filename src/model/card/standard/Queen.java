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

public class Queen extends Standard {

    public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 12, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && marbles.size() == 1; 
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
       
    	if (marbles.isEmpty()) {
            try {
                gameManager.discardCard();
            } catch (CannotDiscardException e) {
                throw new StandardActionException(e.getMessage());
            }
        } 
        else {
            try {
                boardManager.moveBy(marbles.get(0), 12, false);
            } catch (IllegalMovementException | IllegalDestroyException e) {
                throw new StandardActionException(e.getMessage());
            }
        }
    }
}
