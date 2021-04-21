package pl.dele;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * The collection of cards
 * API: get list of cards, check role of specific card, which team starts
 */
public class Pack {

    // == fields ==
    private final List<Card> cards;
    private final PackDetails details;
    private RoleMap cardsRoles;

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
        details = new PackDetails(amount, startingTeamAmount, blackCards);
        cardsRoles = new RoleMap(cards,details);
    }

    public Pack(List<Card> cards, PackDetails details, RoleMap cardsRoles) {
        if (cards == null || details == null || cardsRoles == null)
            throw new NullPointerException();

        this.cards = cards;
        this.details = details;
        this.cardsRoles = cardsRoles;
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

    public StartingTeam whichTeamStarts(){
        return details.isSTARTING_TEAM() ? StartingTeam.BLUE_TEAM : StartingTeam.RED_TEAM;
    }

    // red, blue, neutral, black
    public CardRole getCardRole(Card card){
        return cardsRoles.getCardRole(card);
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
