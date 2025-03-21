package engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import engine.board.Board;
import model.Colour;
import model.card.Card;
import model.card.Deck;
import model.player.Player;
import model.player.CPU;

public class Game implements GameManager {
    private final Board board;
    private final ArrayList<Player> players;
    private final ArrayList<Card> firePit;
    private int currentPlayerIndex;
    private int turn;

    public Game(String playerName) throws IOException {
        ArrayList<Colour> colourOrder = new ArrayList<>(Arrays.asList(Colour.values())); 
        Collections.shuffle(colourOrder);
        
        board = new Board(colourOrder, this);
        Deck.loadCardPool(board, this);
        firePit = new ArrayList<>();
        players = new ArrayList<>();

        if (colourOrder.size() < 4) {
            throw new IllegalStateException("Not enough colours for players");
        }

        players.add(new Player(playerName, colourOrder.get(0)));

        for (int i = 1; i <= 3; i++) {  
            players.add(new CPU("CPU " + i, colourOrder.get(i), board));
        }

        for (Player player : players) {
            ArrayList<Card> hand = Deck.drawCards();
            player.setHand(hand);
        }
    }

   

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Card> getFirePit() {
        return firePit;
    }
}