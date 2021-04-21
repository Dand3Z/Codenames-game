package pl.dele;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RoleMap {

    private Map<Card, CardRole> cardsRoles;

    RoleMap(List<Card> cards, PackDetails details) {
        cardsRoles = new HashMap<>();

        int cardNum = 0;

        for (int i = 0; i < details.getBLUE_CARDS_AMOUNT(); ++i) {
            cardsRoles.put(cards.get(cardNum), CardRole.BLUE_TEAM);
            ++cardNum;
        }
        for (int i = 0; i < details.getRED_CARDS_AMOUNT(); ++i) {
            cardsRoles.put(cards.get(cardNum), CardRole.RED_TEAM);
            ++cardNum;
        }
        for (int i = 0; i < details.getNEUTRAL_CARDS_AMOUNT(); ++i) {
            cardsRoles.put(cards.get(cardNum), CardRole.NEUTRAL);
            ++cardNum;
        }
        for (int i = 0; i < details.getBLACK_CARDS_AMOUNT(); ++i) {
            cardsRoles.put(cards.get(cardNum), CardRole.BLACK_CARD);
            ++cardNum;
        }
    }

    public CardRole getCardRole(Card card){
        if (!cardsRoles.containsKey(card)) throw new InvalidParameterException();
        return cardsRoles.get(card);
    }

    // for testing
    int amountOf(CardRole cardRole){
        int amount = 0;
        for (Card c : cardsRoles.keySet()){
            if (cardsRoles.get(c).equals(cardRole)){
                ++amount;
            }
        }
        return amount;
    }
}
