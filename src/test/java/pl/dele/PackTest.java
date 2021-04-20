package pl.dele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import static org.junit.jupiter.api.Assertions.*;

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

}
