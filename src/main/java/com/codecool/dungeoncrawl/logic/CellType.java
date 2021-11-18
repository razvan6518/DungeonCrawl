package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    DOOR_CLOSED("doorClosed"),
    DOOR_OPENED("doorOpened"),
    MONSTER("monster"),
    FENCE_UP("fenceUp"),
    CRATE("crate"),
    TRAP("trap"),
    LEVEL("level"),
    HEALTH("health");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
