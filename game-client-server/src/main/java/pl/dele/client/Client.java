package pl.dele.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application{

    public Client() {}

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/codenames-gui.fxml")));

        stage.setTitle("Codenames-game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
