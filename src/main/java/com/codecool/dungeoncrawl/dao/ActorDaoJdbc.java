package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ActorModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ActorDaoJdbc implements ActorDao {
    private DataSource dataSource;

    public  ActorDaoJdbc(DataSource dataSource) {
        this.dataSource =dataSource;
    }
    @Override
    public void add(ActorModel actor) {
        try (Connection conn = dataSource.getConnection()) {
        String sql = "INSERT INTO actors (actor_type, hp, x, y, map_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, actor.getActorType());
        statement.setInt(2, actor.getHp());
        statement.setInt(3, actor.getX());
        statement.setInt(4, actor.getY());
        statement.setInt(5, actor.getMap_id());
        statement.executeUpdate();
        ResultSet resultSet = statement.getGeneratedKeys();
        resultSet.next();
            actor.setMap_id(resultSet.getInt(5));
    } catch (
    SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public void update(ActorModel actor) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE actors SET actor_type= ? , map_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,actor.getActorType());
            ps.setInt(2, actor.getMap_id());
            ps.setInt(3, actor.getId());

            ps.executeUpdate();

        }
        catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public ActorModel get(int id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM actors WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            return new ActorModel(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7));

        }
        catch (SQLException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public List<ActorModel> getAll() {
        return null;
    }
}
