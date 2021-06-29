package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MapDaoJdbc implements MapDao{
    private DataSource dataSource;
    private GameStateDao gameStateDao;
    private PlayerDao playerDao;


    public MapDaoJdbc(DataSource dataSource, GameStateDao gameState, PlayerDao playerDao) {
        this.dataSource = dataSource;
        this.gameStateDao = gameState;
        this.playerDao = playerDao;
    }
    @Override
    public void add(MapModel map, String mapString, PlayerModel playerModel, PlayerDao playerDao) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO maps (name, map, game_state_id) VALUES (?, ?, ?)";

            String getData = "SELECT id FROM game_state WHERE player_id = ?";
            PreparedStatement ps  = conn.prepareStatement(getData);
            ps.setInt(1,playerModel.getId());

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return;
            }

            int game_state_id = rs.getInt(1);

            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "map1");
            statement.setString(2, mapString);
            statement.setInt(3, game_state_id);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            map.setId(resultSet.getInt(1));
            playerModel.setMap_id(rs.getInt(1));
            playerDao.update(playerModel);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(MapModel map) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE maps SET name = ?, map = ?, game_state_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,"map1");
            ps.setString(2, map.getMap().getMapString());
            ps.setInt(3, map.getGameStateId());
            ps.setInt(4, map.getId());

            ps.executeUpdate();

        }
        catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(int id) {

        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT maps.map FROM maps WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }


            return rs.getString(1);

        }
        catch (SQLException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }
}
