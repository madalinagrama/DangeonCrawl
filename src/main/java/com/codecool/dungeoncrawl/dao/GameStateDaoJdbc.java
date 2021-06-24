package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDate;

public class GameStateDaoJdbc implements GameStateDao {

    private DataSource dataSource;
    private PlayerDao playerDao;
    java.sql.Date sqlDate = java.sql.Date.valueOf( LocalDate.now() );

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
    }

    @Override
    public void add(GameState state) {

        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO game_state (saved_at, player_id) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            state.setSavedAt(sqlDate);
            statement.setDate(1, state.getSavedAt());
            statement.setInt(2, state.getPlayer().getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(GameState state) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE game_state SET saved_at = ?, player_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            state.setSavedAt(sqlDate);
            ps.setDate(1,state.getSavedAt());
            ps.setInt(2, state.getPlayer().getId());
            ps.setInt(3, state.getId());

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
            playerStatement.setInt(1, rs.getInt(3));

            if(!rs.next()) {
                return null;
            }

            ResultSet playerResult = playerStatement.executeQuery();
            playerResult.next();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(playerResult.getString("inventory"));


            Gson gson = new Gson();
            GameState gameState =  new GameState(rs.getDate(2),new PlayerModel(playerResult.getInt(1),playerResult.getString(2),playerResult.getInt(3),playerResult.getInt(4),playerResult.getInt(5),playerResult.getInt(6),playerResult.getInt(7),playerResult.getInt(8), gson.fromJson(String.valueOf(json), Inventory.class)));
            gameState.setId(rs.getInt(1));
            return gameState;
        }
        catch (SQLException | ParseException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
