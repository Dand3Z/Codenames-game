package pl.dele.cards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.teams.TeamColor;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * The collection of cards
 * API: get list of cards, check role of specific card, which team starts
 */
public class Pack {

    // == fields ==
    private static Logger log = LoggerFactory.getLogger(Pack.class);
    private final List<Card> cards;
    private final PackDetails details;
    private RoleMap cardsRoles;

    // == constructors ==
    public Pack(IGenerateCards generator, int amount, int startingTeamAmount,
                int blackCards) {
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

    public Pack(){
        this(new FileCardsReader(), 25, 9, 1);
    }

    public Pack(String path){
        this(new FileCardsReader(path), 25, 9,1);
    }

    public Pack(Pack pack){
        this.cards = new ArrayList<>(pack.cards);
        this.details = new PackDetails(pack.details);
        this.cardsRoles = new RoleMap(pack.cardsRoles);
    }

    // == methods ==
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public TeamColor whichTeamStarts(){
        return details.isSTARTING_TEAM() ? TeamColor.BLUE_TEAM : TeamColor.RED_TEAM;
    }

    // red, blue, neutral, black
    public CardRole getCardRole(Card card){
        return cardsRoles.getCardRole(card);
    }

    public int amountOf(CardRole cardRole){
        return cardsRoles.amountOf(cardRole);
    }

//    public RoleMap getCardsRoles(){
//        return new RoleMap(cardsRoles);
//    }

    public boolean containsCard(String phrase){
        return cards.contains(new Card(phrase));
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
