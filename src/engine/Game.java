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

        // Ensure at least 4 colours (1 human + 3 CPUs)
        if (colourOrder.size() < 4) {
            throw new IllegalStateException("Not enough colours for players");
        }

        // Add human player first
        players.add(new Player(playerName, colourOrder.get(0)));

        // Add exactly 3 CPU players with correct names
        for (int i = 1; i <= 3; i++) {  
            players.add(new Player("CPU " + i, colourOrder.get(i)));
        }
    }


    public void setTurn(int turn) {
        this.turn = turn;
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
