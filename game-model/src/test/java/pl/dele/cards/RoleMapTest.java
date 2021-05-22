package pl.dele.cards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleMapTest {

    @Test
    void shouldCreateNewRoleMapFromAnotherRoleMap(){
        List<Card> cards = new FileCardsReader().generatePack(25);
        PackDetails details = new PackDetails();
        RoleMap roleMap = new RoleMap(cards, details);

        RoleMap secondRoleMap = new RoleMap(roleMap);
        assertEquals(roleMap, secondRoleMap);
    }
}
