package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;

import io.github.cdimascio.dotenv.Dotenv;


public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private Dotenv dotenv;
    private MapDao mapDao;
    private ItemDao itemDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        mapDao = new MapDaoJdbc(dataSource,gameStateDao);
        itemDao = new ItemDaoJdbc(dataSource);
    }

    public void saveGame(Player player, GameMap map) {
//        Gson gson = new Gson();
//        String json = gson.toJson(map);
//
//        GameMap car = gson.fromJson(json, GameMap.class);
//        System.out.println(car.getPlayer().getName());

        java.sql.Date sqlDate = java.sql.Date.valueOf( LocalDate.now() );
        PlayerModel model = new PlayerModel(player);
        GameState gameState = new GameState(sqlDate, model);
        MapModel mapModel = new MapModel(map);
        String mapString = map.getMapString();
        ItemModel item = new ItemModel();

        model.setMap_id(player.getMap_id());
        mapDao.add(mapModel, mapString);
        playerDao.add(model);
        gameStateDao.add(gameState);

        itemDao.add(item);
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
