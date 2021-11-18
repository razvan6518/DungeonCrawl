package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Key;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
        this.damage = 5;
        this.health = 30;
        this.items.add(new Key());
        this.items.add(new Key());
        this.items.add(new Key());
    }

    public String getTileName() {
        return "player";
    }
}
