package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.AlertBox;
import com.codecool.dungeoncrawl.logic.ReplayGame;
import com.codecool.dungeoncrawl.logic.actors.Soldier;
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

import java.util.function.Predicate;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label DamageLabel = new Label();
    Label inventoryLabel = new Label();
    Label armorLabel = new Label();
    Button addItem = new Button("Add Item");
    Button inventory = new Button("Inventory");

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
        ui.add(new Label("Armor: "), 0, 10);
        ui.add(armorLabel, 1, 10);
        ui.add(addItem, 0, 15);
        ui.add(inventory, 5, 15);


        inventory.setOnAction(e -> {
            AlertBox.display(map.getPlayer().getInventory(), "Inventory");
        });

        addItem.setOnAction(e -> {
            if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() != CellType.FLOOR) {
                Inventory inventory = map.getPlayer().getInventory();
                Item item = map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getItem();
                inventory.addItem(inventory.getInventory(), item, item.getTileName(), 1);
                System.out.println(inventory.getInventory());
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.SWORD) {
                    map.getPlayer().setDamage();
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.KEY) {
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.POTION) {
                    map.getPlayer().setHealthUp(5);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.HAMMER) {
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                }
                if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.ARMOR) {
                    map.getPlayer().setArmor(2);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                }

            }
        });


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

        Cell cell = new Cell(map, 6, 15, CellType.FLOOR);
        Player player = new Player(cell);
        player.setCell(cell);
        map.setPlayer(player);

        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();


    }

    public void restart() {
        map.setPlayer(null);
        map = MapLoader.loadMap("/map.txt");
        Cell cell = new Cell(map, 5, 14, CellType.FLOOR);
        Player player = new Player(cell);
        player.setCell(cell);
        map.setPlayer(player);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (map.getPlayer().getHealth() <= 0) {
            ReplayGame.display("Restart", "You Died", this);
        }
        if (map.getPlayer().getCell().getType() == CellType.WINDOOR) {
            ReplayGame.display("Restart", "You won!", this);
        }

        Predicate<Ghost> isQualified = Ghost::isQualified;
        map.getGhosts().stream()
                .filter(isQualified);
        map.getGhosts().removeIf(isQualified);

        Predicate<Soldier> isQualified1 = Soldier::isQualified;
        map.getSoldiers().stream()
                .filter(isQualified1);
        map.getSoldiers().removeIf(isQualified1);

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
                    });
                }
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
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
                    });
                }
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
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

                    });
                }
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1, 0);
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

                    });
                }
                refresh();
                break;
        }

        if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.OPENDOOR) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap2("/map2.txt", player);
            Cell cell = new Cell(map, 44, 29, CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
            canvas = new Canvas(
                    200 * Tiles.TILE_WIDTH,
                    200 * Tiles.TILE_WIDTH);
        } else if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.BACKDOOR) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap("/map.txt");
            Cell cell = new Cell(map, 20, 19, CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
        }


    }


    private void refresh() {
        context.setFill(Color.BLUE);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Player player = map.getPlayer();
        int deltaView = 12;
        int befX = deltaView;
        int xAfter = deltaView;


        if (map.getPlayer().getX() < deltaView) {
            befX = map.getPlayer().getX();
            xAfter = deltaView + (deltaView - befX);
        } else if (map.getWidth() - 1 - map.getPlayer().getX() < deltaView) {
            xAfter = map.getWidth() - 1 - map.getPlayer().getX();
            befX = deltaView + (deltaView - xAfter);
        }
        deltaView = 10;
        int befY = deltaView;
        int yAfter = deltaView;
        if (map.getPlayer().getY() < deltaView) {
            befY = map.getPlayer().getY();
            yAfter = deltaView + (deltaView - befY);
        } else if (map.getHeight() - 1 - map.getPlayer().getY() < deltaView) {
            yAfter = map.getHeight() - 1 - map.getPlayer().getY();
            befY = deltaView + (deltaView - yAfter);
        }


        int minX = map.getPlayer().getX() - befX;
        int minY = map.getPlayer().getY() - befY;
        int maxX = map.getPlayer().getX() + xAfter;
        int maxY = map.getPlayer().getY() + yAfter;


        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x - minX, y - minY);
                } else {
                    Tiles.drawTile(context, cell, x - minX, y - minY);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        DamageLabel.setText("" + map.getPlayer().getDamage());
        armorLabel.setText("" + map.getPlayer().getArmor());
        inventoryLabel.setText("" + map.getPlayer().getInventory().getInventory());
    }
}
