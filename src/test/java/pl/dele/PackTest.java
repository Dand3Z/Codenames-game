package pl.dele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

public class PackTest {

    private IGenerateCards generator;

    @BeforeEach
    public void init(){
        // create generator
        generator = new FileCardsReader();
    }

    @Test
    public void shouldCreatePack(){
        Pack pack = new Pack(generator, 25);
        assertEquals(25, pack.getCards().size());

        pack = new Pack(generator, 36);
        assertEquals(36, pack.getCards().size());

    }

    @Test
    public void shouldThrowInvalidParameterExceptionWhenYouTryMakeInvalidPack(){

        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 24));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, -1));
        assertThrows(InvalidParameterException.class, () -> new Pack(generator, 123));
    }
}
