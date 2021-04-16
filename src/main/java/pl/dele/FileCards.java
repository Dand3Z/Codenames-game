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
public class FileCards implements IDownloadCards{

    // == fields ==
    // logger
    private static Logger log = LoggerFactory.getLogger(FileCards.class);
    // path to the cards file
    private final String PATH;


    // == constructors ==
    public FileCards(){
        this("cards.csv");
    }

    public FileCards(String PATH) {
        this.PATH = PATH;
    }

    // == methods ==

    @Override
    public ArrayList<Card> generatePack(int amount) {
        List<Card> allCards = readCards();
        // shuffle allCards
        Collections.shuffle(allCards);

        /**
         * return new list with elements from 0 to amount - 1
         */
        return new ArrayList<>(allCards.subList(0, amount - 1));
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
            // create new Card
            Card card = new Card(text);
            // if list doesn't contain this card, add it
            if (!allCards.contains(card)) allCards.add(card);
        }
        return allCards;
    }
}