package model.card;

import engine.board.BoardManager;
import engine.GameManager;
import model.card.standard.*;
import model.card.wild.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {

    // Class attribute for the CSV file name
    public static final String CARDS_FILE = "Cards.csv";

    // Class attribute to store the pool of cards
    private static ArrayList<Card> cardsPool = new ArrayList<>();

    // Method to load the card pool from the CSV file
    public static void loadCardPool(BoardManager boardManager, GameManager gameManager) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int code = Integer.parseInt(data[0].trim());
                int frequency = Integer.parseInt(data[1].trim());
                String name = data[2].trim();
                String description = data[3].trim();

                // Determine the type of card based on the code
                for (int i = 0; i < frequency; i++) {
                    Card card = createCard(code, name, description, boardManager, gameManager, data);
                    if (card != null) {
                        cardsPool.add(card);
                    }
                }
            }
        }
    }

    // Helper method to create the appropriate card based on the code
    private static Card createCard(int code, String name, String description, BoardManager boardManager, GameManager gameManager, String[] data) {
        switch (code) {
            case 1: // Ace
            case 4: // Four
            case 5: // Five
            case 7: // Seven
            case 10: // Ten
            case 11: // Jack
            case 12: // Queen
            case 13: // King
                // Create a generic Standard card
                int rank = Integer.parseInt(data[4].trim()); // Extract rank from CSV
                Suit suit = Suit.valueOf(data[5].trim()); // Extract suit from CSV
                return new Standard(name, description, rank, suit, boardManager, gameManager);
            case 14: // Burner (Wild Card)
                return new Burner(name, description, boardManager, gameManager);
            case 15: // Saver (Wild Card)
                return new Saver(name, description, boardManager, gameManager);
            default:
                return null; // Ignore unsupported cards
        }
    }

    // Method to draw cards from the pool
    public static ArrayList<Card> drawCards() {
        Collections.shuffle(cardsPool);
        ArrayList<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < 4 && !cardsPool.isEmpty(); i++) {
            drawnCards.add(cardsPool.remove(0));
        }
        return drawnCards;
    }
    
    public static void shuffleDeck() {
        Collections.shuffle(cardsPool);
    }

    public static int getDeckSize() {
        return cardsPool.size();
    }
}
