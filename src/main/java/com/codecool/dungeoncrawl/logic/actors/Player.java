package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
        this.damage = 5;
        this.health = 30;
    }

    public String getTileName() {
        return "player";
    }
}
