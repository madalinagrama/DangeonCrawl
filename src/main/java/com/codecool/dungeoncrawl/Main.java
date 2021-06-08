package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label DamageLabel = new Label();
    Label inventoryLabel = new Label();
    Button addItem = new Button("Add Item");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label("Damage: "), 0, 5);
        ui.add(DamageLabel, 1, 5);
        ui.add(addItem,0,10);
        ui.add(new Label("Inventory: "), 0, 15);
        ui.add(inventoryLabel,1,15);

        addItem.setOnAction(e -> {
            Inventory inventory = map.getPlayer().getInventory();
            Item item = map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getItem();
            System.out.println(item);
            inventory.addItem(inventory.getInventory(),item, item.getTileName(),1);
            System.out.println(inventory.getInventory());
            if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.SWORD) {
                map.getPlayer().setDamage();
                map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setType(CellType.FLOOR);
            }
            if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.KEY) {
                map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setType(CellType.FLOOR);
            }

        });


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED,this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();

    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                refresh();
                break;
        }

        if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.OPENDOOR) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap2("/map2.txt", player);

        }
    }



    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        DamageLabel.setText("" + map.getPlayer().getDamage());
        inventoryLabel.setText("" + map.getPlayer().getInventory().getInventory());
    }
}
