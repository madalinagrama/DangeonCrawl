package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

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
            if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() != CellType.FLOOR) {
                Inventory inventory = map.getPlayer().getInventory();
                Item item = map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getItem();
                inventory.addItem(inventory.getInventory(),item, item.getTileName(),1);
                System.out.println(inventory.getInventory());
                if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.SWORD) {
                    map.getPlayer().setDamage();
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.KEY) {
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.POTION) {
                    map.getPlayer().setHealthUp(5);
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).setItem(null);
                }

            }


        });


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
//        PerspectiveCamera camera = new PerspectiveCamera(true);
//        camera.setTranslateX(300);
//        camera.setTranslateY(0);
//        camera.setTranslateZ(-1500);
//        camera.setNearClip(0.1);
//        camera.setFarClip(2000);
//        camera.setFieldOfView(35);
//        scene.setCamera(camera);
        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED,this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();

    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                if (map.getGhosts() != null && map.getSoldiers() != null) {
                    map.getGhosts().forEach(g -> {
                        if (g.getX() > map.getPlayer().getX()) {
                            g.move(-1, 0);
                        }
                        if (g.getX() < map.getPlayer().getX()) {
                            g.move(1, 0);
                        }
                        if (g.getY() > map.getPlayer().getY()) {
                            g.move(0, -1);
                        }
                        if (g.getY() < map.getPlayer().getY()) {
                            g.move(0, 1);
                        }
                        if (g.getHealth() <= 0) {
                            map.getGhosts().remove(g);
                        }
                    });
                    map.getSoldiers().forEach(s -> {
                        if (s.getX() > map.getPlayer().getX()) {
                            s.move(-1, 0);
                        }
                        if (s.getX() < map.getPlayer().getX()) {
                            s.move(1, 0);
                        }
                        if (s.getY() > map.getPlayer().getY()) {
                            s.move(0, -1);
                        }
                        if (s.getY() < map.getPlayer().getY()) {
                            s.move(0, 1);
                        }
                        if (s.getHealth() <= 0) {
                            map.getSoldiers().remove(s);
                        }
                    });
                }
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                map.getGhosts().forEach(g -> {
                    if (g.getX() > map.getPlayer().getX()) {
                        g.move(-1, 0);
                    }
                    if (g.getX() < map.getPlayer().getX()) {
                        g.move(1, 0);
                    }
                    if (g.getY() > map.getPlayer().getY()) {
                        g.move(0, -1);
                    }
                    if (g.getY() < map.getPlayer().getY()) {
                        g.move(0, 1);
                    }
                    if (g.getHealth() <= 0) {
                        map.getGhosts().remove(g);
                    }
                });
                map.getSoldiers().forEach(s -> {
                    if (s.getX() > map.getPlayer().getX()) {
                        s.move(-1, 0);
                    }
                    if (s.getX() < map.getPlayer().getX()) {
                        s.move(1, 0);
                    }
                    if (s.getY() > map.getPlayer().getY()) {
                        s.move(0, -1);
                    }
                    if (s.getY() < map.getPlayer().getY()) {
                        s.move(0, 1);
                    }
                    if (s.getHealth() <= 0) {
                        map.getSoldiers().remove(s);
                    }
                });
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                map.getGhosts().forEach(g -> {
                    if (g.getX() > map.getPlayer().getX()) {
                        g.move(-1, 0);
                    }
                    if (g.getX() < map.getPlayer().getX()) {
                        g.move(1, 0);
                    }
                    if (g.getY() > map.getPlayer().getY()) {
                        g.move(0, -1);
                    }
                    if (g.getY() < map.getPlayer().getY()) {
                        g.move(0, 1);
                    }
                    if (g.getHealth() <= 0) {
                        map.getGhosts().remove(g);
                    }
                });
                map.getSoldiers().forEach(s -> {
                    if (s.getX() > map.getPlayer().getX()) {
                        s.move(-1, 0);
                    }
                    if (s.getX() < map.getPlayer().getX()) {
                        s.move(1, 0);
                    }
                    if (s.getY() > map.getPlayer().getY()) {
                        s.move(0, -1);
                    }
                    if (s.getY() < map.getPlayer().getY()) {
                        s.move(0, 1);
                    }
                    if (s.getHealth() <= 0) {
                        map.getSoldiers().remove(s);
                    }
                });
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                map.getGhosts().forEach(g -> {
                    if (g.getX() > map.getPlayer().getX()) {
                        g.move(-1, 0);
                    }
                    if (g.getX() < map.getPlayer().getX()) {
                        g.move(1, 0);
                    }
                    if (g.getY() > map.getPlayer().getY()) {
                        g.move(0, -1);
                    }
                    if (g.getY() < map.getPlayer().getY()) {
                        g.move(0, 1);
                    }
                    if (g.getHealth() <= 0) {
                        map.getGhosts().remove(g);
                    }
                });
                map.getSoldiers().forEach(s -> {
                    if (s.getX() > map.getPlayer().getX()) {
                        s.move(-1, 0);
                    }
                    if (s.getX() < map.getPlayer().getX()) {
                        s.move(1, 0);
                    }
                    if (s.getY() > map.getPlayer().getY()) {
                        s.move(0, -1);
                    }
                    if (s.getY() < map.getPlayer().getY()) {
                        s.move(0, 1);
                    }
                    if (s.getHealth() <= 0) {
                        map.getSoldiers().remove(s);
                    }
                });
                refresh();
                break;
        }

        if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.OPENDOOR) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap2("/map2.txt", player);
            Cell cell = new Cell(map,21,0,CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
            canvas = new Canvas(
                    200 * Tiles.TILE_WIDTH,
                    200 * Tiles.TILE_WIDTH);
        } else if (map.getCell(map.getPlayer().getX(),map.getPlayer().getY()).getType() == CellType.BACKDOOR) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap("/map.txt");
            Cell cell = new Cell(map,20,19,CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
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
