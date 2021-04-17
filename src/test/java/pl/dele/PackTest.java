package pl.dele;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;

public class PackTest {

    @Test
    public void invalidAmountTest(){
        // no-argument constructor
        IGenerateCards generator = new FileCardsReader();

        // invalid amounts
        List<Card> cards = generator.generatePack(24);
        assertNull(cards);

        cards = generator.generatePack(-1);
        assertNull(cards);

        cards = generator.generatePack(123);
        assertNull(cards);
    }
}
