package com.codecool.dungeoncrawl.logic.map;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapFactory {

    public static void createMap(int numberOfRooms) {
        char[][] map = createEmptyMap();
        List<Room> rooms = placeRooms(numberOfRooms, map);
        createFileMap("map2");
        for (Room room: rooms){
            makePath(map, room, rooms.get(new Random().nextInt(rooms.size())));
        }
        addWalls(map);
        writeToFile(map);
    }

    private static void addWalls(char[][] map){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                if (map[i][j] == '.'){
                    ArrayList<int[]> neighbours = getNeighbours(map, new int[] {i, j});
                    for (int[] neighbour: neighbours){
                        map[neighbour[0]][neighbour[1]] = '#';
                    }
                }
            }
        }
    }

    private static ArrayList<int[]> getNeighbours(char[][] map, int[] coordinates){
        ArrayList<int[]> neighbours = new ArrayList<>();
        try {
            if (map[coordinates[0] + 1][coordinates[1]] == ' ')
                neighbours.add(new int[] {coordinates[0] + 1, coordinates[1]});
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            if (map[coordinates[0] - 1][coordinates[1]] == ' ')
                neighbours.add(new int[] {coordinates[0] - 1, coordinates[1]});
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            if (map[coordinates[0]][coordinates[1] + 1] == ' ')
                neighbours.add(new int[] {coordinates[0], coordinates[1] + 1});
        } catch (IndexOutOfBoundsException ignored) {}
        try {
            if (map[coordinates[0]][coordinates[1] - 1] == ' ')
                neighbours.add(new int[] {coordinates[0], coordinates[1] - 1});
        } catch (IndexOutOfBoundsException ignored) {}
        return  neighbours;
    }

    private static void makePath(char[][] map, Room room1, Room room2){
        int[] startPosition = new int[] {room1.getX() + (room1.getWidth() / 2), room1.getY() + (room1.getHeight() / 2)};
        int[] endPosition = new int[] {room2.getX() + (room2.getWidth() / 2), room2.getY() + (room2.getHeight() / 2)};
        int[] currentPosition = startPosition;
        int[] randomDirection;
        while (currentPosition[0] != endPosition[0] || currentPosition[1] != endPosition[1]){
            map[currentPosition[1]][currentPosition[0]] = '.';
            randomDirection = pickRandomDirection(currentPosition, endPosition);
            currentPosition[0] = currentPosition[0] + randomDirection[0];
            currentPosition[1] = currentPosition[1] + randomDirection[1];
        }
    }

    private static int[] pickRandomDirection(int[] startPosition, int[] endPosition){
        List<int[]> validDirections = new ArrayList<>();
        if ((startPosition[0] - endPosition[0]) > 0){
            validDirections.add(new int[] {-1, 0});
        }
        if ((startPosition[0] - endPosition[0]) < 0){
            validDirections.add(new int[] {+1, 0});
        }
        if ((startPosition[1] - endPosition[1]) > 0){
            validDirections.add(new int[] {0, -1});
        }
        if ((startPosition[1] - endPosition[1]) < 0){
            validDirections.add(new int[] {0, +1});
        }
        return validDirections.get(new Random().nextInt(validDirections.size()));
    }

    private static void writeToFile(char[][] map){
        for (int i = 0; i < map.length; i++){
            try {
                writeToFileMap("map2", " ".repeat(30));
            } catch (IOException ignored) {}
            for (int j = 0; j < map[0].length; j++){
                try {
                    writeToFileMap("map2", String.valueOf(map[i][j]));
                } catch (IOException ignored) {}
            }
            try {
                writeToFileMap("map2", String.valueOf('\n'));
            } catch (IOException ignored) {}
        }
    }

    private static List<Room> placeRooms(int numberOfRooms, char[][] map){
        List<Room> rooms = new ArrayList<>();
        Random rnd = new Random();
        Room randomRoom;
        while (numberOfRooms > 0){
            randomRoom = new Room(rnd.nextInt(100), rnd.nextInt(40), rnd.nextInt(15) + 10, rnd.nextInt(15) + 10);
            if (canPlaceRoom(randomRoom, map)){
                placeRoom(randomRoom, map);
                numberOfRooms --;
                rooms.add(randomRoom);
            }
        }
        return rooms;
    }

    private static char[][] createEmptyMap(){
        char[][] map = new char[160][160];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map.length; j++){
                map[i][j] = ' ';
            }
        }
        return map;
    }

    private static void placeRoom(Room room, char[][] map){
        for (int i = room.getY(); i < room.getHeight() + room.getY(); i++){
            for (int j = room.getX(); j < room.getWidth() + room.getX(); j++){
                    if (i == room.getY() || i + 1 == room.getHeight() + room.getY() || j == room.getX() || j + 1 == room.getWidth() + room.getX()){
                        map[i][j] = '#';
                    } else {
                        map[i][j] = '.';
                    }
            }
        }
    }

    private static boolean canPlaceRoom(Room room, char[][] map){
        for (int i = room.getY(); i < room.getHeight() + room.getY(); i++){
            for (int j = room.getX(); j < room.getWidth() + room.getX(); j++){
                if (map[i][j] != ' ')
                    return false;
            }
        }
        return true;
    }

    public static void writeToFileMap(String fineName, String toWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("/home/razvan/Documents/projects/oop/week3/dungeon-crawl-1-java-razvan6518/src/main/resources/" + fineName + ".txt", true));
        writer.append(toWrite);
        writer.close();
    }

    private static void createFileMap(String fineName){
       try {
           File myObj = new File("/home/razvan/Documents/projects/oop/week3/dungeon-crawl-1-java-razvan6518/src/main/resources/" + fineName + ".txt");
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