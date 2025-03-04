package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.Standard;
import model.card.standard.Suit;

public class Deck {
    private static final String CARDS_FILE = "cards.csv";
    private static ArrayList<Card> cardsPool = new ArrayList<>();

    public static void loadCardPool(BoardManager boardManager, GameManager gameManager) {
        try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                try {
                    int frequency = Integer.parseInt(values[1]);
                    String name = values[2];
                    String description = values[3];

                    if (values.length == 6) {
                        int rank = Integer.parseInt(values[4]);
                        if (rank < 1) {
                            throw new IllegalArgumentException("Invalid rank for card: " + line);
                        }
                        Suit suit = Suit.valueOf(values[5].toUpperCase());
                        Standard card = new Standard(name, description, rank, suit, boardManager, gameManager);
                        addCardsToPool(card, frequency);
                    } else {
                        System.err.println("Invalid card format: " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + CARDS_FILE);
        }
    }

    private static void addCardsToPool(Card card, int frequency) {
        for (int i = 0; i < frequency; i++) {
            cardsPool.add(card);
        }
    }

    public static ArrayList<Card> getCardPool() {
        return cardsPool;
    }

    public static Card drawCard() {
        if (!cardsPool.isEmpty()) {
        	shuffleDeck();
        	for (int i = 0;i<4;i++) {
        		if (!cardsPool.isEmpty()) {
        		return cardsPool.remove(0);}
        	}
            
            
        }
        return null;
    }

    public static void shuffleDeck() {
        Collections.shuffle(cardsPool);
    }

    public static int getDeckSize() {
        return cardsPool.size();
    }
}
