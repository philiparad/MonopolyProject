package com.example.monopoly;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public static Map<Integer, Tile> createTiles() {
        Map<Integer, Tile> tiles = new HashMap<>();
        tiles.put(0, new Tile("GO", TileType.GO, 0, 0, null));
        tiles.put(1, new Tile("Mediterranean Avenue", TileType.PROPERTY, 60, 2, "Brown"));
        tiles.put(2, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, 0, null));
        tiles.put(3, new Tile("Baltic Avenue", TileType.PROPERTY, 60, 4, "Brown"));
        tiles.put(4, new Tile("Income Tax", TileType.TAX, 200, 0, null));
        tiles.put(5, new Tile("Reading Railroad", TileType.PROPERTY, 200, 25, "Railroad"));
        tiles.put(6, new Tile("Oriental Avenue", TileType.PROPERTY, 100, 6, "Light Blue"));
        tiles.put(7, new Tile("Chance", TileType.CHANCE, 0, 0, null));
        tiles.put(8, new Tile("Vermont Avenue", TileType.PROPERTY, 100, 6, "Light Blue"));
        tiles.put(9, new Tile("Connecticut Avenue", TileType.PROPERTY, 120, 8, "Light Blue"));
        tiles.put(10, new Tile("Jail / Just Visiting", TileType.JAIL, 0, 0, null));
        tiles.put(11, new Tile("St. Charles Place", TileType.PROPERTY, 140, 10, "Pink"));
        tiles.put(12, new Tile("Electric Company", TileType.PROPERTY, 150, 4, "Utility"));
        tiles.put(13, new Tile("States Avenue", TileType.PROPERTY, 140, 10, "Pink"));
        tiles.put(14, new Tile("Virginia Avenue", TileType.PROPERTY, 160, 12, "Pink"));
        tiles.put(15, new Tile("Pennsylvania Railroad", TileType.PROPERTY, 200, 25, "Railroad"));
        tiles.put(16, new Tile("St. James Place", TileType.PROPERTY, 180, 14, "Orange"));
        tiles.put(17, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, 0, null));
        tiles.put(18, new Tile("Tennessee Avenue", TileType.PROPERTY, 180, 14, "Orange"));
        tiles.put(19, new Tile("New York Avenue", TileType.PROPERTY, 200, 16, "Orange"));
        tiles.put(20, new Tile("Free Parking", TileType.FREE_PARKING, 0, 0, null));
        tiles.put(21, new Tile("Kentucky Avenue", TileType.PROPERTY, 220, 18, "Red"));
        tiles.put(22, new Tile("Chance", TileType.CHANCE, 0, 0, null));
        tiles.put(23, new Tile("Indiana Avenue", TileType.PROPERTY, 220, 18, "Red"));
        tiles.put(24, new Tile("Illinois Avenue", TileType.PROPERTY, 240, 20, "Red"));
        tiles.put(25, new Tile("B. & O. Railroad", TileType.PROPERTY, 200, 25, "Railroad"));
        tiles.put(26, new Tile("Atlantic Avenue", TileType.PROPERTY, 260, 22, "Yellow"));
        tiles.put(27, new Tile("Ventnor Avenue", TileType.PROPERTY, 260, 22, "Yellow"));
        tiles.put(28, new Tile("Water Works", TileType.PROPERTY, 150, 4, "Utility"));
        tiles.put(29, new Tile("Marvin Gardens", TileType.PROPERTY, 280, 24, "Yellow"));
        tiles.put(30, new Tile("Go To Jail", TileType.GO_TO_JAIL, 0, 0, null));
        tiles.put(31, new Tile("Pacific Avenue", TileType.PROPERTY, 300, 26, "Green"));
        tiles.put(32, new Tile("North Carolina Avenue", TileType.PROPERTY, 300, 26, "Green"));
        tiles.put(33, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, 0, null));
        tiles.put(34, new Tile("Pennsylvania Avenue", TileType.PROPERTY, 320, 28, "Green"));
        tiles.put(35, new Tile("Short Line Railroad", TileType.PROPERTY, 200, 25, "Railroad"));
        tiles.put(36, new Tile("Chance", TileType.CHANCE, 0, 0, null));
        tiles.put(37, new Tile("Park Place", TileType.PROPERTY, 350, 35, "Dark Blue"));
        tiles.put(38, new Tile("Luxury Tax", TileType.TAX, 100, 0, null));
        tiles.put(39, new Tile("Boardwalk", TileType.PROPERTY, 400, 50, "Dark Blue"));
        return tiles;
    }
}
