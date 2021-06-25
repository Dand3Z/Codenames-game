package pl.dele.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.Commands;
import pl.dele.GameEngine;
import pl.dele.cards.Card;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private static Logger log = LoggerFactory.getLogger(ClientHandler.class);

    private final List<ClientHandler> clients;
    private final Socket socket;
    private final GameEngine gameEngine;
    private BufferedReader reader;
    private PrintWriter writer;

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
                    if (msg.equalsIgnoreCase("join")) {
                        // join team
                    }
                    else if (msg.equalsIgnoreCase("card")){
                        // card analysis
                    }
                    else if (msg.equalsIgnoreCase("init")){
                        // initial 5x5
                        initialCards();
                    }
                    else{
                        // other commands ...
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

    private void initialCards(){
        StringBuilder sb = new StringBuilder();
        sb.append(Commands.INITIAL).append(System.lineSeparator());
        for(Card card: gameEngine.getPack().getCards()){
            sb.append(card.getPhrase()).append(System.lineSeparator());
        }

        sendCommand(sb.toString());
    }

    private void sendCommand(String command){
        for(ClientHandler client: clients){
            log.debug(client.getName());
            client.writer.println(command);
        }
    }

    // join to team
    // phrase of card interpretation
    // init 5x5 field

}
