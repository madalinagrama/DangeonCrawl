package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Soldier;
import com.codecool.dungeoncrawl.logic.modals.ConfirmName;
import com.codecool.dungeoncrawl.model.ActorModel;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;


public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private Dotenv dotenv;
    private MapDao mapDao;
    private ActorDao actorDao;
    java.sql.Date sqlDate = java.sql.Date.valueOf(LocalDate.now());
    GameState gmState;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
        mapDao = new MapDaoJdbc(dataSource, gameStateDao, playerDao);
        actorDao = new ActorDaoJdbc(dataSource);
    }

    public void saveGame(Player player, GameMap map, String name, Main main) {
        List<PlayerModel> players = playerDao.getAll();

        for (PlayerModel playerModel : players) {
            if (playerModel.getPlayerName().equals(name)) {
                ConfirmName.display("Confirm Save", "Would you like to overwrite the already existing state?", main, name);
                if (ConfirmName.getData()) {
                    update(player, map, name);
                    return;
                } else {
                    return;
                }
            }
        }

        PlayerModel model = new PlayerModel(player);
        GameState gameState = new GameState(sqlDate, model);
        MapModel mapModel = new MapModel(map);


        model.setMap_id(player.getMap_id());
        playerDao.add(model);
        map.getPlayer().setId(model.getId());
        gameState.addDiscoveredMap(map.getMapString());
        gmState = saveGameState(gameState);
        gameStateDao.add(gameState);
        mapDao.add(mapModel, map.getMapString(), model, playerDao);

        for (Soldier soldier : map.getSoldiers()) {
            ActorModel actor = new ActorModel(soldier);
            actor.setMap_id(model.getMap_id());
            actorDao.add(actor);
        }
        for (Ghost ghost : map.getGhosts()) {
            ActorModel actor = new ActorModel(ghost);
            actor.setMap_id(model.getMap_id());
            actorDao.add(actor);
        }
    }

    public void update(Player player, GameMap map, String name) {
        PlayerModel playerUpdateModel = new PlayerModel(player);
        GameState gameStateUpdateModel = gmState;
        gameStateUpdateModel.addDiscoveredMap(map.getMapString());
        MapModel mapUpdateModel = new MapModel(map);
        playerUpdateModel.setId(playerDao.getByName(name).getId());
        gameStateUpdateModel.setId(playerDao.getByName(name).getId());
        mapUpdateModel.setId(playerDao.getByName(name).getId());
        playerDao.update(playerUpdateModel);
        gameStateDao.update(gameStateUpdateModel);
        mapDao.update(mapUpdateModel);

    }

    public GameState saveGameState(GameState gameState) {
        return gameState;
    }

    public GameMap loadMap(GameMap map, String option) {
            PlayerModel pl = playerDao.get(Integer.parseInt(option));
            map = MapLoader.loadSavedMap(mapDao.get(Integer.parseInt(option)));
            Cell cell = new Cell(map, pl.getX(), pl.getY(), CellType.FLOOR);
            Player newPlayer = new Player(cell, pl);
            map.setPlayer(newPlayer);
            return map;
    }
    public String loadPreviousMap(String mapName, int playerId) {

            return gameStateDao.getPreviousMap(playerId,mapName);
    }

    public String getSavedStates() {
        int i = 0;
        StringBuilder stringBuilder = new StringBuilder();
        List<String> gameStates = gameStateDao.getAll();

        for (String gameState : gameStates) {
            i++;
            stringBuilder.append("" + i + " " + gameState);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void saveDiscoveredMaps(GameMap map) {
        int mapLength = map.getMapString().length();
        if (mapLength == 546) {
            gameStateDao.addDiscoveredMaps(map.getMapString(),map.getPlayer(),"map1", gmState);
        } else {
            gameStateDao.addDiscoveredMaps(map.getMapString(),map.getPlayer(), "map2",gmState);
        }

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
