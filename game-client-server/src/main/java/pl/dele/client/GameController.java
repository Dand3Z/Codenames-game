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
    //private TeamRole role;

    @FXML
    private GridPane gripMap;

    @FXML
    private Button startGameButton;

    @FXML
    private Button resetButton;

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
        for(int x = 0; x < 5; ++x){
            for(int y = 0; y < 5; ++y){
                CardTile cardTile = new CardTile();
                gripMap.add(cardTile,x,y);

                String phrase = "";
                if (cards != null && cards.size() > 0){
                    phrase = cards.get(x + 5 * y).getPhrase();
                    cardTile.addPhrase(phrase);
                }

                // paint card if it is discovered or you are a spymaster (temp impl)
                if (cards != null && cards.size() > 0) {

                    log.debug("cards size = {}", cards.size());
                    for (Card c:
                         cards) { log.debug(c.getPhrase());
                    }//debug

                    CardRole cardRole = cardRoleMap.get(new Card(phrase));
                    cardTile.setBackground(getCardColor(cardRole));
                }
            }
        }
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8888);
            log.info("Client is connecting to pl.dele.server");
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
                log.debug(command);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine().trim()) != null) {
                    log.debug(line);
                    sb.append(line).append(System.lineSeparator());
                    if(line.trim().equalsIgnoreCase("END")) break;
                }
                log.debug("test2");
                switch (command){
                    case ServerResponse.INITIAL:
                        log.debug(ServerResponse.INITIAL);
                        initialHandling(sb.toString());
                        break;
                    case ServerResponse.PAINT_CARDS:
                        log.debug(ServerResponse.PAINT_CARDS);
                        paintCardsHandling(sb.toString());
                        break;
                    case ServerResponse.JOIN_TEAM:
                        log.debug(ServerResponse.JOIN_TEAM);
                        joinHandling(sb.toString());
                        break;
                    case ServerResponse.PHRASE_INTERPRETATION:
                        log.debug(ServerResponse.PHRASE_INTERPRETATION);
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

    }





    private void implButtons() {
        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            writer.println(ServerRequest.INIT);
        });
        // impl for testing -> change it in final version
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            refreshGui();
        });
        joinRedOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("joinRedOperative");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.OPERATIVE));
            joinRedOperative.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinRedSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("joinRedSpymaster");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.SPYMASTER));
            joinRedSpymaster.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinBlueOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("joinBlueOperative");
            writer.println(joinToTeam(TeamColor.BLUE_TEAM, PlayerType.OPERATIVE));
            joinBlueOperative.setTextFill(TEAM_ROLE_SELECTED);
            disableJoinButtons();
            refreshGui();
        });
        joinBlueSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("joinBlueSpymaster");
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
