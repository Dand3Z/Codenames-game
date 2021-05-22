package pl.dele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    /**
     * Object creation test
     */

    @Test
    public void shouldMakeCard(){
        // given
        Card card1 = new Card("HorSe");
        String text = "cat";
        // when
        Card card2 = new Card(text);
        // then
        assertEquals("HORSE", card1.getText());
        assertEquals(text.toUpperCase(), card2.getText());
    }

    /**
     * Test equals() and hashcode() methods
     */
    @Test
    public void ShouldCompareCard(){
        // given
        Card horse1 = new Card("HorSe");
        Card horse2 = new Card("horse");
        Card dog1 = new Card("DoG");
        Card dog2 = new Card("dog");

        // then
        assertTrue(horse1.equals(horse2));
        assertTrue(dog1.equals(dog2));
        assertFalse(horse1.equals(dog1));

        assertEquals(horse1.hashCode(), horse2.hashCode());
        assertEquals(dog1.hashCode(), dog2.hashCode());
        assertNotEquals(dog1.hashCode(), horse1.hashCode());
    }

    @Test
    public void shouldHashcodeBeEqual(){
        Card c1 = new Card("horSe");
        Card c2 = new Card("HORse");

        Set<Card> cards = new HashSet<>();
        cards.add(c1);
        cards.add(c2);

        assertEquals(c1,c2);
        Assertions.assertEquals(1, cards.size());
    }
}
