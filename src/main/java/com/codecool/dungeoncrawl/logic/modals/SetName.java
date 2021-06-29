package com.codecool.dungeoncrawl.logic.modals;

import com.codecool.dungeoncrawl.Main;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SetName {
    public static String display(String title, String message){
        Stage window = new Stage();
        TextField nameField = new TextField();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        Label label = new Label(message);
        label.setTranslateX(90);
        label.setTranslateY(70);
        window.setMinWidth(250);
        window.setMinHeight(250);

//        Label label;

        Button startGame = new Button("Start Game");
        startGame.setOnAction( e -> {
            window.close();
        });

        VBox layout = new VBox(label, startGame, nameField);
        startGame.setTranslateX(75);
        startGame.setTranslateY(150);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return nameField.getText();
    }

    public static String getData(TextField nameField) {
        return nameField.getText();
    }

}
