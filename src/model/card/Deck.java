package model.card;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import model.card.standard.*;
import engine.GameManager;
import engine.board.BoardManager;
import model.card.standard.Standard;
import model.card.standard.Seven; // Importing Seven class
import model.card.standard.Suit;
import model.card.wild.Burner;
import java.util.*;
import java.util.regex.*;
import model.card.wild.Saver;

public class Deck {
    private static final String CARDS_FILE = "Cards.csv"; // Ensure the correct file name
    private static ArrayList<Card> cardsPool = new ArrayList<>();


    public static void loadCardPool(BoardManager boardManager, GameManager gameManager)
            throws IOException {
        cardsPool = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(CARDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);
                addCardsToPool(boardManager, gameManager, values, line);
            }
        }
    }
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|([^,]+)").matcher(line);
        
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                values.add(matcher.group(1)); // Quoted field
            } else {
                values.add(matcher.group(2)); // Regular field
            }
        }
        return values.toArray(new String[0]);
    }

    public static void addCardsToPool(BoardManager boardManager, GameManager gameManager, String[] row, String line) {
        int code = Integer.parseInt(row[0]);
        int frequency = Integer.parseInt(row[1]);
        for (int i = 0; i < frequency; i++) {
            Card temporary;
            switch (code) {
                case 0:
                    temporary = new Standard(row[2], row[3], Integer.parseInt(row[4]), Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 1:
                    temporary = new Ace(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 13:
                    temporary = new King(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 12:
                    temporary = new Queen(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 11:
                    temporary = new Jack(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 4:
                    temporary = new Four(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 5:
                    temporary = new Five(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 7:
                    temporary = new Seven(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 10:
                    temporary = new Ten(row[2], row[3], Suit.valueOf(row[5]), boardManager, gameManager);
                    break;
                case 14:
                    temporary = new Burner(row[2], row[3], boardManager, gameManager);
                    break;
                case 15:
                    temporary = new Saver(row[2], row[3], boardManager, gameManager);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid Card Code: " + line);
            }
            cardsPool.add(temporary);
        }
    }





    public static ArrayList<Card> getCardPool() {
        return new ArrayList<>(cardsPool); // Return a copy to avoid modification issues
    }

    public static ArrayList<Card> drawCards() {

        Collections.shuffle(cardsPool);
        ArrayList<Card> drawnCards = new ArrayList<>();
        int drawCount = Math.min(4, cardsPool.size());

        for (int i = 0; i < drawCount; i++) {
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