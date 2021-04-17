package pl.dele;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PackTest {

    @Test
    public void validAmountTest(){
        // create generator
        IGenerateCards generator = new FileCardsReader();
        Pack pack = new Pack(generator, 25);
        assertEquals(25, pack.getCards().size());

        pack = new Pack(generator, 36);
        assertEquals(36, pack.getCards().size());

    }

    @Test
    public void invalidAmountTest(){
        // create generator
        IGenerateCards generator = new FileCardsReader();
        Pack pack;

        try{
            pack = new Pack(generator, 24);
            assertNull(pack);
        } catch (InvalidParameterException e){}

        try{
            pack = new Pack(generator, -1);
            assertNull(pack);
        } catch (InvalidParameterException e) {}

        try{
            pack = new Pack(generator, 123);
            assertNull(pack);
        } catch (InvalidParameterException e) {}
    }
}
