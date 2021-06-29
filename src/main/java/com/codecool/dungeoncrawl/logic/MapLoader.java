package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String maps) {
        InputStream is = MapLoader.class.getResourceAsStream(maps);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.SKELETON);
                            new Skeleton(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            map.getPlayer().setMap_id(1);
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            new Sword(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            new Key(cell);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSEDOOR);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            Ghost ghost = new Ghost(cell, map);
                            map.getGhosts().add(ghost);
                            break;
                        case 'p':
                            cell.setType(CellType.POTION);
                            new Potion(cell);
                            break;
                        case 'v':
                            cell.setType(CellType.FLOOR);
                            Soldier soldier = new Soldier(cell, map);
                            map.getSoldiers().add(soldier);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

    public static GameMap loadMap2(String maps, Player player) {
        InputStream ls = MapLoader.class.getResourceAsStream(maps);
        Scanner scanner = new Scanner(ls);
        int width = scanner.nextInt();
        int height = scanner.nextInt();


        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(player);
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            new Sword(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            new Key(cell);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSEDOOR);
                            break;
                        case 'b':
                            cell.setType(CellType.BACKDOOR);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            Ghost ghost = new Ghost(cell, map);
                            map.getGhosts().add(ghost);
                            break;
                        case 'p':
                            cell.setType(CellType.POTION);
                            new Potion(cell);
                            break;
                        case 'v':
                            cell.setType(CellType.FLOOR);
                            Soldier soldier = new Soldier(cell, map);
                            map.getSoldiers().add(soldier);
                            break;
                        case 'f':
                            cell.setType(CellType.PORTAL);
                            break;
                        case 'c':
                            cell.setType(CellType.BOSSDOOR);
                            break;
                        case 'l':
                            cell.setType(CellType.HAMMER);
                            new Hammer(cell);
                            break;
                        case 'a':
                            cell.setType(CellType.ARMOR);
                            new Armor(cell);
                            break;
                        case 'j':
                            cell.setType(CellType.FLOOR);
                            Boss boss = new Boss(cell);
                            map.setBoss(boss);
                            break;
                        case '*':
                            cell.setType(CellType.WINDOOR);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }


    public static GameMap loadSavedMap(String maps) {
        int width;
        int height;
        Scanner scanner = new Scanner(maps);

        if( maps.length() == 546){
            width = 25;
            height = 21;
        }else {
            width = 48;
            height = 32;
        }

//        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case 'w':
                            cell.setType(CellType.SWORD);
                            new Sword(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.KEY);
                            new Key(cell);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSEDOOR);
                            break;
                        case 'b':
                            cell.setType(CellType.BACKDOOR);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            Ghost ghost = new Ghost(cell, map);
                            map.getGhosts().add(ghost);
                            break;
                        case 'p':
                            cell.setType(CellType.POTION);
                            new Potion(cell);
                            break;
                        case 'v':
                            cell.setType(CellType.FLOOR);
                            Soldier soldier = new Soldier(cell, map);
                            map.getSoldiers().add(soldier);
                            break;
                        case 'f':
                            cell.setType(CellType.PORTAL);
                            break;
                        case 'c':
                            cell.setType(CellType.BOSSDOOR);
                            break;
                        case 'l':
                            cell.setType(CellType.HAMMER);
                            new Hammer(cell);
                            break;
                        case 'a':
                            cell.setType(CellType.ARMOR);
                            new Armor(cell);
                            break;
                        case 'j':
                            cell.setType(CellType.FLOOR);
                            Boss boss = new Boss(cell);
                            map.setBoss(boss);
                            break;
                        case '*':
                            cell.setType(CellType.WINDOOR);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }


}
