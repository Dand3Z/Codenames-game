package pl.dele.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.dele.cards.PackDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PackDetailsTest {

    @Test
    void ShouldCreatePackWith25CardsAmount9StartingTeamCardsAnd1BlackCard(){
        PackDetails stats = new PackDetails();
        assertEquals(25,stats.getAMOUNT());
        assertEquals(1,stats.getBLACK_CARDS_AMOUNT());

        // blue team starts
        if (stats.isSTARTING_TEAM()){
            assertEquals(9, stats.getBLUE_CARDS_AMOUNT());
            assertEquals(8, stats.getRED_CARDS_AMOUNT());
        } else {
            assertEquals(8, stats.getBLUE_CARDS_AMOUNT());
            assertEquals(9, stats.getRED_CARDS_AMOUNT());
        }

        assertEquals(25-9-8-1, stats.getNEUTRAL_CARDS_AMOUNT());
    }

    @Test
    void ShouldCreatePackStatsWith36CardsAmount11StartingTeamCards2BlackCards(){
        PackDetails stats = new PackDetails(36,11,2);
        assertEquals(36, stats.getAMOUNT());
        assertEquals(2,stats.getBLACK_CARDS_AMOUNT());

        // blue team starts
        if (stats.isSTARTING_TEAM()){
            assertEquals(11, stats.getBLUE_CARDS_AMOUNT());
            assertEquals(10, stats.getRED_CARDS_AMOUNT());
        } else {
            assertEquals(10, stats.getBLUE_CARDS_AMOUNT());
            assertEquals(11, stats.getRED_CARDS_AMOUNT());
        }

        assertEquals(36-11-10-2, stats.getNEUTRAL_CARDS_AMOUNT());
    }

    @Test
    void ShouldThrowIllegalArgumentExceptionWhenCardsAmountIsLessEqual0(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new PackDetails(-4,9,1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new PackDetails(0,9,1));
    }

    @Test
    void ShouldThrowIllegalArgumentExceptionWhenStartingTeamAmountIsLessEqual0(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new PackDetails(25,-2,1));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new PackDetails(25,0,1));
    }

    @Test
    void ShouldThrowIllegalArgumentExceptionWhenBlackAmountIsLessThen0(){
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new PackDetails(25,9,-4));

        Assertions.assertDoesNotThrow(() -> new PackDetails(25,9,0));
    }
}
