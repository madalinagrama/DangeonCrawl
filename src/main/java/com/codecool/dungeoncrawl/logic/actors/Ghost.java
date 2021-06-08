package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Ghost extends Actor{

    public Ghost(Cell cell) {
        super(cell);
        this.damage = 3;
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
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
