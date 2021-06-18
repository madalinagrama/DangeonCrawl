package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Sword extends ItemWithEffect{
    public Sword(Cell cell) {
        super(cell, "sword");
    }

    @Override
    public String getTileName() {
        return "sword";
    }

    @Override
    public void applyEffect(Player player) {
        player.setDamage();
    }
}
