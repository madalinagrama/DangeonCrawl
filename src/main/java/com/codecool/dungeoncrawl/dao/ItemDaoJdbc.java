package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class ItemDaoJdbc implements ItemDao {
    private DataSource dataSource;

    public ItemDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(ItemModel item) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO items (item_type, x, y, map_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getItemType());
            statement.setInt(2, item.getX());
            statement.setInt(3, item.getY());
            statement.setInt(4, item.getMap_id());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
//            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(ItemModel item) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE items SET item_type= ? , x = ?, y = ?, map_id = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, item.getItemType());
            ps.setInt(2, item.getX());
            ps.setInt(3, item.getY());
            ps.setInt(4, item.getMap_id());

            ps.executeUpdate();

        }
        catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public ItemModel get(int id) {
        try(Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM items WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();

            if(!rs.next()) {
                return null;
            }

            return new ItemModel(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4), rs.getInt(5));

        }
        catch (SQLException throwable) {
            throw new RuntimeException("Error while updating the Author.", throwable);
        }
    }

    @Override
    public List<ItemModel> getAll() {
        return null;
    }
}
