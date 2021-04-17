package pl.dele;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileCardsReaderTest {

    @Test
    public void shouldGenerateListOfSufficientLength(){
        // no-argument constructor
        IGenerateCards generator = new FileCardsReader();

        // valid amount
        List<Card> cards = generator.generatePack(25);

        // test length
        assertTrue(25 == cards.size());
    }

    @Test
    public void shouldGenerateNullWhenInvalidAmount(){
        // no-argument constructor
        IGenerateCards generator = new FileCardsReader();

        List<Card> cards = generator.generatePack(0);
        assertNull(cards);

        cards = generator.generatePack(-10);
        assertNull(cards);

        // amount is too big
        cards = generator.generatePack(100000000);
        assertNull(cards);
    }

    @Test
    public void shouldMakeGeneratorForTheSpecifiedPath(){
        // valid argument
        IGenerateCards generator = new FileCardsReader("src/test/java/pl/dele/cards.csv");
        List<Card> cards = generator.generatePack(25);
        assertEquals(25, cards.size());

        // empty argument, default path should be set
        generator = new FileCardsReader("");
        cards = generator.generatePack(25);
        assertEquals(25, cards.size());
    }

}
