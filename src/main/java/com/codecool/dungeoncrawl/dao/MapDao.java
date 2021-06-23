package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.MapModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface MapDao {
    void add(MapModel map, String mapString);
    void update(MapModel map);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
}
