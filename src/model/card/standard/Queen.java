package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
<<<<<<< Updated upstream
=======
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;
>>>>>>> Stashed changes

public class Queen extends Standard {

    public Queen(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 12, suit, boardManager, gameManager);
    }
<<<<<<< Updated upstream

=======

    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles.isEmpty() || super.validateMarbleSize(marbles);
    }

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (marbles.isEmpty()) 
            this.gameManager.discardCard();
        
        else
            super.act(marbles);
    }

>>>>>>> Stashed changes
}
