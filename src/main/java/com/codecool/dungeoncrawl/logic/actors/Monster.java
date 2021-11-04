package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Monster extends Actor{
    public Monster(Cell cell) {
        super(cell);
        this.damage = 2;
    }

    @Override
    public String getTileName() {
        return "monster";
    }
}
