package com.codecool.dungeoncrawl.logic.modals;

import com.codecool.dungeoncrawl.Main;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;

public class LoadGame {

    private static ListView<String> listView;

    public static void display(String title, String message, Main main) {
        Stage window = new Stage();
        listView = new ListView<>();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        listView.getItems().addAll(message.split("\n"));
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setMaxSize(350,250);
        listView.setTranslateX(22);
        window.setMinWidth(400);
        window.setMinHeight(400);

//        Label label;

        Button loadGame = new Button("Load Game");
        loadGame.setOnAction( e -> {
            String option = String.valueOf(Arrays.stream(listView.getSelectionModel().getSelectedItem().split("^")).findFirst().get().charAt(0));
            System.out.println(option);
            main.loadGame(option);
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(listView,loadGame);
        loadGame.setTranslateX(165);
        loadGame.setTranslateY(390);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static String getData(TextField nameField) {
        return nameField.getText();
    }
}
