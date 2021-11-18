package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.manager.DbManager;

import java.sql.SQLException;
import java.util.Calendar;

public class Util {
    public static void saveMap(GameMap map){
        char[][] extendedMap = new char[map.getHeight() + 40][map.getWidth() + 40];
        for (int i = 0; i < extendedMap.length; i++){
            for (int j = 0; j < extendedMap[0].length; j++){
                if (i < 15 || i >= map.getHeight() + 15 || j < 15 || j >= map.getWidth() + 15 ){
                    extendedMap[i][j] = ' ';
                } else {
                    switch (map.getCell(j - 15, i - 15).getTileName()) {
                        case "wall":
                            extendedMap[i][j] = '#';
                            break;
                        case "doorClosed":
                            extendedMap[i][j] = 'D';
                            break;
                        case "floor":
                            extendedMap[i][j] = '.';
                            break;
                        case "health":
                            extendedMap[i][j] = 'h';
                            break;
                        case "level":
                            extendedMap[i][j] = 'L';
                            break;
                        default:
                            extendedMap[i][j] = ' ';
                            break;
                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < extendedMap.length; i++){
            for (int j = 0; j < extendedMap[0].length; j++){
                stringBuilder.append(extendedMap[i][j]);
            }
            stringBuilder.append('\n');
        }

        String mapForDb = stringBuilder.toString();
        DbManager dbManager = new DbManager();
        try {
            dbManager.setup();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        dbManager.saves.addSave(date, mapForDb);
    }
}
