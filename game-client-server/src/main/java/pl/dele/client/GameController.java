package pl.dele.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.Commands;
import pl.dele.cards.Card;
import pl.dele.cards.CardRole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class GameController extends Thread{

    private static Logger log = LoggerFactory.getLogger(Client.class);

    private Socket socket;
    private final List<Card> cards;
    private Map<Card, CardRole> cardRoleMap;
    private Map<Card, Boolean> isCardDiscovered;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean selectedTeam;

    @FXML
    private GridPane gripMap;

    @FXML
    private Button startGameButton;

    @FXML
    private Button resetButton;

    public GameController() {
        cards = new ArrayList<>();
        cardRoleMap = new LinkedHashMap<>();
        isCardDiscovered = new HashMap<>();
    }

    @FXML
    void initialize(){
        connectSocket();
        refreshGui();

        startGameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            writer.println("init");
        });
        // impl for testing
        resetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            refreshGui();
        });
    }

    private void refreshGui(){
        for(int x = 0; x < 5; ++x){
            for(int y = 0; y < 5; ++y){
                CardTile cardTile = new CardTile();
                gripMap.add(cardTile,x,y);

                String phrase = cards.get(x + 5 * y).getPhrase();
                if (cards != null && cards.size() > 0){
                    cardTile.addPhrase(phrase);
                }

                // paint card if it is discovered or you are a spymaster (temp impl)
                if (cards != null && cards.size() > 0) {
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
                    case Commands.INITIAL:
                        log.debug(Commands.INITIAL);
                        initialHandling(sb.toString());
                        break;
                    case Commands.PAINT_CARDS:
                        log.debug(Commands.PAINT_CARDS);
                        paintCardsHandling(sb.toString());
                        break;
                    case Commands.JOIN_TEAM:
                        log.debug(Commands.JOIN_TEAM);
                        joinHandling(sb.toString());
                        break;
                    case Commands.PHRASE_INTERPRETATION:
                        log.debug(Commands.PHRASE_INTERPRETATION);
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
    }

    private void interpretationHandling(String instruction) {

    }

    private void joinHandling(String instruction) {

    }

    private CardRole stringToCardRole(String s){
        switch (s){
            case "RED_TEAM":
                return CardRole.RED_TEAM;
            case "BLUE_TEAM":
                return CardRole.BLUE_TEAM;
            case "NEUTRAL":
                return CardRole.NEUTRAL;
            default:
                return CardRole.BLACK_CARD;
        }
    }

    private Color getCardColor(CardRole cardRole){
        switch (cardRole){
            case RED_TEAM:
                return Color.RED;
            case BLUE_TEAM:
                return Color.BLUE;
            case BLACK_CARD:
                return Color.BLACK;
            default:
                return Color.DIMGREY;
        }
    }

}
