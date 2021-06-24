package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Soldier;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.model.ActorModel;
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
import java.util.List;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;


public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private Dotenv dotenv;
    private MapDao mapDao;
    private ActorDao actorDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
        mapDao = new MapDaoJdbc(dataSource,gameStateDao);
        actorDao = new ActorDaoJdbc(dataSource);
    }

    public void saveGame(Player player, GameMap map) {

        List<PlayerModel> players = playerDao.getAll();

//        for (PlayerModel playerModel : players) {
//            if (playerModel.getPlayerName().equals(player.getName())){
//                //modal yes/no
//                {
//                    //if yes-overwrite
//                } // else
//                {
////                    dont
//                }
//
//            }
//
//        }

        Gson gson = new Gson();
        String json = gson.toJson(player.getInventory());

//        Inventory car = gson.fromJson(json, Inventory.class);
//        System.out.println(json);

        java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
        PlayerModel model = new PlayerModel(player);
        GameState gameState = new GameState(sqlDate, model);
        MapModel mapModel = new MapModel(map);
        String mapString = map.getMapString();

        model.setMap_id(player.getMap_id());
        mapDao.add(mapModel, mapString);
        playerDao.add(model);
        gameStateDao.add(gameState);

        for (Soldier soldier : map.getSoldiers()) {
            ActorModel actor = new ActorModel(soldier);
            actor.setMap_id(1);// de reviziut cu id-ul hartii
            actorDao.add(actor);
        }
        for (Ghost ghost : map.getGhosts()) {
            ActorModel actor = new ActorModel(ghost);
            actor.setMap_id(1);// de reviziut cu id-ul hartii
            actorDao.add(actor);
        }
//        if(map.getName().equals("map2")) {
//            ActorModel actor = new ActorModel(map.getBoss());
//            actor.setMap_id(1);// de reviziut cu id-ul hartii
//            actorDao.add(actor);
//        }


//        PlayerModel pl = playerDao.get(1);

//        System.out.println(pl.getInventory().getInventory());
//        pl.getInventory().addItem(pl.getInventory().getInventory(),new Sword(map.getCell(10,12)),"sword",1);
//        System.out.println(pl.getId());
//        System.out.println(pl.getInventory().getInventory());

//        System.out.println(pl.getInventory().getInventory());
//
//        map.setPlayer(null);
//        map = MapLoader.loadSavedMap(mapDao.get(2));
//        PlayerModel pl = playerDao.get(2);
//        Player newPlayer = new Player(map.getCell(pl.getX(),pl.getY()), pl);
//        map.setPlayer(newPlayer);

    }

    public GameMap  loadMap(GameMap map) {
        map.setPlayer(null);
        map = MapLoader.loadSavedMap(mapDao.get(2));
        PlayerModel pl = playerDao.get(2);
        Player newPlayer = new Player(map.getCell(pl.getX(),pl.getY()), pl);
        map.setPlayer(newPlayer);
        return map;
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
