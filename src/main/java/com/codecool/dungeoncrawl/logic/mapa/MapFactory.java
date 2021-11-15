package com.codecool.dungeoncrawl.logic.mapa;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MapFactory {

    public static void createMap() {
        Random rnd = new Random();
        char[][] map = new char[160][160];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map.length; j++){
                map[i][j] = ' ';
            }
        }
        Room randomRoom;
        int rooms = 5;
        while (rooms > 0){
            randomRoom = new Room(rnd.nextInt(100), rnd.nextInt(40), rnd.nextInt(7) + 5, rnd.nextInt(7) + 5);
            if (canPlaceRoom(randomRoom, map)){
                placeRoom(randomRoom, map);
                rooms --;
            }
        }
        System.out.println(" ");
    }

    private static void placeRoom(Room room, char[][] map){
        for (int i = room.getY(); i < room.getHeight(); i++){
            for (int j = room.getX(); j < room.getWidth(); j++){
                    if (i == room.getY() || i - 1 == room.getHeight() || j == room.getX() || j - 1 == room.getWidth()){
                        map[i][j] = '#';
                    } else {
                        map[i][j] = '.';
                    }
            }
        }
    }

    private static boolean canPlaceRoom(Room room, char[][] map){
        for (int i = room.getY(); i < room.getHeight(); i++){
            for (int j = room.getX(); j < room.getWidth(); j++){
                if (map[i][j] != ' ')
                    return false;
            }
        }
        return true;
    }

    public static void writeRowToFileMap(String row) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/razvan/Documents/projects/oop/week3/dungeon-crawl-1-java-razvan6518/src/main/resources/filename.txt", true));
        writer.append(' ');
        writer.append(row);

        writer.close();
    }

    private static void createFileMap(){
       try {
           File myObj = new File("/home/razvan/Documents/projects/oop/week3/dungeon-crawl-1-java-razvan6518/src/main/resources/filename.txt");
           if (myObj.createNewFile()) {
               System.out.println("File created: " + myObj.getName());
           } else {
               System.out.println("File already exists.");
           }
       } catch (IOException e) {
           System.out.println("An error occurred.");
           e.printStackTrace();
       }
    }
}