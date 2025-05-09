package model.card;

import engine.GameManager;
import engine.board.BoardManager;

public abstract class Card {
	private final String name;
    private final String description;
    protected BoardManager boardManager;
    protected GameManager gameManager;

    public Card(String name, String description, BoardManager boardManager, GameManager gameManager) {
        this.name = name;
        this.description = description;
        this.boardManager = boardManager;
        this.gameManager = gameManager;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
<<<<<<< Updated upstream
=======
    
    public abstract void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException;
    
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles.size() == 1;
    }
    
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        Colour ownerColour = gameManager.getActivePlayerColour();
        boolean sameColour = true;
        for (Marble marble : marbles) {
            if (marble.getColour() != ownerColour) {
                sameColour = false;
            }
        }
        return sameColour;
    }
>>>>>>> Stashed changes
    
}
