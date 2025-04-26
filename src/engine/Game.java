package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import engine.board.Board;
import engine.board.Cell;
import exception.CannotDiscardException;
import exception.CannotFieldException;
import exception.GameException;
import exception.IllegalDestroyException;
import exception.InvalidCardException;
import exception.InvalidMarbleException;
import exception.SplitOutOfRangeException;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.*;

@SuppressWarnings("unused")
public class Game implements GameManager {
    private final Board board;
    private final ArrayList<Player> players;
	private int currentPlayerIndex;
    private final ArrayList<Card> firePit;
    private int turn;

    public Game(String playerName) throws IOException {
        turn = 0;
        currentPlayerIndex = 0;
        firePit = new ArrayList<>();

        ArrayList<Colour> colourOrder = new ArrayList<>();
        
        colourOrder.addAll(Arrays.asList(Colour.values()));
        
        Collections.shuffle(colourOrder);
        
        this.board = new Board(colourOrder, this);
        
        Deck.loadCardPool(this.board, (GameManager)this);
        
        this.players = new ArrayList<>();
        this.players.add(new Player(playerName, colourOrder.get(0)));
        
        for (int i = 1; i < 4; i++) 
            this.players.add(new CPU("CPU " + i, colourOrder.get(i), this.board));
        
        for (int i = 0; i < 4; i++) 
            this.players.get(i).setHand(Deck.drawCards());
        
    }
    
    public Board getBoard() {
        return board;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Card> getFirePit() {
        return firePit;
    }
    @Override
    public Colour getActivePlayerColour() {
        return players.get(currentPlayerIndex).getColour();
    }
    public void selectCard(Card card) throws InvalidCardException {
        players.get(currentPlayerIndex).selectCard(card);
    }

    public void selectMarble(Marble marble) throws InvalidMarbleException {
        if (marble == null) {
            throw new InvalidMarbleException("Cannot select a null marble.");
        }
        if (!marble.getColour().equals(getActivePlayerColour())) {
            throw new InvalidMarbleException("Cannot select marble of different color.");
        }
        players.get(currentPlayerIndex).selectMarble(marble);
    }



    
    public void deselectAll() {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.deselectAll();
    }

    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
        if (splitDistance < 1 || splitDistance > 6) {
            throw new SplitOutOfRangeException("Split distance must be between 1 and 6.");
        }
        board.setSplitDistance(splitDistance);
    }

    public boolean canPlayTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer.getHand().size() == (4-turn);
        }



    public void playPlayerTurn() throws GameException {
        players.get(currentPlayerIndex).play();
    }

    public void endPlayerTurn() {

        Player active = players.get(currentPlayerIndex);

        /* 1. Choose which card goes to the fire-pit */
        Card toDiscard = active.getSelectedCard();          // what the player actually picked
        if (toDiscard == null && !active.getHand().isEmpty()) {
            // no card was chosen – use the first card in hand instead
            toDiscard = active.getHand().get(0);
        }
        if (toDiscard != null) {
            active.getHand().remove(toDiscard);             // remove from hand only if it is there
            firePit.add(toDiscard);                         // and burn it
        }

        /* 2. Reset player UI state */
        active.deselectAll();

        /* 3. Pass the turn to the next player */
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        /* 4. If we’re back to the first player, advance the “round” counter */
        if (currentPlayerIndex == 0) {

            int round = ++turn;                             // start counting from 1--4
            if (round == 4) {

                /* 4a. Time to start a brand-new hand for everyone */
                turn = 0;                                   // reset round counter

                // ensure the draw pile is big enough; if not, recycle the fire-pit
                int cardsNeeded = players.size() * 4;
                if (Deck.getPoolSize() < cardsNeeded) {
                    Deck.refillPool(firePit);
                    firePit.clear();
                }

                // deal four fresh cards to every player
                for (Player p : players) {
                    p.getHand().clear();
                    p.getHand().addAll(Deck.drawCards());
                }
            }
        }
    }


    public Colour checkWin() {
    	for (int i =0; i<board.getSafeZones().size();i++) {
    		if (board.getSafeZones().get(i).isFull())
    			return board.getSafeZones().get(i).getColour();
    	}
    	return null;
    }
    @Override
    public void discardCard(Colour colour) throws CannotDiscardException {
        for (Player player : players) {
            if (player.getColour() == colour) {
                if (player.getHand().isEmpty()) {
                    throw new CannotDiscardException();
                }
                Random rand = new Random();
                int randomIndex = rand.nextInt(player.getHand().size()); 
                player.getHand().remove(randomIndex);
                return;
            }
        }
    }

    
	public void discardCard() throws CannotDiscardException {
		ArrayList<Integer> candidates = new ArrayList<>();
		for (int i = 0; i < players.size(); i++) {
			if (i != currentPlayerIndex && players.get(i).getHand().size() > 0) {
				candidates.add(i);
			}
		}

		if (candidates.isEmpty()) {
			throw new CannotDiscardException();
		}

		Random rand = new Random();
		int targetIndex = candidates.get(rand.nextInt(candidates.size()));
		ArrayList<Card> hand = players.get(targetIndex).getHand();
		hand.remove(rand.nextInt(hand.size()));
	}

    
   

	@Override
	public void sendHome(Marble marble) {
	    if (marble == null) {
	        return;
	    }

	    // 1. Remove marble from the board (track cells)
	    ArrayList<Cell> trackCells = board.getTrack();
	    for (int i = 0; i < trackCells.size(); i++) {
	        Cell cell = trackCells.get(i);
	        if (cell.getMarble() != null && cell.getMarble().equals(marble)) {
	            cell.setMarble(null);
	            break;
	        }
	    }

	    // 2. Regain marble into owner's Home Zone
	    Player owner = findPlayerByColour(marble.getColour());
	    if (owner != null) {
	        owner.regainMarble(marble);
	    }
	}
	


    private Player findPlayerByColour(Colour colour) {
        for (Player p : players) {
            if (p.getColour() == colour) {
                return p;
            }
        }
        return null; // or throw an exception if not found
    }
    @Override
    public void fieldMarble() throws CannotFieldException, IllegalDestroyException {
        Player currentPlayer = players.get(currentPlayerIndex);

        Marble marble = null;

        if (!currentPlayer.getMarbles().isEmpty()) {
            marble = currentPlayer.getMarbles().get(0);
        } else {
            marble = currentPlayer.getOneMarble();
        }

        if (marble == null) {
            throw new CannotFieldException("No marbles available to field.");
        }

        this.board.sendToBase(marble);

        if (currentPlayer.getMarbles().contains(marble)) {
            currentPlayer.getMarbles().remove(marble);
        } else {
            currentPlayer.getMarbles().remove(marble);
        }
    }


	@Override
	public Colour getNextPlayerColour() {
	    int nextIndex = (currentPlayerIndex + 1) % players.size();
	    return players.get(nextIndex).getColour();
	}


  
    
}
