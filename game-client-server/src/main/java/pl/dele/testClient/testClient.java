package pl.dele.testClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.ServerResponse;
import pl.dele.cards.Card;
import pl.dele.client.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class testClient extends Thread {

    public static void main(String[] args) throws InterruptedException {
        var x = new testClient();
    }

    private static Logger log = LoggerFactory.getLogger(Client.class);

    private Socket socket;
    private final List<Card> cards;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean selectedTeam;

    public testClient(){
        cards = new ArrayList<>();
        connectSocket();
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 8888);
            log.info("Client is connecting to pl.dele.server");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            start();

            writer.print("init");
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
                while ((line = reader.readLine()).trim() != null) {
                    sb.append(line).append(System.lineSeparator());
                    if (line.trim().equalsIgnoreCase("END")) break;
                }
                log.debug("test2");
                switch (command){
                    case ServerResponse.INITIAL:
                        log.debug(ServerResponse.INITIAL);
                        initialHandling(sb.toString());
                        break;
                    case ServerResponse.JOIN_TEAM:
                        log.debug(ServerResponse.JOIN_TEAM);
                        joinHandling(sb.toString());
                        break;
                    case ServerResponse.UNCOVER_CARD:
                        log.debug(ServerResponse.UNCOVER_CARD);
                        interpretationHandling(sb.toString());
                        break;
                }

                command = null;

            }
        }
        catch (IOException e) { e.getMessage(); }}

        // == command handling ==
        private void initialHandling(String instruction) {
            String[] phrases = instruction.split(System.lineSeparator());
            for(String phrase: phrases){
                cards.add(new Card(phrase));
            }
            // we got list of cards, now display it on board
        }

        private void interpretationHandling(String s) {

        }

        private void joinHandling(String instruction) {

        }
}
