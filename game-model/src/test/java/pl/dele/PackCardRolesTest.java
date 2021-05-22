package pl.dele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;

public class PackCardRolesTest {

    private Pack pack;
    private List<Card> cardList;

    @BeforeEach
    void init(){
        pack = new Pack();
        cardList = pack.getCards();
    }

    @Test
    void shouldCreateRoleMap(){
        for (Card card : cardList){
            // all card has CardRole
            Assertions.assertNotNull(pack.getCardRole(card));
        }
    }

    @Test
    void shouldThrowInvalidParameterExceptionWhenCardHasNotRole(){
        Card invalidCard = new Card("zxcxv");
        Assertions.assertThrows(InvalidParameterException.class,
                () -> pack.getCardRole(invalidCard));
    }
}
