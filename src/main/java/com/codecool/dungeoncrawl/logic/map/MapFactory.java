package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.manager.DbManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MapFactory {

    public MapFactory() {
    }

    public static void createMap(int numberOfRooms) throws SQLException {

        char[][] map = createEmptyMap();
        List<Room> rooms = placeRooms(5, map);
        createFileMap("map1");
        for (Room room: rooms){
            makePath(map, room, rooms.get(new Random().nextInt(rooms.size())));
            putActorInRoom(room, map, 'c');
            putActorInRoom(room, map, 'c');
            putActorInRoom(room, map, 'M');
            putActorInRoom(room, map, 'M');
        }
        //put stairs in a random room
        putActorInRoom(rooms.get(new Random().nextInt(rooms.size())), map, 'L');
        Room randomRoom = rooms.get(new Random().nextInt(rooms.size()));
        map[randomRoom.getY() + randomRoom.getHeight()/2][randomRoom.getX() + randomRoom.getWidth()/2] ='@';
        addWalls(map);
        writeToFile(map, 1);
        writeInDb(map);
    }

    public static void putActorInRoom(Room room, char[][] map, char actor){
        map[room.getY() + new Random().nextInt(room.getHeight() - 3) + 1][room.getX() + (new Random().nextInt(room.getWidth() - 3) + 1)] = actor;
    }

    private static void addWalls(char[][] map){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[0].length; j++){
                if (map[i][j] == '.' || map[i][j] == 'D'){
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
        boolean putFirstDoor = true;
        boolean putSecondDoor = true;
        int[] startPosition = new int[] {room1.getX() + (room1.getWidth() / 2), room1.getY() + (room1.getHeight() / 2)};
        int[] endPosition = new int[] {room2.getX() + (room2.getWidth() / 2), room2.getY() + (room2.getHeight() / 2)};
        int[] currentPosition = startPosition;
        int[] randomDirection;
        while (currentPosition[0] != endPosition[0] || currentPosition[1] != endPosition[1]){
            if (map[currentPosition[1]][currentPosition[0]] == ' ' && putFirstDoor){
                map[currentPosition[1]][currentPosition[0]] = 'D';
                putFirstDoor = false;
            }
            else {
                map[currentPosition[1]][currentPosition[0]] = '.';
            }
            randomDirection = pickRandomDirection(currentPosition, endPosition, map);
            if (map[currentPosition[1] + randomDirection[1]][currentPosition[0] + randomDirection[0]] == '#' && putSecondDoor && !putFirstDoor){
                putSecondDoor = false;
                map[currentPosition[1]][currentPosition[0]] = 'D';
            }
            currentPosition[0] = currentPosition[0] + randomDirection[0];
            currentPosition[1] = currentPosition[1] + randomDirection[1];
        }
    }

    private static int[] pickRandomDirection(int[] startPosition, int[] endPosition, char[][] map){
        List<int[]> validDirections = new ArrayList<>();
        if ((startPosition[0] - endPosition[0]) > 0 && map[startPosition[0] - 1][startPosition[1]] != '#'){
            validDirections.add(new int[] {-1, 0});
        }
        if ((startPosition[0] - endPosition[0]) < 0 && map[startPosition[0] + 1][startPosition[1]] != '#'){
            validDirections.add(new int[] {+1, 0});
        }
        if ((startPosition[1] - endPosition[1]) > 0 && map[startPosition[0]][startPosition[1] - 1] != '#'){
            validDirections.add(new int[] {0, -1});
        }
        if ((startPosition[1] - endPosition[1]) < 0 && map[startPosition[0]][startPosition[1] + 1] != '#'){
            validDirections.add(new int[] {0, +1});
        }
        if (validDirections.size() == 0){
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
        }
        return validDirections.get(new Random().nextInt(validDirections.size()));
    }

    private static void writeToFile(char[][] map, int mapNumber){
        try {
            writeToFileMap("map" + mapNumber, "200 200\n");
            writeToFileMap("map" + mapNumber, "\n".repeat(10));
        } catch (IOException ignored) {}
        for (int i = 0; i < map.length; i++){
            try {
                writeToFileMap("map" + mapNumber, " ".repeat(30));
            } catch (IOException ignored) {}
            for (int j = 0; j < map[0].length; j++){
                try {
                    writeToFileMap("map" + mapNumber, String.valueOf(map[i][j]));
                } catch (IOException ignored) {}
            }
            try {
                writeToFileMap("map" + mapNumber, String.valueOf('\n'));
            } catch (IOException ignored) {}
        }
    }

    private static void writeInDb(char[][] map) throws SQLException {
        char[][] extendedMap = new char[map.length + 40][map[0].length + 40];
        for (int i = 0; i < extendedMap.length; i++){
            for (int j = 0; j < extendedMap[0].length; j++){
                if (i < 15 || i >= map.length + 15 || j < 15 || j >= map[0].length + 15 ){
                    extendedMap[i][j] = ' ';
                } else {
                    extendedMap[i][j] = map[i - 15][j - 15];
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
        dbManager.setup();
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        dbManager.saves.addSave(date, mapForDb);
    }

    private static List<Room> placeRooms(int numberOfRooms, char[][] map){
        List<Room> rooms = new ArrayList<>();
        Random rnd = new Random();
        Room randomRoom;
        while (numberOfRooms > 0){
            randomRoom = new Room(rnd.nextInt(140), rnd.nextInt(140), rnd.nextInt(15) + 10, rnd.nextInt(15) + 10);
            if (canPlaceRoom(randomRoom, map)){
                numberOfRooms --;
                placeRoom(randomRoom, map);
                rooms.add(randomRoom);
            }
        }
        return rooms;
    }

    private static char[][] createEmptyMap(){
        char[][] map = new char[200][200];
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
        File file = new File("/home/razvan/Documents/projects/oop/week3/dungeon-crawl-1-java-razvan6518/src/main/resources/" + fineName + ".txt");
        if (file.delete()) {
            System.out.println("Deleted the file: " + file.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
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