package pl.dele;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * The collection of cards
 */
public class Pack {

    // == fields ==
    private static Logger log = LoggerFactory.getLogger(Pack.class);

    private final List<Card> cards;
    private int amount = 25; // 5 x 5

    // == constructors ==
    public Pack(IGenerateCards generator, int amount) {
        // protection
        if (generator == null) {
            log.error("generator is null!!!");
            throw new NullPointerException();
        }
        if (amount <= 0 || !hasIntegerRoot(amount)){
            log.error("invalid amount, amount = {}", amount);
            throw new InvalidParameterException();
        }

        cards = generator.generatePack(amount);
    }

    public Pack(IGenerateCards generator){
        this(generator, 25);
    }

    // == methods ==
    /**
     ** return copy of cards list
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * only the square number of cards is valid like
     * 1x1, 2x2, 3x3, 4x4, 5x5, ...
     * @return
     */
    private boolean hasIntegerRoot(int number){
        // invalid number
        if (number <= 0) return false;

        int root = (int) Math.sqrt(number);
        return number == (root * root);
    }
}
