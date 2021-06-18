package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Boss extends Actor{


    public Boss(Cell cell) {
        super(cell);
        this.damage = 5;
        this.health = 35;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && nextCell.getType() != CellType.BOSSDOOR && nextCell.getType() != CellType.PORTAL) {
            super.move(dx, dy);
        }
    }

    @Override
    public void makeMove() {

    }

    @Override
    public String getTileName() {
        return "boss";
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
