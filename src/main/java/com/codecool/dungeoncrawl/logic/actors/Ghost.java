package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;

public class Ghost extends Actor{

    private GameMap map;

    public Ghost(Cell cell, GameMap map) {
        super(cell);
        this.map = map;
        this.damage = 2;
        this.health = 11;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getTileName() {
        return "ghost";
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if (nextCell.getActor() == null) {
            super.move(dx,dy);
        }
    }

    @Override
    public void makeMove() {
        if (this.getX() > map.getPlayer().getX()) {
            this.move(-1, 0);
        }
        if (this.getX() < map.getPlayer().getX()) {
            this.move(1, 0);
        }
        if (this.getY() > map.getPlayer().getY()) {
            this.move(0, -1);
        }
        if (this.getY() < map.getPlayer().getY()) {
            this.move(0, 1);
        }
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }

    public boolean isQualified() {
        return this.health <= 0;
    }

}
