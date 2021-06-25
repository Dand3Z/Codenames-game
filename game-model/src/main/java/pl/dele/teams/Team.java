package pl.dele.teams;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private Spymaster spymaster;
    private List<Operative> operatives;
    private TeamColor teamColor;

    public Team(TeamColor teamColor) {
        operatives = new ArrayList<>();
        this.teamColor = teamColor;
    }

    public void addSpymaster(Spymaster newSpymaster){
        if (newSpymaster == null) throw new IllegalArgumentException("Argument is null!");
        if (spymaster != null) throw new IllegalAccessError("Spymaster is assigned!");
        this.spymaster = newSpymaster;
    }

    public void addOperative(Operative newOperative){
        if (newOperative == null || operatives.contains(newOperative))
            throw new IllegalArgumentException("Invalid argument!");
        this.operatives.add(newOperative);
    }

    public void removeSpymaster(){
        if (spymaster == null) throw new IllegalArgumentException("Spymaster isn't assigned");
        spymaster = null;
    }

    public void removeOperative(Operative operative){
        if (operative == null) throw new IllegalArgumentException("Operative is null!");
        if (operatives.contains(operative)) operatives.remove(operative);
    }

    // == getters ==
    public Spymaster getSpymaster() { return spymaster; }
    public List<Operative> getOperatives() { return operatives; }
    public TeamColor getTeamColor() { return teamColor; }
}
