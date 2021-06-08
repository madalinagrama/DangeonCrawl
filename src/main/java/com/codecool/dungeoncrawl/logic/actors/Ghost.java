package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Ghost extends Actor{

    public Ghost(Cell cell) {
        super(cell);
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
            System.out.println(nextCell.getTileName());
            super.move(dx,dy);
        }


    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
