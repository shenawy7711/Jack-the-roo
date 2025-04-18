package model.card;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.InvalidMarbleException;
import model.Colour;
import model.player.Marble;

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
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        
        return marbles != null && marbles.size() == 1;
    }
    
    public boolean validateMarbleColours(ArrayList<Marble> marbles) {
        if (marbles == null || marbles.isEmpty()) {
            return false;
        }
        Colour currentPlayerColour = gameManager.getActivePlayerColour();
        for (Marble m : marbles) {
            if (m.getColour() != currentPlayerColour) {
                return false;
            }
        }
        return true;
    }
    public abstract void act(ArrayList<Marble> marbles) throws ActionException,
    InvalidMarbleException;
    
}
