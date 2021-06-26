package pl.dele;

import pl.dele.cards.Card;
import pl.dele.cards.CardRole;
import pl.dele.cards.Pack;
import pl.dele.teams.Operative;
import pl.dele.teams.Spymaster;
import pl.dele.teams.Team;
import pl.dele.teams.TeamColor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameEngine {

    private final Pack pack;
    private final Team redTeam;
    private final Team blueTeam;
    private final PropertyChangeSupport observerSupport;
    // left to guess ..... (map)

    private TeamColor guessingTeam; // whose turn is now

    public GameEngine(Team redTeam, Team blueTeam, Pack pack) {
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.pack = pack;
        this.guessingTeam = pack.whichTeamStarts();

        observerSupport = new PropertyChangeSupport(this);
    }

    public GameEngine(Team redTeam, Team blueTeam){
        this(redTeam, blueTeam, new Pack());
    }

    public GameEngine(Team redTeam, Team blueTeam, String path){
        this(redTeam, blueTeam, new Pack(path));
    }

    private CardRole whatRoleIsIt(String phrase){
        if (phrase == null || !pack.containsCard(phrase)) throw new IllegalArgumentException("Invalid argument!");
        return pack.getCardRole(new Card(phrase));
    }

    public boolean isCorrectAnswer(String phrase){
        if (phrase == null) throw new IllegalArgumentException();

        CardRole role = whatRoleIsIt(phrase);
        if (guessingTeam == TeamColor.BLUE_TEAM){
            switch (role){
                case BLUE_TEAM:
                    return true;
                case RED_TEAM:
                case NEUTRAL:
                    return false;
                case BLACK_CARD:
                    gameover();
                    return false;
            }
        }
        else {
            switch (role){
                case RED_TEAM:
                    return true;
                case BLUE_TEAM:
                case NEUTRAL:
                    return false;
                case BLACK_CARD:
                    gameover();
                    return false;
            }
        }
        return false;
    }

    private int amountOf(CardRole cardRole) { return pack.amountOf(cardRole); }

    // private int leftToGuess() {}

    private void gameover() {}

    public void pass(){}

    //public boolean isGameWon() {}



    private void changeGuessingTeam(){
        guessingTeam = (guessingTeam == TeamColor.BLUE_TEAM) ? TeamColor.RED_TEAM : TeamColor.BLUE_TEAM;
    }

    // == join to team ==
    public void addRedTeamSpymaster(Spymaster spymaster){
        redTeam.addSpymaster(spymaster);
    }

    public void addBlueTeamSpymaster(Spymaster spymaster){
        blueTeam.addSpymaster(spymaster);
    }

    public void addRedTeamOperative(Operative operative){
        redTeam.addOperative(operative);
    }

    public void addBlueTeamOperative(Operative operative){
        blueTeam.addOperative(operative);
    }

    public Pack getPack(){
        return new Pack(pack);
    }

    // == OBSERVER ==
    public void addObserver(String eventType, PropertyChangeListener obs){
        observerSupport.addPropertyChangeListener(eventType, obs);
    }

    public void removeObserver(PropertyChangeListener obs){
        observerSupport.removePropertyChangeListener(obs);
    }

    public void notifyObservers(PropertyChangeEvent event){
        observerSupport.firePropertyChange(event);
    }

}
