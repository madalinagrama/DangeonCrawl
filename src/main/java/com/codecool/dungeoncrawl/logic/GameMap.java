package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;


import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;
    private String name;
    private Actor boss;
    private Player player;
    private List<Ghost> ghosts = new ArrayList<Ghost>();
    private List<Soldier> soldiers = new ArrayList<>();


    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public List<Soldier> getSoldiers() {
        return soldiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMapString() {
        StringBuilder str = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                str.append(cells[x][y].getChar(cells[x][y]));
            }
            str.append("\n");
        }
        return str.toString();
    }


    public void setBoss(Actor boss) {
        this.boss = boss;
    }
}
