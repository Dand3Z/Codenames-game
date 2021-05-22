package pl.dele.teams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dele.teams.Operative;
import pl.dele.teams.Spymaster;
import pl.dele.teams.Team;
import pl.dele.teams.TeamColor;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private Team redTeam;
    private Team blueTeam;

    private Operative redOper1;
    private Operative redOper2;
    private Spymaster redSpy;

    private Operative blueOper1;
    private Operative blueOper2;
    private Spymaster blueSpy;

    @BeforeEach
    void init(){
        redTeam = new Team(TeamColor.RED_TEAM);
        blueTeam = new Team(TeamColor.BLUE_TEAM);

        redOper1 = new Operative("red1");
        redOper2 = new Operative("red2");
        redSpy = new Spymaster("spy");

        blueOper1 = new Operative();
        blueOper2 = new Operative();
        blueSpy = new Spymaster();

        redTeam.addOperative(redOper1);
        redTeam.addOperative(redOper2);
        redTeam.addSpymaster(redSpy);

        blueTeam.addOperative(blueOper1);
        blueTeam.addOperative(blueOper2);
        blueTeam.addSpymaster(blueSpy);
    }

    @Test
    void shouldThrowIllegalAccessErrorWhenSpymasterIsAssignedAndITryToAssignAgain(){
        Assertions.assertThrows(IllegalAccessError.class, () -> redTeam.addSpymaster(new Spymaster()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenYouTryToAddOperativeThatAlreadyIsInList(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> redTeam.addOperative(redOper2));
    }

    @Test
    void bothTeamsShouldHaveTwoOperatorsAndSpymasterAssigned(){
        assertEquals(2, redTeam.getOperatives().size());
        assertEquals(2, blueTeam.getOperatives().size());

        Assertions.assertNotNull(redTeam.getSpymaster());
        Assertions.assertNotNull(blueTeam.getSpymaster());
    }

    @Test
    void listSizeShouldBeOneWhenOneOperativeIsRemoved(){
        assertTrue(redTeam.getOperatives().contains(redOper2));
        redTeam.removeOperative(redOper2);
        assertEquals(1, redTeam.getOperatives().size());
        assertFalse(redTeam.getOperatives().contains(redOper2));
    }

    @Test
    void spymasterShouldBeNullWhenIsRemoved(){
        Assertions.assertNotNull(redTeam.getSpymaster());
        redTeam.removeSpymaster();
        Assertions.assertNull(redTeam.getSpymaster());
    }

}
