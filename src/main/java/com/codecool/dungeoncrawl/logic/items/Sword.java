package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.Random;

public class Sword extends Item{

    private int damage;

    public Sword(Cell cell) {
        super(cell);
        this.damage = new Random().nextInt(15) + 5;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String getTileName() {
        return "sword";
    }
}
