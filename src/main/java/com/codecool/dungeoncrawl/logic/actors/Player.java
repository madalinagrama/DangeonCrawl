package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Inventory;
import com.codecool.dungeoncrawl.model.PlayerModel;

public class Player extends Actor {
    private int id;
    private Inventory inventory;
    private int armor = 0;
    private int dX;
    private int dY;

    public Player(Cell cell) {
        super(cell);
        cell.setX(5);
        cell.setY(14);
        this.inventory = new Inventory();
        this.damage = 5;
   }

    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    public Player(Cell cell, PlayerModel playerModel) {
        super(cell);
        this.name = playerModel.getPlayerName();
        this.id = playerModel.getId();
        this.health = playerModel.getHp();
        this.damage = playerModel.getDamage();
        this.armor = playerModel.getArmor();
        this.inventory = playerModel.getInventory();
        this.map_id = playerModel.getMap_id();
    }


    public void setDamage() {
        this.damage += 6;
    }

    public int getDamage() {
        return damage;
    }

    public Inventory getInventory() {
        return inventory;
    }
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTileName() {
        return "player";
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor += armor;
    }

    @Override
    public void move(int dx, int dy) {
            super.move(dx, dy);
    }

    @Override
    public void makeMove() {
        Cell nextCell = getCell().getNeighbor(dX, dY);
        if ((nextCell.getType() != CellType.WALL) && (nextCell.getType() != CellType.CLOSEDOOR) && (nextCell.getActor() == null) && (nextCell.getType() != CellType.GHOST) && (nextCell.getType() != CellType.BOSSDOOR)) {
            move(dX, dY);
        } else if (nextCell.getType() == CellType.CLOSEDOOR && inventory.getInventory().containsKey("key")) {

            nextCell.setType(CellType.OPENDOOR);
            move(dX, dY);
        } else if (nextCell.getType() == CellType.BOSSDOOR && inventory.getInventory().containsKey("hammer")) {
            move(dX, dY);
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
//                nextCell.getActor().getCell().setType(CellType.FLOOR);
                nextCell.getActor().getCell().setActor(null);

            }
        } else if (nextCell.getActor() instanceof Soldier) {
            this.setHealth(((Soldier) nextCell.getActor()).getDamage());
            nextCell.getActor().setHealth(damage);
            if (nextCell.getActor().getHealth() <= 0) {
//                nextCell.getActor().getCell().setType(CellType.FLOOR);
                nextCell.getActor().getCell().setActor(null);
            }
        } else if (nextCell.getActor() instanceof Boss) {
            this.setHealth(((Boss) nextCell.getActor()).getDamage()-armor);
            nextCell.getActor().setHealth(damage);
            if (nextCell.getActor().getHealth() <= 0) {
                nextCell.getActor().getCell().setType(CellType.FLOOR);
                nextCell.getActor().getCell().setActor(null);
            }
        }
    }

    public void setNewDirection(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    @Override
    public void setHealth(int damage) {
        this.health -= damage;
    }

    public void setHealthUp(int points) {
        this.health += points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
