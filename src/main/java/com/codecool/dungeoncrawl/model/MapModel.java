package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.GameMap;

public class MapModel extends BaseModel{
    private GameMap map;
    protected String name;
    protected int gameStateId;

    public MapModel(GameMap map) {
        this.map = map;
        this.name = map.getName();
    }

    public GameMap getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGameStateId() {
        return gameStateId;
    }

    public void setGameStateId(int gameStateId) {
        this.gameStateId = gameStateId;
    }
}
