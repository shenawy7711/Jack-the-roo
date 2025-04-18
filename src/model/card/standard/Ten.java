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
        // Ten can operate with 0 or 1 marble:
        //  - 0 => discard from next player's hand and skip
        //  - 1 => standard 10-step move
        return marbles.size() == 0 || marbles.size() == 1;
    }

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        // If one marble is selected, it must be your (active player's) marble.
        // If zero marbles are selected, no color check needed.
        if (marbles.size() == 1) {
            Colour activeColour = gameManager.getActivePlayerColour();
            return marbles.get(0).getColour().equals(activeColour);
        }
        return true;
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        try {
            if (marbles.isEmpty()) {
                // 0 marbles => discard random card from NEXT player's hand, skip that player's turn
                Colour nextPlayerColour = gameManager.getNextPlayerColour();
                gameManager.discardCard(nextPlayerColour);
                // The “skip next turn” logic might be in the Game or GameManager after discarding.
            } else {
                // 1 marble => move 10 steps as standard movement
                boardManager.moveBy(marbles.get(0), 10, false);
            }
        } catch (CannotDiscardException e) {
            // If the next player’s hand is empty, can’t discard -> throw standard action exception
            throw new StandardActionException(e.getMessage());
        } catch (IllegalMovementException | IllegalDestroyException e) {
            // Wrap board exceptions
            throw new StandardActionException(e.getMessage());
        }
    }
}
