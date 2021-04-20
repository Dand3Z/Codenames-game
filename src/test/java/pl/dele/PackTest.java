package pl.dele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PackTest {

    private IGenerateCards generator;

    @BeforeEach
    void init(){
        // create generator
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
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 24));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, -1));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 123));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 25,-1,1));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 25,9,-1));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenGeneratorIsNull(){
        assertThrows(NullPointerException.class, () -> new Pack(null, 25));
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
        StartingTeam startingTeam;
        if (details.getRED_CARDS_AMOUNT() > details.getBLUE_CARDS_AMOUNT()){
            startingTeam = StartingTeam.RED_TEAM;
        } else startingTeam = StartingTeam.BLUE_TEAM;

        assertEquals(pack.whichTeamStarts(), startingTeam);

        // --- Test 2 ---
        startingTeam = cardsRoles.amountOf(CardRole.BLUE_TEAM) >
                cardsRoles.amountOf(CardRole.RED_TEAM) ? StartingTeam.BLUE_TEAM :
                StartingTeam.RED_TEAM;

        assertEquals(pack.whichTeamStarts(), startingTeam);
    }
}
