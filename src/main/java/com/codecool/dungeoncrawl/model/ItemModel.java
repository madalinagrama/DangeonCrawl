package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

public class ItemModel extends BaseModel {
    private String itemType;
    private int x;
    private int y;
    private int map_id;

    public ItemModel(int id, String itemType, int x, int y, int map_id) {
        this.itemType = itemType;
        this.x = x;
        this.y = y;
        this.map_id = map_id;
        this.id = id;
    }

    public String getItemType() {
        return itemType;
    }

    public int getX() {
        return x;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }
}
