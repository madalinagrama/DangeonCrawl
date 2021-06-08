package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

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
    public String getTileName() {
        return "boss";
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }
}
