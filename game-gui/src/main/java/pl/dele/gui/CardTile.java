package pl.dele.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class CardTile extends StackPane {

    private final Rectangle rectangle;

    public CardTile() {
        rectangle = new Rectangle(100,110, Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        getChildren().add(rectangle);
    }

    void setBackground(Color color){
        rectangle.setFill(color);
    }

    void addPhrase(String phrase){
        getChildren().add(new Label(phrase));
    }
}
