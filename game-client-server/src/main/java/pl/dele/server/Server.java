package pl.dele.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.GameEngine;
import pl.dele.teams.Team;
import pl.dele.teams.TeamColor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final String CARD_PATH = "game-model/src/main/resources/cards.csv";
    private static List<ClientHandler> clients = new ArrayList<>();
    private static Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        Team redTeam = new Team(TeamColor.RED_TEAM);
        Team blueTeam = new Team(TeamColor.BLUE_TEAM);

        GameEngine gameEngine = new GameEngine(redTeam, blueTeam, CARD_PATH);

        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(8888);
            log.info("Server is starting on port 8888");
            while (true){
                socket = serverSocket.accept();
                log.info("Connected: " + socket.toString());
                ClientHandler clientThread = new ClientHandler(socket, clients, gameEngine);
                clients.add(clientThread);
                clientThread.start();
            }
        } catch (IOException e){
            e.getMessage();
        }
    }
}
