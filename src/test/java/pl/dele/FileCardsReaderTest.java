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
        List<Card> cards2 = generator.generatePack(36);
        List<Card> cards3 = generator.generatePack(49);

        // test length
        assertTrue(25 == cards.size());
        assertTrue(36 == cards2.size());
        assertTrue(49 == cards3.size());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenInvalidAmount(){
        // no-argument constructor
        IGenerateCards generator = new FileCardsReader();
        assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(0));

        assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(-10));

        // amount is too big
        assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(100000000));
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

    @Test
    public void shouldThrowNullPointerExceptionWhenPathIsWrong(){
        // wrong path
        IGenerateCards generator = new FileCardsReader("src/test/cards.csv");

        assertThrows(NullPointerException.class,
                () -> generator.generatePack(25));
    }
}
