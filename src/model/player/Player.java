package model.player;
import java.util.ArrayList;
import model.Colour;
import model.card.Card;
public class Player {
	private final String name;
	private final Colour colour;
	private ArrayList<Card> hand;
	private ArrayList<Marble> marbles;
	private final Card selectedCard;
	private ArrayList<Marble> selectedMarbles;
	
}
