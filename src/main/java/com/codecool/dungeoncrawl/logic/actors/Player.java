package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Sword;

public class Player extends Actor {
    private Inventory inventory;

    public Player(Cell cell) {
        super(cell);
        this.inventory = new Inventory();
        this.damage = 5;
   }

    public void setDamage() {
        this.damage += 5;
    }

    public int getDamage() {
        return damage;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getTileName() {
        return "player";
    }


    @Override
    public void move(int dx, int dy) {
        Cell nextCell = getCell().getNeighbor(dx, dy);
        if ((nextCell.getType() != CellType.WALL) && (nextCell.getType() != CellType.CLOSEDOOR) && (nextCell.getActor() == null)) {
            super.move(dx, dy);
        } else if (nextCell.getType() == CellType.CLOSEDOOR && inventory.getInventory().containsKey("key")) {
            System.out.println("open");
            nextCell.setType(CellType.OPENDOOR);
            super.move(dx, dy);
        } else if (nextCell.getActor() instanceof Skeleton) {
            this.setHealth(((Skeleton) nextCell.getActor()).getDamage());
            nextCell.getActor().setHealth(damage);
            if (nextCell.getActor().getHealth() <= 0) {
                nextCell.getActor().getCell().setType(CellType.FLOOR);
                nextCell.getActor().getCell().setActor(null);
            }
        } else if (nextCell.getActor() instanceof Ghost) {
            this.setHealth(((Ghost) nextCell.getActor()).getDamage());
            nextCell.getActor().setHealth(damage);
            if (nextCell.getActor().getHealth() <= 0) {
                nextCell.getActor().getCell().setType(CellType.FLOOR);
                nextCell.getActor().getCell().setActor(null);
            }
        }
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }

}
