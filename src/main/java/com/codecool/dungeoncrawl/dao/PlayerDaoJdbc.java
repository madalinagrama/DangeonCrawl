package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, hp, damage, armor, x, y, map_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getDamage());
            statement.setInt(4, player.getArmor());
            statement.setInt(5, player.getX());
            statement.setInt(6, player.getY());
            statement.setInt(7, player.getMap_id());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
//            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET player_name= ? , map_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,player.getPlayerName());
            ps.setInt(2, player.getMap_id());
            ps.setInt(3, player.getId());

            ps.executeUpdate();

        }
        catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM player WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            return new PlayerModel(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7));

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
