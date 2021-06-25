package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.Item;

public class Cell implements Drawable {
    private CellType type;
    private Actor actor;
    private GameMap gameMap;
    private Item item;

    private int x, y;

    public Cell(GameMap gameMap, int x, int y, CellType type) {
        this.gameMap = gameMap;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public void setItem(Item item) { this.item = item; }

    public Actor getActor() {
        return actor;
    }

    public Cell getNeighbor(int dx, int dy) {
        return gameMap.getCell(x + dx, y + dy);
    }

    public Item getItem() { return item; }

    @Override
    public String getTileName() {
        return type.getTileName();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public char getChar(Cell cell) {
        if (cell.getType() == CellType.EMPTY) {
            return ' ';
        } else if ( cell.getType() == CellType.WALL) {
            return  '#';
        } else if (cell.getType() == CellType.FLOOR ) {
            if (cell.getActor() != null) {
                if( cell.getActor() instanceof Soldier) {
                    return  'v';
                } else if (cell.getActor() instanceof Ghost) {
                    return 'g';
                }else if (cell.getActor() instanceof Boss) {
                    return 'j';
                }
            }

        } else if (cell.getActor() instanceof Skeleton) {
            return 's';
        } else if (cell.getType() == CellType.SWORD) {
            return 'w';
        }else if (cell.getType() == CellType.POTION) {
            return 'p';
        }else if (cell.getType() == CellType.KEY) {
            return 'k';
        }else if (cell.getType() == CellType.CLOSEDOOR) {
            return 'd';
        }else if (cell.getType() == CellType.OPENDOOR) {
            return 'd';
        } else if (cell.getType() == CellType.BACKDOOR) {
            return 'b';
        }else if (cell.getType() == CellType.PORTAL) {
            return 'f';
        } else if (cell.getType() == CellType.BOSSDOOR){
            return 'c';
        } else if (cell.getType() == CellType.WINDOOR) {
            return '*';
        } else if (cell.getType() == CellType.ARMOR) {
            return 'a';
        } else if ( cell.getType() == CellType.HAMMER) {
            return 'l';
        }
        return '.';
    }
}
