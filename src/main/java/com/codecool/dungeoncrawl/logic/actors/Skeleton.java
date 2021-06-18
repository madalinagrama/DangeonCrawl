package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super(cell);
        this.damage = 1;
    }

    @Override
    public void makeMove() {

    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }

    public int getDamage() {
        return this.damage;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
