package pl.dele;

import pl.dele.cards.Card;
import pl.dele.cards.CardRole;
import pl.dele.cards.Pack;
import pl.dele.teams.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public class GameEngine {

    public static final String GAME_OVER = "GAME_OVER";
    private final PropertyChangeSupport observerSupport;

    private final Pack pack;
    private final Team redTeam;
    private final Team blueTeam;
    private final Map<Card, CardRole> uncoveredCards;
    // left to guess ..... (map)
    // amount of - how many of each cardRole is

    private TeamColor guessingTeam; // whose turn is now
    private PlayerType guessingRole; // whose role has turn

    public GameEngine(Team redTeam, Team blueTeam, Pack pack) {
        this.redTeam = redTeam;
        this.blueTeam = blueTeam;
        this.pack = pack;
        this.guessingTeam = pack.whichTeamStarts();
        this.guessingRole = PlayerType.SPYMASTER;  // spymaster always starts
        this.uncoveredCards = new HashMap<>();

        observerSupport = new PropertyChangeSupport(this);
    }

    public GameEngine(Team redTeam, Team blueTeam){
        this(redTeam, blueTeam, new Pack());
    }

    public GameEngine(Team redTeam, Team blueTeam, String path){
        this(redTeam, blueTeam, new Pack(path));
    }

    private CardRole whatRoleIsIt(String phrase){
        if (phrase == null || !pack.containsCard(phrase)) throw new IllegalArgumentException("Card not in the pack or null");
        return pack.getCardRole(new Card(phrase));
    }

    public CardRole uncoverCard(String phrase){
        if (phrase == null || !pack.containsCard(phrase)) throw new IllegalArgumentException("Card not in the pack or null");

        Card uncoveredCard = new Card(phrase);
        if (uncoveredCards.containsKey(uncoveredCard)) throw new IllegalArgumentException("Card already is uncovered");

        CardRole cardRole = whatRoleIsIt(phrase);
        uncoveredCards.put(uncoveredCard, cardRole);

        return cardRole;
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
                    gameOver();
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
                    gameOver();
                    return false;
            }
        }
        return false;
    }

    private int amountOf(CardRole cardRole) { return pack.amountOf(cardRole); }

    public int howManyCardsLeft(CardRole checkingRole) {
        int uncovered = 0;
        for (CardRole role : uncoveredCards.values()){
            if (role == checkingRole) ++uncovered;
        }
        return amountOf(checkingRole) - uncovered;
    }

    public void gameOver() {
        TeamColor winner = (guessingTeam == TeamColor.BLUE_TEAM) ? TeamColor.RED_TEAM : TeamColor.BLUE_TEAM;
        notifyObservers(new PropertyChangeEvent(this, GAME_OVER, guessingTeam, winner)); // loser, winner
    }

    public void pass(){}

    public boolean isGameWon() {
        return howManyCardsLeft(mapTeamColorToCardRole(guessingTeam)) <= 0;
    }

    private CardRole mapTeamColorToCardRole(TeamColor teamColor){
        switch (teamColor){
            case BLUE_TEAM:
                return CardRole.BLUE_TEAM;
            default:
                return CardRole.RED_TEAM;
        }
    }

    @Deprecated
    private void changeGuessingTeam(){
        guessingTeam = (guessingTeam == TeamColor.BLUE_TEAM) ? TeamColor.RED_TEAM : TeamColor.BLUE_TEAM;
    }

    @Deprecated
    private void changeGuessingRole(){
        guessingRole = (guessingRole == PlayerType.SPYMASTER) ? PlayerType.OPERATIVE : PlayerType.SPYMASTER;
    }

    public void nextTurn(){
        if (guessingRole == PlayerType.SPYMASTER) guessingRole = PlayerType.OPERATIVE;
        else {
            guessingTeam = (guessingTeam == TeamColor.BLUE_TEAM) ? TeamColor.RED_TEAM : TeamColor.BLUE_TEAM;
            guessingRole = PlayerType.SPYMASTER;
        }
    }

    public TeamColor whoseTeamGuessing(){ return guessingTeam; }
    public PlayerType whoseRoleHasTurn(){ return guessingRole; }

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
