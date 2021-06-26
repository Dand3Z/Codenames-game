package pl.dele.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.ServerRequest;
import pl.dele.ServerResponse;
import pl.dele.cards.Card;
import pl.dele.cards.CardRole;
import pl.dele.teams.PlayerType;
import pl.dele.teams.TeamColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static pl.dele.client.ClientService.*;

public class GameController extends Thread{

    private static Logger log = LoggerFactory.getLogger(Client.class);
    public static final Color TEAM_ROLE_SELECTED = Color.CHOCOLATE;

    private Socket socket;
    private final List<Card> cards;
    private Map<Card, CardRole> cardRoleMap;
    private Map<Card, Boolean> isCardDiscovered;
    private BufferedReader reader;
    private PrintWriter writer;

    private TeamColor team;
    private PlayerType type;
    private boolean isMyTurn;


    @FXML
    private GridPane gripMap;

    @FXML
    private Button startGameButton;

    @FXML
    private Button resetButton;

    @FXML
    private Button passButton;

    @FXML
    private Button joinRedOperative;

    @FXML
    private Button joinRedSpymaster;

    @FXML
    private Button joinBlueOperative;

    @FXML
    private Button joinBlueSpymaster;

    public GameController() {
        cards = new ArrayList<>();
        cardRoleMap = new LinkedHashMap<>();
        isCardDiscovered = new HashMap<>();
    }

    @FXML
    void initialize(){
        connectSocket();
        refreshGui();
        implButtons();
    }

    private void refreshGui(){
        log.debug("cards size = {}", cards.size());
        for (Card c: cards) { log.debug("Card {}", c.getPhrase()); }

        for(int x = 0; x < 5; ++x){
            for(int y = 0; y < 5; ++y){
                CardTile cardTile = new CardTile();
                gripMap.add(cardTile,x,y);

                String phrase = "";
                int current = x + 5 * y;
                if (cards != null && cards.size() > 0){
                    phrase = cards.get(current).getPhrase();
                    cardTile.addPhrase(phrase);

                    // paint card if it is discovered or you are a spymaster
                    boolean isDiscovered = isCardDiscovered.get(new Card(phrase)) != null
                                ? isCardDiscovered.get(new Card(phrase)) : false;
                    if(type == PlayerType.SPYMASTER || isDiscovered){
                        CardRole cardRole = cardRoleMap.get(new Card(phrase));
                        if (cardRole == null) {
                            log.error("NULL cardRole!");
                            continue;
                        }
                        cardTile.setBackground(getCardColor(cardRole));
                    }
                }
            }
        }
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8888);
            log.info("Client is connected to server");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            start();
        }
        catch (IOException e) { e.getMessage(); }
    }

    @Override
    public void run() {
        try {
            while (true){
                String command;
                while ((command = reader.readLine()) == null);
                log.debug("read command: {}", command);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine().trim()) != null) {
                    log.debug("read argument: {}", line);
                    sb.append(line).append(System.lineSeparator());
                    if(line.trim().equalsIgnoreCase("END")) break;
                }
                switch (command){
                    case ServerResponse.INITIAL:
                        log.debug("Execute command: {}",ServerResponse.INITIAL);
                        initialHandling(sb.toString());
                        break;
                    case ServerResponse.PAINT_CARDS:
                        log.debug("Execute command: {}", ServerResponse.PAINT_CARDS);
                        paintCardsHandling(sb.toString());
                        break;
                    case ServerResponse.JOIN_TEAM:
                        log.debug("Execute command: {}", ServerResponse.JOIN_TEAM);
                        joinHandling(sb.toString());
                        break;
                    case ServerResponse.CHANGE_TURN:
                        log.debug("Execute command: {}", ServerResponse.CHANGE_TURN);
                        whoseTurnHandling(sb.toString());
                        break;
                    case ServerResponse.PHRASE_INTERPRETATION:
                        log.debug("Execute command: {}", ServerResponse.PHRASE_INTERPRETATION);
                        interpretationHandling(sb.toString());
                        break;
                }
                command = null;
            }
        }
        catch (IOException e) { e.getMessage(); }

    }

    // == command handling ==
    private void initialHandling(String instruction) {
        cards.clear();
        String[] phrases = instruction.split(System.lineSeparator());
        for(String phrase: phrases){
            if(phrase.equalsIgnoreCase("END")) break;
            cards.add(new Card(phrase));
        }
        // we got list of cards, now display it on board
        Platform.runLater(() -> {
            refreshGui();
        });
    }

    private void paintCardsHandling(String instruction){
        String[] colors = instruction.split(System.lineSeparator());
        for (int i = 0; i < cards.size(); ++i){
            cardRoleMap.put(cards.get(i), stringToCardRole(colors[i]));
        }

        // temp
        Platform.runLater(() -> {
            refreshGui();
        });
    }

    private void interpretationHandling(String instruction) {

    }

    private void joinHandling(String instruction) {
        String[] roleInfo = instruction.split(System.lineSeparator());
        team = getTeamColor(roleInfo[0]);
        type = getPlayerType(roleInfo[1]);
        log.info("joinHandling(): team {}", team.toString());
        log.info("joinHandling(): type {}", type.toString());
    }

    private void whoseTurnHandling(String instruction) {
        String[] msg = instruction.split(System.lineSeparator());
        TeamColor teamColor = getTeamColor(msg[0]);
        PlayerType playerType = getPlayerType(msg[1]);
        isMyTurn = (teamColor == team && playerType == type) ? true : false;
        log.info("Is my turn: {}", isMyTurn);
    }


    private void implButtons() {
        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            writer.println(ServerRequest.INIT);
        });
        // impl for testing -> change it in final version
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            refreshGui();
        });
        passButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (isMyTurn) writer.println(nextTurn());
        });
        joinRedOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinRedOperative");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.OPERATIVE));
            joinRedOperative.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinRedSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinRedSpymaster");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.SPYMASTER));
            joinRedSpymaster.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinBlueOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinBlueOperative");
            writer.println(joinToTeam(TeamColor.BLUE_TEAM, PlayerType.OPERATIVE));
            joinBlueOperative.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinBlueSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinBlueSpymaster");
            writer.println(joinToTeam(TeamColor.BLUE_TEAM, PlayerType.SPYMASTER));
            joinBlueSpymaster.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
    }

    private void disableJoinButtons(){
        joinBlueOperative.setDisable(true);
        joinBlueSpymaster.setDisable(true);
        joinRedSpymaster.setDisable(true);
        joinRedOperative.setDisable(true);
    }

}
