package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.manager.DbManager;
import com.codecool.dungeoncrawl.logic.map.MapFactory;
import com.codecool.dungeoncrawl.logic.util.Util;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class Main extends Application {
    public static GameMap map;
    Canvas canvas = new Canvas(
            50 * Tiles.TILE_WIDTH,
            40 * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label itemsDisplay = new Label();
    Button button = new Button("Take item");
    GridPane ui = new GridPane();
    static DbManager dbManager;

    public static void main(String[] args) throws SQLException {
//        MapFactory.createMap(13);
        map = MapLoader.loadMap();
        dbManager = new DbManager();
        dbManager.setup();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ui.setPrefWidth(500);
        ui.setPadding(new Insets(10));
        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(itemsDisplay, 0, 2);
        button.setFocusTraversable(false);
        ui.add(button, 0, 1);

        button.setOnAction(buttonHandler);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            Item item = map.getPlayer().getCell().getItem();
            map.getPlayer().getCell().removeItem();
            map.getPlayer().addItem(item);
            event.consume();
        }
    };

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                refresh();
                break;
            case S:
                if (keyEvent.isControlDown()){
                    java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                    Util.saveMap(map);
//                    dbManager.saves.addSave(date, "map");
                }
                break;
        }
    }

    private void refresh() {
        ArrayList<int[]> directions = new ArrayList<>(Arrays.asList(new int[] {0, -1}, new int[] {0, 1}, new int[] {-1, 0}, new int[] {1, 0}));
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, 10, 10);
        int playerX2 = map.getPlayer().getX();
        int playerY2 = map.getPlayer().getY();
        for (int x = 0; x < 60; x++) {
            for (int y = 0; y < 40; y++) {
                try {
                    Cell cell = map.getCell(playerX2 - 30 + x, playerY2 - 20 + y);
                    if (cell.getActor() != null && cell.getActor().getHealth() > 0) {
                        Tiles.drawTile(context, cell.getActor(), x, y);
                    }else if (cell.getItem() != null) {
                        Tiles.drawTile(context, cell.getItem(), x, y);
                    }else {
                        Tiles.drawTile(context, cell, x, y);
                    }
                } catch (IndexOutOfBoundsException ignored){}

            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        ArrayList<Item> items = map.getPlayer().getItems();
        if (items.size() > 0){
            String itemsText = "";
            for (Item item: items){
                itemsText += "\n" + item.getTileName();
            }
            itemsDisplay.setText(itemsText);
        }
        for (Actor scorpion: map.getScorpions()){
            int[] randomDirection = directions.get(new Random().nextInt(4));
            scorpion.move(randomDirection[0], randomDirection[1]);
        }
        for (Actor monster: map.getMonsters()){
            int playerX = map.getPlayer().getCell().getX();
            int playerY = map.getPlayer().getCell().getY();
            int monsterX = monster.getX();
            int monsterY = monster.getY();
            int[] direction = new int[] {Integer.compare(playerX - monsterX, 0), Integer.compare(playerY - monsterY, 0)};
            monster.move(direction[0], direction[1]);
        }
    }
}
