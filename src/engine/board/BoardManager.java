package engine.board;

import exception.IllegalMovementException;
import exception.IllegalDestroyException;
import exception.IllegalSwapException;
import exception.CannotFieldException;
import exception.InvalidMarbleException;
import model.player.Marble;
import java.util.ArrayList;

public interface BoardManager {
    int getSplitDistance();
    void moveBy(Marble marble, int steps, boolean destroy) throws IllegalMovementException, IllegalDestroyException;
    void swap(Marble marble1, Marble marble2) throws IllegalSwapException;
    void destroyMarble(Marble marble) throws IllegalDestroyException;
    void sendToBase(Marble marble) throws CannotFieldException, IllegalDestroyException;
    void sendToSafe(Marble marble) throws InvalidMarbleException;
    ArrayList<Marble> getActionableMarbles();
}
