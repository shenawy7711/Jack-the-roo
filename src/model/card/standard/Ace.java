package model.card.standard;

import java.util.ArrayList;

import engine.GameManager;
import engine.board.BoardManager;
import exception.ActionException;
import exception.CannotFieldException;
import exception.IllegalDestroyException;
import exception.IllegalMovementException;
import exception.InvalidMarbleException;
import exception.StandardActionException;
import model.Colour;
import model.player.Marble;

public class Ace extends Standard {

    public Ace(String name, String description, Suit suit, BoardManager boardManager, GameManager gameManager) {
        super(name, description, 1, suit, boardManager, gameManager);
    }
    @Override
    public boolean validateMarbleSize(ArrayList<Marble> marbles) {
        return marbles != null && (marbles.size()==0 ||marbles.size() == 1); 
    }

    
    

    @Override
    public void act(ArrayList<Marble> marbles) throws ActionException, InvalidMarbleException {
        if (!validateMarbleSize(marbles))
            throw new InvalidMarbleException("Ace requires 0 or 1 marble to act.");
        if (!validateMarbleColours(marbles))
            throw new InvalidMarbleException("Marble colours invalid for Ace.");

        
        if (marbles == null || marbles.isEmpty()) {
           throw new CannotFieldException ();                                
        }

        try {
            boardManager.moveBy(marbles.get(0), 1, false);
        } catch (IllegalMovementException | IllegalDestroyException e) {
            throw new StandardActionException(e.getMessage());
        }
    }



        
    

}
