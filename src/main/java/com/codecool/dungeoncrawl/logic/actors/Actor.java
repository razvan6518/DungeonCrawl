package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.items.Item;

import java.util.ArrayList;

public abstract class Actor implements Drawable {
    private Cell cell;
    protected int damage;
    protected int health = 10;
    protected ArrayList<Item> items;

    public Actor(Cell cell) {
        this.damage = 3;
        this.cell = cell;
        this.cell.setActor(this);
        this.items = new ArrayList<>();
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getTileName().equals("wall"))
            return;
        if (nextCell.getActor() instanceof Skeleton){
            nextCell.getActor().getHit(this);
            if (nextCell.getActor() instanceof Skeleton){
                this.getHit(nextCell.getActor());
            }
        } else {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void getHit(Actor actor) {
        this.health -= actor.getDamage();
        if (this.health < 1){
            this.cell.removeActor();
        }
    }

    public void addItem(Item item){
        this.items.add(item);
        if (item.getTileName().equals("sword")){
            this.damage += 5;
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getDamage() {
        return damage;
    }
}
