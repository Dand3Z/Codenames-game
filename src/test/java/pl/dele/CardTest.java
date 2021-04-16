package pl.dele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    /**
     * Object creation test
     */
    @Test
    public void createACards(){
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
    public void compareCards(){
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
}
