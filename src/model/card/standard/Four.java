package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
<<<<<<< Updated upstream
=======
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;
>>>>>>> Stashed changes

public class Four  extends Standard {

    public Four(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 4, suit, boardManager, gameManager);
    }

<<<<<<< Updated upstream
=======
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        boardManager.moveBy(marbles.get(0), -4, false);
    }

>>>>>>> Stashed changes
}
