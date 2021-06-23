package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapDaoJdbc implements MapDao{
    private DataSource dataSource;
    private GameStateDao gameStateDao;

    public MapDaoJdbc(DataSource dataSource, GameStateDao gameState) {
        this.dataSource = dataSource;
        this.gameStateDao = gameState;
    }
    @Override
    public void add(MapModel map, String mapString) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO maps (name, map, game_state_id) VALUES (?, ?, ?)";

//            String getData = "SELECT id FROM game_state WHERE player_id = ?";
//            PreparedStatement ps  = conn.prepareStatement(getData, Statement.RETURN_GENERATED_KEYS);
//            ps.setInt(1,map.getMap().getPlayer().getId());
//
//            ResultSet rs = ps.getGeneratedKeys();
//            rs.next();
//
//            int game_state_id = rs.getInt(1);
//            System.out.println(game_state_id);

            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, "map1");
            statement.setString(2, mapString);
            statement.setInt(3, 1);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            map.setId(resultSet.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(MapModel map) {

    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }
}