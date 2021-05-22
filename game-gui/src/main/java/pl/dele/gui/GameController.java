package pl.dele.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class GameController {

    @FXML
    private GridPane gripMap;

    public GameController() {

    }

    @FXML
    void initialize(){
        refreshGut();
    }

    private void refreshGut(){
        for(int x = 0; x < 5; ++x){
            for(int y = 0; y < 5; ++y){
                CardTile cardTile = new CardTile();
                gripMap.add(cardTile,x,y);
            }
        }
    }

}
