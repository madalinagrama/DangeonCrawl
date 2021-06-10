package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Soldier extends Actor{
    public Soldier(Cell cell) {
        super(cell);
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
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && nextCell.getType() != CellType.BOSSDOOR && nextCell.getType() != CellType.PORTAL) {
            System.out.println(nextCell.getTileName());
            super.move(dx, dy);
        }
    }

    public boolean isQualified() {
        return this.health < 0;
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
