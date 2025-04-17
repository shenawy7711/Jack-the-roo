package engine;

import model.card.Card;
import model.player.Player;
import model.Colour;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.CannotDiscardException;
import model.player.Marble;

public interface GameManager {
    void sendHome(Marble marble);
    void fieldMarble() throws CannotFieldException, IllegalDestroyException;
    void discardCard(Colour colour) throws CannotDiscardException;
    void discardCard() throws CannotDiscardException;
    Colour getActivePlayerColour();
    Colour getNextPlayerColour();
}
