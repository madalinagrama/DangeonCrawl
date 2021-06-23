package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;


public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private Dotenv dotenv;
    private MapDao mapDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        mapDao = new MapDaoJdbc(dataSource,gameStateDao);
    }

    public void saveGame(Player player, GameMap map) {
        Gson gson = new Gson();
        String json = gson.toJson(player.getInventory());

//        Inventory car = gson.fromJson(json, Inventory.class);
//        System.out.println(json);

        java.sql.Date sqlDate = java.sql.Date.valueOf( LocalDate.now() );
        PlayerModel model = new PlayerModel(player);
        GameState gameState = new GameState(sqlDate, model);
        MapModel mapModel = new MapModel(map);
        String mapString = map.getMapString();

        model.setMap_id(player.getMap_id());
        mapDao.add(mapModel, mapString);
        playerDao.add(model);
        gameStateDao.add(gameState);
        PlayerModel pl = playerDao.get(1);

//        System.out.println(pl.getInventory().getInventory());
//        pl.getInventory().addItem(pl.getInventory().getInventory(),new Sword(map.getCell(10,12)),"sword",1);
//        System.out.println(pl.getId());
        System.out.println(pl.getInventory().getInventory());
    }


    private DataSource connect() throws SQLException {
        dotenv = Dotenv.load();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        String dbName = dotenv.get("PSQL_DB_NAME");
        String user = dotenv.get("PSQL_USER_NAME");
        String password = dotenv.get("PSQL_PASSWORD");



        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
