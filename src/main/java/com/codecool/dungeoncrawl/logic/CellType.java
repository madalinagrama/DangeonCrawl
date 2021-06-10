package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    SWORD("sword"),
    KEY("key"),
    OPENDOOR("openDoor"),
    CLOSEDOOR("closeDoor"),
    BACKDOOR("backDoor"),
    SKELETON("skeleton"),
    GHOST("ghost"),
    PLAYER("player"),
    POTION("potion"),
    SOLDIER("soldier"),
    PORTAL("portal"),
    BOSSDOOR("bossDoor"),
    HAMMER("hammer"),
    ARMOR("armor"),
    WINDOOR("winDoor");

    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }
}
