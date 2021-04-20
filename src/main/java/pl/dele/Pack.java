package pl.dele;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * The collection of cards
 */
public class Pack {

    // == fields ==
    private final List<Card> cards;
    private final PackStats stats;
    // == constructors ==
    public Pack(IGenerateCards generator, int amount, int startingTeamAmount,
                int blackCards) {
        // protection
        if (generator == null) {
            throw new NullPointerException();
        }
        if (amount <= 0 || startingTeamAmount <=0 || blackCards < 0
                || !hasIntegerRoot(amount)){
            throw new InvalidParameterException();
        }

        cards = generator.generatePack(amount);
        stats = new PackStats(amount, startingTeamAmount, blackCards);
    }

    public Pack(IGenerateCards generator, int amount){
        this(generator, amount, 9, 1);
    }

    // Default Pack
    public Pack(){
        this(new FileCardsReader(), 25, 9, 1);
    }

    // == methods ==
    /**
     ** return copy of cards list
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    PackStats getStats() {
        return stats;
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
