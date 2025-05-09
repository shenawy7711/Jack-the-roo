package model.card.wild;

import engine.GameManager;
import engine.board.BoardManager;
<<<<<<< Updated upstream
=======
import exception.ActionException;
import exception.InvalidMarbleException;
import model.player.Marble;
>>>>>>> Stashed changes

public class Saver extends Wild {

    public Saver(String name, String description, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
    }

<<<<<<< Updated upstream
=======
    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        boardManager.sendToSafe(marbles.get(0));
    }

>>>>>>> Stashed changes
}
