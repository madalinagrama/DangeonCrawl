package com.codecool.dungeoncrawl.logic.modals;

import com.codecool.dungeoncrawl.App;
import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Inventory;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.StringJoiner;


public class SaveGame {

    public static void display(String title, String message, Main main) {
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

        Button saveGame = new Button("Save Game");
        saveGame.setOnAction( e -> {
            String name = getData(nameField);
            main.save(name);
            window.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(label, saveGame, nameField, cancel);
        saveGame.setTranslateX(75);
        saveGame.setTranslateY(150);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static String getData(TextField nameField) {
        return nameField.getText();
    }
}
