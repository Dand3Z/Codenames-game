package pl.dele.testClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dele.Commands;
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

    public static void main(String[] args) {
        new testClient();
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
        writer.println("init");
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
                log.debug("test");
                String command;
                while ((command = reader.readLine()) != null);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) { sb.append(line).append(System.lineSeparator()); }
                log.debug("test2");
                switch (command){
                    case Commands.INITIAL:
                        log.debug(Commands.INITIAL);
                        initialHandling(sb.toString());
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
                // send init - temp
                //writer.println("init");
                //refreshGui();
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
