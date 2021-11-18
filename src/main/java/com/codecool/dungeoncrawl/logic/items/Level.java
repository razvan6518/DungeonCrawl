package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Level extends Item{
    public Level(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "level";
    }
}
