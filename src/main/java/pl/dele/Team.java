package pl.dele;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class Team {

    private Spymaster spymaster;
    private List<Operative> operatives;
    private TeamColor teamColor;

    Team(TeamColor teamColor) {
        operatives = new ArrayList<>();
        this.teamColor = teamColor;
    }

    public void addSpymaster(Spymaster newSpymaster){
        if (spymaster != null) throw new IllegalAccessError();
        this.spymaster = newSpymaster;
    }

    public void add(Operative newOperative){
        if (newOperative == null || operatives.contains(newOperative))
            throw new InvalidParameterException();
        this.operatives.add(newOperative);
    }
}
