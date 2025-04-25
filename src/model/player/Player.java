package model.player;

import java.util.ArrayList;

import exception.GameException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import model.Colour;
import model.card.Card;

@SuppressWarnings("unused")
public class Player {
    private final String name;
    private final Colour colour;
    private ArrayList<Card> hand;
    private final ArrayList<Marble> marbles;
    private Card selectedCard;
	private final ArrayList<Marble> selectedMarbles;

    public Player(String name, Colour colour) {
        this.name = name;
        this.colour = colour;
        this.hand = new ArrayList<>();
        this.selectedMarbles = new ArrayList<>();
        this.marbles = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            this.marbles.add(new Marble(colour));
        }
        
        //default value
        this.selectedCard = null;
    }

    public String getName() {
        return name;
    }

    public Colour getColour() {
        return colour;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    
    public ArrayList<Marble> getMarbles() {
		return marbles;
	}
    
    public Card getSelectedCard() {
        return selectedCard;
    }
    
    public void regainMarble(Marble marble) {
    	this.marbles.add(marble);
    }
    
    public Marble getOneMarble() {
    	if (!this.marbles.isEmpty()) 
    		return this.marbles.get(0);
    	else 
    		return null;
    }
    
    public void selectCard(Card card) throws InvalidCardException {
        if (!hand.contains(card)) {
            throw new InvalidCardException("Selected card is not in the player's hand.");
        }
        this.selectedCard = card;
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {

        if (selectedMarbles.contains(marble)) {
            return; // already selected, do nothing
        }
        selectedMarbles.add(marble);
    }

    public void deselectAll() {
        this.selectedCard = null;
        this.selectedMarbles.clear();
    }

    public void play() throws GameException {
        if (selectedCard == null) {
            throw new InvalidCardException("No card selected.");
        }
        if (!selectedCard.validateMarbleSize(selectedMarbles)) {
            throw new InvalidMarbleException("Incorrect number of marbles selected for this card.");
        }
        if (!selectedCard.validateMarbleColours(selectedMarbles)) {
            throw new InvalidMarbleException("Invalid marble color selection for this card.");
        }
        selectedCard.act(selectedMarbles);
    }

	

}
