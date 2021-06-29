package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;
    private PlayerDao playerDao;
    java.sql.Date sqlDate = java.sql.Date.valueOf( LocalDate.now() );
    Gson gson = new Gson();

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
    }

    @Override
    public void add(GameState state) {

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (saved_at, player_id, discovered_maps) VALUES (?, ?,?::json)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setSavedAt(sqlDate);
            statement.setDate(1, state.getSavedAt());
            statement.setInt(2, state.getPlayer().getId());
            statement.setString(3,gson.toJson(state.getDiscoveredMaps()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void addDiscoveredMaps(String stringMap, Player player,String mapName, GameState gameState) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET discovered_maps = ?::json WHERE player_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,gson.toJson(gameState.getDiscoveredMaps()));
            statement.setInt(2,player.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(GameState state) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET saved_at = ?, player_id = ?, discovered_maps = ?::json WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            state.setSavedAt(sqlDate);
            ps.setDate(1,state.getSavedAt());
            ps.setInt(2, state.getPlayer().getId());
            ps.setString(3,gson.toJson(state.getDiscoveredMaps()));
            System.out.println(state.getId());
            ps.setInt(4, state.getId());

            ps.executeUpdate();

        }
        catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public GameState get(int id) {
        try(Connection conn = dataSource.getConnection()) {
            String sqlPlayerDao = "SELECT * FROM player WHERE id = ?";

            String sql = "SELECT * FROM game_state WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            PreparedStatement playerStatement = conn.prepareStatement(sqlPlayerDao);
            if(!rs.next()) {
                return null;
            }

            playerStatement.setInt(1, rs.getInt(3));

            ResultSet playerResult = playerStatement.executeQuery();
            playerResult.next();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(playerResult.getString("inventory"));

            GameState gameState =  new GameState(rs.getDate(2),new PlayerModel(playerResult.getInt(1),playerResult.getString(2),playerResult.getInt(3),playerResult.getInt(4),playerResult.getInt(5),playerResult.getInt(6),playerResult.getInt(7),playerResult.getInt(8), gson.fromJson(String.valueOf(json), Inventory.class)));
            gameState.setId(rs.getInt(1));
            return gameState;
        }
        catch (SQLException | ParseException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public String getPreviousMap(int id, String mapName) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT discovered_maps FROM game_state WHERE player_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(rs.getString("discovered_maps"));
            HashMap<String, String> dsmaps = gson.fromJson(String.valueOf(json), HashMap.class);


            return dsmaps.get(mapName);
        }
        catch (SQLException | ParseException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public List<String> getAll() {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM game_state";

            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<String> gameStates = new ArrayList<>();

            while(rs.next()) { ;
                gameStates.add(String.format("%s, %s",playerDao.get(rs.getInt(3)).getPlayerName(),rs.getString(2)));
            }
            return gameStates;

        }
        catch (SQLException  throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }
}
