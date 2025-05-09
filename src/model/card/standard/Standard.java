package model.card.standard;

import engine.GameManager;
import engine.board.BoardManager;
<<<<<<< Updated upstream
=======
import exception.ActionException;
import exception.InvalidMarbleException;
>>>>>>> Stashed changes
import model.card.Card;

public class Standard extends Card {
    private final int rank;
    private final Suit suit;

    public Standard(String name, String description, int rank, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, boardManager, gameManager);
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
<<<<<<< Updated upstream
=======

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException{
        this.boardManager.moveBy(marbles.get(0), rank, false);
    }

>>>>>>> Stashed changes
}
