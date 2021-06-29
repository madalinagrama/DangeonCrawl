package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.PlayerDao;
import com.codecool.dungeoncrawl.dao.PlayerDaoJdbc;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.modals.*;
import com.codecool.dungeoncrawl.logic.actors.Soldier;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.items.ItemWithEffect;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.function.Predicate;

import java.sql.SQLException;
//DE FACUT SETAT NUME PLAYER LA INCEPUT
public class Main extends Application {
    boolean saved = false;
    String name = SetName.display("Set Name", "Please set a name!");
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
    Button loadGame = new Button("Load Game");
    GameDatabaseManager dbManager;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        GridPane ui = new GridPane();
        ui.setPrefWidth(250);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label("Damage: "), 0, 5);
        ui.add(DamageLabel, 1, 5);
        ui.add(new Label("Armor: "), 0, 10);
        ui.add(armorLabel, 1, 10);
        ui.add(addItem, 0, 15);
        ui.add(inventory, 5, 15);
        ui.add(loadGame, 0, 25);

        invetoryWindow();
        addItemsToInventory();
        loadGame.setOnAction(e -> {
            LoadGame.display("Load Game", dbManager.getSavedStates(), this);
        });

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

        Cell cell = new Cell(map, 6, 15, CellType.FLOOR);
        Player player = new Player(cell);
        player.setName(name);
        player.setCell(cell);
        map.setPlayer(player);

        refresh();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }



    public void invetoryWindow() {
        inventory.setOnAction(e -> {
            AlertBox.display(map.getPlayer().getInventory(), "Inventory");
            addItem.requestFocus();
        });
    }

    public void addItemsToInventory() {
        addItem.setOnAction(e -> {
            addItem.requestFocus();
            if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() != CellType.FLOOR) {
                Inventory inventory = map.getPlayer().getInventory();
                Item item = map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getItem();
                inventory.addItem(inventory.getInventory(), item, item.getTileName(), 1);
                if (item != null) {
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setType(CellType.FLOOR);
                    map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).setItem(null);
                    if (item instanceof ItemWithEffect) {
                        ((ItemWithEffect) item).applyEffect(map.getPlayer());
                    }
                }
            }

        });
    }
    public void save(String name) {
        map.getPlayer().setName(name);
        dbManager.saveGame(map.getPlayer(),map, name, this);
    }

    public void loadGame (String option) {
        map = dbManager.loadMap(map, option); // de refacut
    }

    public void updateGame(String name) {
        dbManager.update(map.getPlayer(),map, name);
    }

    public void autoSave() {
        dbManager.saveGame(map.getPlayer(),map, map.getPlayer().getName(), this);
        dbManager.saveDiscoveredMaps(map);
    }

    public void restart() {
        map.setPlayer(null);
        map = MapLoader.loadMap("/map.txt");
        Cell cell = new Cell(map, 5, 14, CellType.FLOOR);
        Player player = new Player(cell);
        player.setName(name);
        player.setCell(cell);
        map.setPlayer(player);
    }
    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        KeyCombination saveCombinationWin = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY);
        KeyCombination saveCombinationMac = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);

        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }

        if (saveCombinationWin.match(keyEvent) || saveCombinationMac.match(keyEvent)) {
            SaveGame.display("Save Game", "Enter your name", this);
        }
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
                map.getPlayer().setNewDirection(0, -1);
                update();
                refresh();
                keyEvent.consume();
                break;

            case DOWN:
                map.getPlayer().setNewDirection(0, 1);
                update();
                refresh();
                keyEvent.consume();
                break;

            case LEFT:
                map.getPlayer().setNewDirection(-1, 0);
                update();
                refresh();
                keyEvent.consume();
                break;

            case RIGHT:
                map.getPlayer().setNewDirection(1, 0);
                update();
                refresh();
                keyEvent.consume();
                break;
        }

        if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.OPENDOOR) {
            autoSave();
            if (!saved) {
            Player player = map.getPlayer();
            map = MapLoader.loadMap2("/map2.txt", player);
            Cell cell = new Cell(map, 44, 29, CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
            canvas = new Canvas(
                    200 * Tiles.TILE_WIDTH,
                    200 * Tiles.TILE_WIDTH);
            saved = true;
            } else {
                Player player = map.getPlayer();
                map = MapLoader.loadSavedMap(dbManager.loadPreviousMap("map2", player.getId()));
                Cell cell = new Cell(map, 44, 29, CellType.FLOOR);
                player.setCell(cell);
                map.setPlayer(player);
                canvas = new Canvas(
                        200 * Tiles.TILE_WIDTH,
                        200 * Tiles.TILE_WIDTH);

            }
        } else if (map.getCell(map.getPlayer().getX(), map.getPlayer().getY()).getType() == CellType.BACKDOOR) {
            autoSave();
            Player player = map.getPlayer();
            map = MapLoader.loadSavedMap(dbManager.loadPreviousMap("map1", player.getId()));
            Cell cell = new Cell(map, 20, 19, CellType.FLOOR);
            player.setCell(cell);
            map.setPlayer(player);
        }

    }

    public void update() {
        map.getPlayer().makeMove();
        if (map.getGhosts() != null && map.getSoldiers() != null) {
            map.getGhosts().forEach(g -> g.makeMove());
            map.getSoldiers().forEach(s -> s.makeMove());
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

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }



}
