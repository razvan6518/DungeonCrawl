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
        if (nextCell.getTileName().equals("doorClosed")){
            for (Item item: items){
                if (item.getTileName().equals("key"))
                    nextCell.setType(CellType.DOOR_OPENED);
            }
            return;
        }
        if (nextCell.getTileName().equals("crate")){
            if (nextCell.getNeighbor(dx, dy).getTileName().equals("trap")){

            }
            nextCell.getNeighbor(dx, dy).setType(CellType.CRATE);
            nextCell.setType(CellType.FLOOR);
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
            return;
        }
        if (nextCell.getActor() != null){
            nextCell.getActor().hit(this);
            if (nextCell.getActor() instanceof Skeleton || nextCell.getActor() instanceof Scorpion || nextCell.getActor() instanceof Monster){
                this.hit(nextCell.getActor());
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

    public void hit(Actor actor) {
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
