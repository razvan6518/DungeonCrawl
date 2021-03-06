package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Scorpion;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.items.Health;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Level;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.manager.DbManager;

import java.sql.SQLException;
import java.util.Random;

public class MapLoader {

    public static GameMap loadMap() {
        int width = 200;
        int height = 200;

        String mapFromDb = getMapFromDb();
        String[] mapSplitByLine = mapFromDb.split("\n");


        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = mapSplitByLine[y];
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            int rnd = new Random().nextInt(100);
                            cell.setType((rnd < 90) ? CellType.EMPTY : (rnd == 90) ? CellType.HOUSE : CellType.TREE);
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
                        case 'h':
                            cell.setType(CellType.HEALTH);
                            map.setItem(new Health(cell));
                            break;
                        case 'L':
                            cell.setType(CellType.LEVEL);
                            map.setItem(new Level(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

    private static String getMapFromDb() {
        DbManager dbManager = new DbManager();
        try {
            dbManager.setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbManager.saves.getLastSave();
    }

}
