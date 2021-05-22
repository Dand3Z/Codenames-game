package pl.dele.cards;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Assertions.assertTrue(25 == cards.size());
        Assertions.assertTrue(36 == cards2.size());
        Assertions.assertTrue(49 == cards3.size());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenInvalidAmount(){
        // no-argument constructor
        IGenerateCards generator = new FileCardsReader();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(0));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(-10));

        // amount is too big
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> generator.generatePack(100000000));
    }

    @Test
    public void shouldMakeGeneratorForTheSpecifiedPath(){
        // valid argument
        IGenerateCards generator = new FileCardsReader("src/test/java/pl/dele/cards.csv");
        List<Card> cards = generator.generatePack(25);
        Assertions.assertEquals(25, cards.size());

        // empty argument, default path should be set
        generator = new FileCardsReader("");
        cards = generator.generatePack(25);
        Assertions.assertEquals(25, cards.size());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenPathIsWrong(){
        // wrong path
        IGenerateCards generator = new FileCardsReader("src/test/cards.csv");

        Assertions.assertThrows(NullPointerException.class,
                () -> generator.generatePack(25));
    }
}
