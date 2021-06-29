package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int hp;
    private int damage;
    private int armor;
    private int x;
    private int y;
    private int map_id;
    private Inventory inventory;

    public PlayerModel(int id, String playerName, int hp, int damage, int armor, int x, int y,int map_id, Inventory inventory) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.damage = damage;
        this.armor = armor;
        this.id = id;
        this.map_id = map_id;
        this.inventory = inventory;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getName();
        this.x = player.getX();
        this.y = player.getY();

        this.hp = player.getHealth();
        this.damage = player.getDamage();
        this.armor = player.getArmor();
        this.id = player.getId();
        this.inventory = player.getInventory();

    }

    public String getPlayerName() {
        return playerName;
    }


    public int getHp() {
        return hp;
    }


    public int getX() {
        return x;
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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
