package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface GameStateDao {
    void add(GameState state);
    void update(GameState state);
    GameState get(int id);
    List<String> getAll();
    void addDiscoveredMaps(String mapString, Player player, String mapName, GameState gameState);
    String getPreviousMap(int id, String mapName);
}
