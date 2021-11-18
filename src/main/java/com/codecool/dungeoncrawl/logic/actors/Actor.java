package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.items.Health;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.map.MapFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Actor implements Drawable {
    private Cell cell;
    protected int damage;
    protected int health = 10;
    protected ArrayList<Item> items;
    protected boolean alive;

    public Actor(Cell cell) {
        this.damage = 3;
        this.cell = cell;
        this.cell.setActor(this);
        this.items = new ArrayList<>();
        this.alive = true;
    }

    public void move(int dx, int dy) {
        if (!alive) return;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getTileName().equals("wall"))
            return;
        if (nextCell.getTileName().equals("level") && this.getTileName().equals("player")){
            try {
                MapFactory.createMap(13);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Main.map = MapLoader.loadMap();
        }
        if (nextCell.getTileName().equals("doorClosed")){
            for (Item item: items){
                if (item.getTileName().equals("key"))
                    nextCell.setType(CellType.DOOR_OPENED);
                items.remove(item);
            }
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
        if (!alive) return;
        this.health -= actor.getDamage();
        if (this.health < 1){
            this.cell.removeActor();
            this.alive = false;
            this.cell.setItem(generateItem(cell));
        }
    }

    private Item generateItem(Cell cell){
        int choice = new Random().nextInt(100);
        if (choice < 50){
            return new Key(cell);
        }
        if (choice > 49 && choice < 60){
            return new Sword(cell);
        }
        if (choice >= 60){
            return new Health(cell);
        }
        return null;
    }


    public void addItem(Item item){
        if (item.getTileName().equals("key")){
            this.items.add(item);
        }
        if (item.getTileName().equals("sword")){
            this.damage += 5;
        }
        if (item.getTileName().equals("health")){
            this.health += 15;
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getDamage() {
        return damage;
    }
}
