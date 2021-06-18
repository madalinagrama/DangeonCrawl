package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;

public class Soldier extends Actor{

    private GameMap map;
    public Soldier(Cell cell, GameMap map) {
        super(cell);
        this.map = map;
        this.damage = 1;
        this.health = 17;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getTileName() {
        return "soldier";
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if (nextCell.getActor() == null
                && nextCell.getType() != CellType.WALL
                && nextCell.getType() != CellType.BOSSDOOR
                && nextCell.getType() != CellType.PORTAL) {
            super.move(dx, dy);
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

    public boolean isQualified() {
        return this.health <= 0;
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
