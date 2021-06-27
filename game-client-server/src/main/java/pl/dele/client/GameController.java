package pl.dele.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.*;

import static pl.dele.client.ClientService.*;

public class GameController extends Thread{

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

    @FXML
    private TextField clueField;

    @FXML
    private ComboBox cbGoals;

    @FXML
    private Button giveClueButton;

    @FXML
    private Label redCardsLeftLb;

    @FXML
    private Label blueCardsLeftLb;

    @FXML
    void initialize(){
        connectSocket();
        refreshGui();
        implButtons();
    }

    private static Logger log = LoggerFactory.getLogger(Client.class);
    public static final Color TEAM_ROLE_SELECTED = Color.CHOCOLATE;

    private Socket socket;
    private final List<Card> cards;
    private Map<Card, CardRole> cardRoleMap;
    private Set<Card> uncoveredCards;
    private BufferedReader reader;
    private PrintWriter writer;

    private TeamColor team;
    private PlayerType type;
    private boolean isMyTurn;

    public GameController() {
        cards = new ArrayList<>();
        cardRoleMap = new LinkedHashMap<>();
        uncoveredCards = new HashSet<>();
    }

    private void refreshGui(){
        log.debug("cards size = {}", cards.size());
        for (Card c: cards) { log.debug("Card {}", c.getPhrase()); }

        setVisibleClueFields();

        for(int x = 0; x < 5; ++x){
            for(int y = 0; y < 5; ++y){
                CardTile cardTile = new CardTile();
                gripMap.add(cardTile,x,y);

                String phrase = "";
                int current = x + 5 * y;
                if (cards != null && cards.size() > 0){
                    phrase = cards.get(current).getPhrase();
                    cardTile.addPhrase(phrase);

                    final String phrase1 = phrase;
                    cardTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        // zabezpiecz przed ponownym odkryciem karty!!!!!
                        if (isMyTurn && type == PlayerType.OPERATIVE){
                            writer.println(cardClicked(phrase1));
                        }
                    });

                    // paint card if it is discovered or you are a spymaster
                    boolean isUncovered = uncoveredCards.contains(new Card(phrase)) ? true : false;
                    if (type == PlayerType.SPYMASTER && isUncovered){
                        CardRole cardRole = cardRoleMap.get(new Card(phrase));
                        if (cardRole == null) {
                            log.error("NULL cardRole!");
                            continue;
                        }
                        cardTile.setBackground(Color.BLACK); // PLACEHOLDER
                    }
                    else if(type == PlayerType.SPYMASTER || isUncovered){
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
                    case ServerResponse.SEND_CLUE:
                        log.debug("Execute command: {}", ServerResponse.SEND_CLUE);
                        printClueHandling(sb.toString());
                    case ServerResponse.UNCOVER_CARD:
                        log.debug("Execute command: {}", ServerResponse.UNCOVER_CARD);
                        uncoverCardHandling(sb.toString());
                        break;
                    case ServerResponse.LEFT_TO_GUESS:
                        log.info("Execute command: {}", ServerResponse.LEFT_TO_GUESS);
                        leftToGuessHandling(sb.toString());
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

    private void uncoverCardHandling(String instruction) {
        String[] msg = instruction.split(System.lineSeparator());
        String phrase = msg[0];
        CardRole cardRole = getCardRole(msg[1]);

        Card uncoveredCard = new Card(phrase);
        if (uncoveredCards.contains(uncoveredCard)) return;
        uncoveredCards.add(uncoveredCard);
        cardRoleMap.put(uncoveredCard, cardRole);

        //if (!isCorrect) writer.println(nextTurn()); // here problem
        // PHRASE\nCardRole\nBoolean - isCorrect\n
        Platform.runLater(() -> {
            refreshGui();
        });
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

        Platform.runLater(() -> {
            refreshGui();
        });
    }

    private void printClueHandling(String instruction){
        String[] msg = instruction.split(System.lineSeparator());

        Platform.runLater(() -> {
            clueField.setText(msg[0]);
            cbGoals.getSelectionModel().select(Integer.parseInt(msg[1]));
            refreshGui();
        });
    }

    private void leftToGuessHandling(String instruction) {
        String[] msg = instruction.split(System.lineSeparator());
        //int redCardsLeft = Integer.parseInt(msg[0]);
        //int blueCardsLeft = Integer.parseInt(msg[1]);

        Platform.runLater(() -> {
            redCardsLeftLb.setText(msg[0]);
            blueCardsLeftLb.setText(msg[1]);
            refreshGui();
        });
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
        giveClueButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            // send clue
            writer.println(nextTurn());
            writer.println(sendClue(clueField.getText(), Integer.parseInt((String) cbGoals.getSelectionModel().getSelectedItem())));
        });
        joinRedOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinRedOperative");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.OPERATIVE));
            //joinRedOperative.setTextFill(TEAM_ROLE_SELECTED);
            try {
                joinRedOperative.getStylesheets()
                        .add((new File("game-client-server/src/main/resources/fxml/redButton.css"))
                                .toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            disableJoinButtons();
            refreshGui();
        });
        joinRedSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinRedSpymaster");
            writer.println(joinToTeam(TeamColor.RED_TEAM, PlayerType.SPYMASTER));
            //joinRedSpymaster.setTextFill(TEAM_ROLE_SELECTED);
            try {
                joinRedSpymaster.getStylesheets()
                        .add((new File("game-client-server/src/main/resources/fxml/redButton.css"))
                                .toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            disableJoinButtons();
            refreshGui();
        });
        joinBlueOperative.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinBlueOperative");
            writer.println(joinToTeam(TeamColor.BLUE_TEAM, PlayerType.OPERATIVE));
            //joinBlueOperative.setTextFill(TEAM_ROLE_SELECTED);
            try {
                joinBlueOperative.getStylesheets()
                        .add((new File("game-client-server/src/main/resources/fxml/blueButton.css"))
                                .toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            disableJoinButtons();
            refreshGui();
        });
        joinBlueSpymaster.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            log.debug("Pressed button: joinBlueSpymaster");
            writer.println(joinToTeam(TeamColor.BLUE_TEAM, PlayerType.SPYMASTER));
            //joinBlueSpymaster.setTextFill(TEAM_ROLE_SELECTED);
            try {
                joinBlueSpymaster.getStylesheets()
                        .add((new File("game-client-server/src/main/resources/fxml/blueButton.css"))
                                .toURI().toURL().toExternalForm());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
            disableJoinButtons();
            refreshGui();
        });

        ObservableList<Integer> list = FXCollections.observableArrayList();
        list.addAll(0,1,2,3,4,5,6,7,8,9);
        cbGoals.setItems(list);
    }

    private void disableJoinButtons(){
        joinBlueOperative.setDisable(true);
        joinBlueSpymaster.setDisable(true);
        joinRedSpymaster.setDisable(true);
        joinRedOperative.setDisable(true);
    }

    private void setVisibleClueFields(){
        if (type == null) return;

        switch (type){
            case SPYMASTER:
                if (isMyTurn){
                    giveClueButton.setVisible(true);
                    clueField.setEditable(true);
                    clueField.setText("");
                    cbGoals.setEditable(true);
                    cbGoals.getSelectionModel().selectFirst();
                    break;
                }
            case OPERATIVE:
                giveClueButton.setVisible(false);
                clueField.setEditable(false);
                cbGoals.setEditable(false);
                break;
        }
    }

}
