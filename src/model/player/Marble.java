package model.player; 
import model.Colour;

public class Marble {
	private final Colour colour;
	//DON'T FORGET TO ASSOCIATE WITH PLAYER

	public Marble(Colour colour) {
		this.colour = colour ;
	}
	public Colour getColour() {
		return colour;
	}
	
}
