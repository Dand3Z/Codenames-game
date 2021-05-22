package pl.dele.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dele.teams.TeamColor;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PackTest {

    private IGenerateCards generator;

    @BeforeEach
    void init(){
        generator = new FileCardsReader();
    }

    @Test
    void shouldCreatePack(){
        Pack pack = new Pack(generator, 25);
        assertEquals(25, pack.getCards().size());

        pack = new Pack(generator, 36);
        assertEquals(36, pack.getCards().size());

    }

    @Test
    void shouldThrowInvalidParameterExceptionWhenYouTryMakeInvalidPack(){
        Assertions.assertThrows(InvalidParameterException.class, () -> new Pack(generator, 24));
        Assertions.assertThrows(InvalidParameterException.class, () -> new Pack(generator, -1));
        Assertions.assertThrows(InvalidParameterException.class, () -> new Pack(generator, 123));
        Assertions.assertThrows(InvalidParameterException.class, () -> new Pack(generator, 25,-1,1));
        Assertions.assertThrows(InvalidParameterException.class, () -> new Pack(generator, 25,9,-1));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenGeneratorIsNull(){
        Assertions.assertThrows(NullPointerException.class, () -> new Pack(null, 25));
    }

    @Test
    void shouldStartTeamWithGreaterAmountCardsToGuess(){
        // initial
        List<Card> cards = generator.generatePack(25);
        PackDetails details = new PackDetails(25,9,1);
        RoleMap cardsRoles = new RoleMap(cards, details);

        Pack pack = new Pack(cards, details, cardsRoles);

        // --- Test 1 ---
        // check which team should start
        TeamColor startingTeam;
        if (details.getRED_CARDS_AMOUNT() > details.getBLUE_CARDS_AMOUNT()){
            startingTeam = TeamColor.RED_TEAM;
        } else startingTeam = TeamColor.BLUE_TEAM;

        assertEquals(pack.whichTeamStarts(), startingTeam);

        // --- Test 2 ---
        startingTeam = cardsRoles.amountOf(CardRole.BLUE_TEAM) >
                cardsRoles.amountOf(CardRole.RED_TEAM) ? TeamColor.BLUE_TEAM :
                TeamColor.RED_TEAM;

        assertEquals(pack.whichTeamStarts(), startingTeam);
    }

    @Test
    void shouldContainsAppointedCard(){
        Pack pack = new Pack(generator, 25);
        Card card = pack.getCards().get(3);
        assertTrue(pack.containsCard(card.getPhrase()));
    }

    @Test
    void shouldNotContainsAppointedCard(){
        Pack pack = new Pack(generator, 25);
        Card card = new Card("aaaaaa");
        assertFalse(pack.containsCard(card.getPhrase()));
    }
}
