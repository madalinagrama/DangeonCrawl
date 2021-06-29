package com.codecool.dungeoncrawl.logic.modals;
import com.codecool.dungeoncrawl.logic.Inventory;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.StringJoiner;


public class AlertBox {

    public static void display(Inventory item, String title) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(250);


        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(closeButton);
        item.getInventory().entrySet().forEach(entry -> {
            Label label = new Label();

            label.setText(entry.getKey() + ": " + entry.getValue());
            layout.getChildren().add(label);

        });

        closeButton.setTranslateX(100);
        closeButton.setTranslateY(150);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
