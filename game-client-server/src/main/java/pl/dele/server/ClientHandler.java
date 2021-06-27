package pl.dele.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.GameEngine;
import pl.dele.ServerRequest;
import pl.dele.ServerResponse;
import pl.dele.cards.Card;
import pl.dele.cards.CardRole;
import pl.dele.teams.Operative;
import pl.dele.teams.PlayerType;
import pl.dele.teams.Spymaster;
import pl.dele.teams.TeamColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static pl.dele.server.ServerService.getPlayerType;
import static pl.dele.server.ServerService.getTeamColor;

public class ClientHandler extends Thread {

    private static Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private final List<ClientHandler> clients;
    private final Socket socket;
    private final GameEngine gameEngine;
    private BufferedReader reader;
    private PrintWriter writer;
    private TeamColor team;
    private PlayerType type;

    public ClientHandler(Socket socket, List<ClientHandler> clients, GameEngine gameEngine){
        this.socket = socket;
        this.clients = clients;
        this.gameEngine = gameEngine;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(),true);
        }
        catch (IOException e) { e.getMessage(); }
    }

    @Override
    public void run() {
        try{
            String msg;
            while (true) {
                while ((msg = reader.readLine().trim()) != null){
                    switch (msg){
                        case ServerRequest.JOIN:
                            log.info("Execute JOIN");
                            team = getTeamColor(reader.readLine().trim());
                            type = getPlayerType(reader.readLine().trim());
                            log.info("teamColor: {}" ,team.toString());
                            log.info("playerType: {}", type.toString());
                            joinTheTeam(team, type);
                            // send responsde
                            sendRoleInfo(team, type);
                            break;
                        case "card":
                            // card analysis
                            break;
                        case ServerRequest.INIT:
                            log.info("Execute INIT");
                            initialCards();
                            break;
                        case ServerRequest.NEXT_TURN:
                            log.info("Execute NEXT_TURN");
                            gameEngine.nextTurn();
                            whoseTurnIsNow();
                            break;
                        case ServerRequest.SEND_CLUE:
                            log.info("Execute SEND_CLUE");
                            String clue = reader.readLine().trim();
                            int index = Integer.parseInt(reader.readLine().trim());
                            sendClue(clue, index);
                            break;
                        case ServerRequest.CHECK_CARD:
                            log.info("Execute CHECK_CARD");
                            String phrase = reader.readLine().trim();
                            // send card Role and mark that card as discovered !!!!!!!
                            uncoverCard(phrase);

                        default:
                            // other commands ...
                            break;
                    }
                }
            }
        }
        catch (IOException e) { e.getMessage(); }
        finally {
            try {
                reader.close();
                writer.close();
                socket.close();
            }
            catch (IOException e) { e.getMessage(); }
        }
    }

    private synchronized void initialCards(){

        StringBuilder initialBuilder = new StringBuilder();
        // fill the cards board
        log.debug("Send cards phrases");
        initialBuilder.append(ServerResponse.INITIAL).append(System.lineSeparator());
        for(Card card: gameEngine.getPack().getCards()){
            initialBuilder.append(card.getPhrase()).append(System.lineSeparator());
        }
        sendCommandToAll(initialBuilder);

        // color the cards on board
        log.debug("Send cards colors");
        StringBuilder colorBuilder = new StringBuilder();
        colorBuilder.append(ServerResponse.PAINT_CARDS).append(System.lineSeparator());
        for(Card card: gameEngine.getPack().getCards()){
            colorBuilder.append(gameEngine.getPack().getCardRole(card)).append(System.lineSeparator());
        }
        sendCommandToAll(colorBuilder);

        // send whose team starts the game
        whoseTurnIsNow();
    }

    private synchronized void sendRoleInfo(TeamColor teamColor, PlayerType playerType) {

        StringBuilder roleInfo = new StringBuilder();
        roleInfo.append(ServerResponse.JOIN_TEAM).append(System.lineSeparator())
                .append(teamColor).append(System.lineSeparator())
                .append(playerType).append(System.lineSeparator());

        sendCommandToOne(roleInfo);
    }

    private synchronized void joinTheTeam(TeamColor teamColor, PlayerType playerType){
        Operative operative;
        Spymaster spymaster;

        switch (teamColor){
            case BLUE_TEAM:
                switch (playerType){
                    case OPERATIVE:
                        operative = new Operative();
                        gameEngine.addBlueTeamOperative(operative);
                        break;
                    default:
                        spymaster = new Spymaster();
                        gameEngine.addBlueTeamSpymaster(spymaster);
                        break;
                }
                break;

            default:
                switch (playerType){
                    case OPERATIVE:
                        operative = new Operative();
                        gameEngine.addRedTeamOperative(operative);
                        break;
                    default:
                        spymaster = new Spymaster();
                        gameEngine.addRedTeamSpymaster(spymaster);
                        break;
                }
                break;
        }
    }

    private synchronized void whoseTurnIsNow(){
        StringBuilder whoseTurn = new StringBuilder();
        whoseTurn.append(ServerResponse.CHANGE_TURN).append(System.lineSeparator())
                 .append(gameEngine.whoseTeamGuessing()).append(System.lineSeparator())
                 .append(gameEngine.whoseRoleHasTurn()).append(System.lineSeparator());
        sendCommandToAll(whoseTurn);
    }

    private synchronized void sendClue(String tip, int index){
        StringBuilder clue = new StringBuilder();
        clue.append(ServerResponse.SEND_CLUE).append(System.lineSeparator())
            .append(tip).append(System.lineSeparator())
            .append(index).append(System.lineSeparator());
        sendCommandToAll(clue);
    }

    private synchronized void uncoverCard(String phrase) {
        StringBuilder uncover = new StringBuilder();
        uncover.append(ServerResponse.UNCOVER_CARD).append(System.lineSeparator())
               .append(phrase).append(System.lineSeparator())
               .append(gameEngine.uncoverCard(phrase)).append(System.lineSeparator());
               //.append(gameEngine.isCorrectAnswer(phrase)).append(System.lineSeparator());
        sendCommandToAll(uncover);

        if (!gameEngine.isCorrectAnswer(phrase)) {
            log.info("Execute NEXT_TURN");
            gameEngine.nextTurn();
            whoseTurnIsNow();
        }

        sendLeftCardsToGuess();
    }

    private synchronized void sendLeftCardsToGuess(){
        StringBuilder leftCards = new StringBuilder();
        leftCards.append(ServerResponse.LEFT_TO_GUESS).append(System.lineSeparator())
                .append(gameEngine.howManyCardsLeft(CardRole.RED_TEAM)).append(System.lineSeparator())
                .append(gameEngine.howManyCardsLeft(CardRole.BLUE_TEAM)).append(System.lineSeparator());
        sendCommandToAll(leftCards);
    }

    private synchronized void sendCommandToAll(StringBuilder sb){
        sb.append("END").append(System.lineSeparator());
        String command = sb.toString();
        for(ClientHandler client: clients){
            log.debug("Send response to {}", client.getName());
            client.writer.println(command.trim());
        }
    }

    private synchronized void sendCommandToOne(StringBuilder sb){
        sb.append("END").append(System.lineSeparator());
        String command = sb.toString();
        log.debug("Send response to {}", getName());
        writer.println(command.trim());
    }

    // join to team
    // phrase of card interpretation
    // init 5x5 field

}
