package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Scorpion;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine();

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
                        case 'D':
                            cell.setType(CellType.DOOR_CLOSED);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.setActor(new Skeleton(cell));
                            break;
                        case 'c':
                            cell.setType(CellType.FLOOR);
                            Scorpion scorpion = new Scorpion(cell);
                            map.setActor(scorpion);
                            break;
                        case 'M':
                            cell.setType(CellType.FLOOR);
                            Monster monster = new Monster(cell);
                            map.setActor(monster);
                            break;
                        case 'F':
                            cell.setType(CellType.FENCE_UP);
                            break;
                        case 'B':
                            cell.setType(CellType.CRATE);
                            break;
                        case 'T':
                            cell.setType(CellType.TRAP);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'X':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Sword(cell));
                            break;
                        case 'K':
                            cell.setType(CellType.FLOOR);
                            map.setItem(new Key(cell));
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
