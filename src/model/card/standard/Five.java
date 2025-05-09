package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
<<<<<<< Updated upstream
=======
import model.player.Marble;
>>>>>>> Stashed changes

public class Five extends Standard {

    public Five(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 5, suit, boardManager, gameManager);
    }
<<<<<<< Updated upstream
=======

    @Override
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        return true;
    }
>>>>>>> Stashed changes

}
