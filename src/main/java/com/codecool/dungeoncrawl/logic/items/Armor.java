package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class Armor extends ItemWithEffect{
    public Armor(Cell cell) {
        super(cell, "armor");
    }

    @Override
    public String getTileName() {
        return "armor";
    }

    @Override
    public void applyEffect(Player player) {
        player.setArmor(2);
    }
}
