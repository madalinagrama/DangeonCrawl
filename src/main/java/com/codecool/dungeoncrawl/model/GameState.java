package com.codecool.dungeoncrawl.model;

import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;
    private HashMap<String, String> discoveredMaps = new HashMap<>();
    private PlayerModel player;

    public GameState(Date savedAt, PlayerModel player) {
        this.savedAt = savedAt;
        this.player = player;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }



    public HashMap<String, String> getDiscoveredMaps() {
        return discoveredMaps;
    }
    public void setDiscoveredMaps(HashMap<String,String> maps) {
        this.discoveredMaps = maps;
    }

    public void addDiscoveredMap(String map) {
        if (map.length() == 546) {
            discoveredMaps.put("map1",map);
        }else {
            discoveredMaps.put("map2",map);
        }
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }
}
