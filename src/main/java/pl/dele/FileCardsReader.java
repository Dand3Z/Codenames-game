package pl.dele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Get cards from file
 */
public class FileCardsReader implements IGenerateCards {

    // == fields ==
    // logger
    private static Logger log = LoggerFactory.getLogger(FileCardsReader.class);
    // path to the cards file
    private final String PATH;


    // == constructors ==
    public FileCardsReader(){
        this("cards.csv");
    }

    public FileCardsReader(String PATH) {
        // protection
        if (PATH == null || PATH.isEmpty()) this.PATH = "cards.csv";
        else this.PATH = PATH;
    }

    // == methods ==

    @Override
    public ArrayList<Card> generatePack(int amount) {
        // protection
        if (amount <= 0) {
            log.error("amount is less / equal to 0, amount = {}", amount);
            return null;
        }

        // load cards
        List<Card> allCards = readCards();

        // protection
        if (amount > allCards.size()){
            log.error("amount is greater than the list size, amount = {}", amount);
            return null;
        }

        // shuffle allCards
        Collections.shuffle(allCards);

        /**
         * return new list with elements from 0 inclusive
         * to amount exclusive
         */
        return new ArrayList<>(allCards.subList(0, amount));
    }

    /**
     * Create list. Read all from file, change to Cards
     * and add to the list. Return this list.
     * @return
     */
    private ArrayList<Card> readCards(){
        ArrayList<Card> allCards = new ArrayList<>();

        // try to create scanner
        Scanner scanner;
        try {
            scanner = new Scanner(new File(PATH));
        }
        catch (FileNotFoundException e) {
            log.error(e.getMessage());
            return null;
        }

        while (scanner.hasNextLine()){
            // read next line
            String text = scanner.nextLine();
            // create new Card, but check first text
            if (text.isEmpty()) continue;
            Card card = new Card(text);
            // if list doesn't contain this card, add it
            if (!allCards.contains(card)) allCards.add(card);
        }
        scanner.close();
        return allCards;
    }
}