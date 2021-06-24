package com.codecool.dungeoncrawl.logic.modals;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class ConfirmName {
    static boolean result;

    public static void display(String title, String message, Main main, String name) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        Label label = new Label(message);
        label.setTranslateX(90);
        label.setTranslateY(70);
        window.setMinWidth(250);
        window.setMinHeight(250);

//        Label label;

        Button saveGame = new Button("Yes");
        saveGame.setOnAction( e -> {
            main.updateGame(name);
            result = true;
            window.close();

        });

        Button cancel = new Button("No");
        cancel.setOnAction(e -> {
            window.close();
            result = false;
            SaveGame.display("Save Game", "Enter your name", main);
        });

        VBox layout = new VBox(label, saveGame, cancel);
        saveGame.setTranslateX(75);
        saveGame.setTranslateY(150);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static boolean getData() {
        return result;
    }
}

