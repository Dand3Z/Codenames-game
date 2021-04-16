package pl.dele;

import java.util.ArrayList;
import java.util.List;

/**
 * The collection of cards
 */
public class Waist {

    // == fields ==
    private final List<Card> cards;
    private int amount = 25; // 5 x 5

    // == constructors ==
    public Waist() {
        // IDownloadCards ...
        // IDownloadCards get all cards and
        // return (amount) cards
        cards = null; //temp
    }

    // == methods ==
    /**
     ** return copy of cards list
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
}
