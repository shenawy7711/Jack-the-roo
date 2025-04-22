package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import engine.board.Board;
import exception.GameException;
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
        players.get(currentPlayerIndex).selectMarble(marble);
    }

    public void deselectAll() {
        players.get(currentPlayerIndex).deselectAll();
    }

    public void editSplitDistance(int splitDistance) throws SplitOutOfRangeException {
        if (splitDistance < 1 || splitDistance > 6) {
            throw new SplitOutOfRangeException("Split distance must be between 1 and 6.");
        }
        board.setSplitDistance(splitDistance);
    }

    public boolean canPlayTurn() {
        return !players.get(currentPlayerIndex).getHand().isEmpty();
    }

    public void playPlayerTurn() throws GameException {
        players.get(currentPlayerIndex).play();
    }

    public void endPlayerTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (currentPlayer.getSelectedCard() != null) {
            firePit.add(currentPlayer.getSelectedCard());
            currentPlayer.getHand().remove(currentPlayer.getSelectedCard());
        }
        currentPlayer.deselectAll();
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        turn++;
        if (turn % (players.size() * 4) == 0) {
            for (Player player : players) {
                player.getHand().addAll(Deck.drawCards());
            }
            turn = 0;
        }
        if (Deck.getPoolSize() < 4) {
            Deck.refillPool(firePit);
            firePit.clear();
        }
    }

    
}
