package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Monster;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Scorpion;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;
    ArrayList<Actor> scorpions = new ArrayList<>();
    ArrayList<Actor> monsters = new ArrayList<>();

    private Player player;
    private Actor actor;
    private Item item;

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

    public void setActor(Actor actor) {
        if (actor instanceof Scorpion)
            scorpions.add(actor);
        if (actor instanceof Monster)
            monsters.add(actor);
        this.actor = actor;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ArrayList<Actor> getScorpions() {
        return scorpions;
    }

    public ArrayList<Actor> getMonsters() {
        return monsters;
    }

    public Actor getActor() {
        return actor;
    }

    public Item getItem() {
        return item;
    }
}
