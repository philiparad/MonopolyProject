package com.example.monopoly;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public static Map<Integer, Tile> createTiles() {
        Map<Integer, Tile> tiles = new HashMap<>();
        tiles.put(0, new Tile("GO", TileType.GO, 0, new int[]{0}, null, 0, 0));
        tiles.put(1, new Tile("Mediterranean Avenue", TileType.PROPERTY, 60,
                new int[]{2,10,30,90,160,250}, "Brown", 50, 30));
        tiles.put(2, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, new int[]{0}, null, 0, 0));
        tiles.put(3, new Tile("Baltic Avenue", TileType.PROPERTY, 60,
                new int[]{4,20,60,180,320,450}, "Brown", 50, 30));
        tiles.put(4, new Tile("Income Tax", TileType.TAX, 200, new int[]{0}, null, 0, 0));
        tiles.put(5, new Tile("Reading Railroad", TileType.PROPERTY, 200,
                new int[]{25,50,100,200}, "Railroad", 0, 100));
        tiles.put(6, new Tile("Oriental Avenue", TileType.PROPERTY, 100,
                new int[]{6,30,90,270,400,550}, "Light Blue", 50, 50));
        tiles.put(7, new Tile("Chance", TileType.CHANCE, 0, new int[]{0}, null, 0, 0));
        tiles.put(8, new Tile("Vermont Avenue", TileType.PROPERTY, 100,
                new int[]{6,30,90,270,400,550}, "Light Blue", 50, 50));
        tiles.put(9, new Tile("Connecticut Avenue", TileType.PROPERTY, 120,
                new int[]{8,40,100,300,450,600}, "Light Blue", 50, 60));
        tiles.put(10, new Tile("Jail / Just Visiting", TileType.JAIL, 0, new int[]{0}, null, 0, 0));
        tiles.put(11, new Tile("St. Charles Place", TileType.PROPERTY, 140,
                new int[]{10,50,150,450,625,750}, "Pink", 100, 70));
        tiles.put(12, new Tile("Electric Company", TileType.PROPERTY, 150,
                new int[]{4,10}, "Utility", 0, 75));
        tiles.put(13, new Tile("States Avenue", TileType.PROPERTY, 140,
                new int[]{10,50,150,450,625,750}, "Pink", 100, 70));
        tiles.put(14, new Tile("Virginia Avenue", TileType.PROPERTY, 160,
                new int[]{12,60,180,500,700,900}, "Pink", 100, 80));
        tiles.put(15, new Tile("Pennsylvania Railroad", TileType.PROPERTY, 200,
                new int[]{25,50,100,200}, "Railroad", 0, 100));
        tiles.put(16, new Tile("St. James Place", TileType.PROPERTY, 180,
                new int[]{14,70,200,550,750,950}, "Orange", 100, 90));
        tiles.put(17, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, new int[]{0}, null, 0, 0));
        tiles.put(18, new Tile("Tennessee Avenue", TileType.PROPERTY, 180,
                new int[]{14,70,200,550,750,950}, "Orange", 100, 90));
        tiles.put(19, new Tile("New York Avenue", TileType.PROPERTY, 200,
                new int[]{16,80,220,600,800,1000}, "Orange", 100, 100));
        tiles.put(20, new Tile("Free Parking", TileType.FREE_PARKING, 0, new int[]{0}, null, 0, 0));
        tiles.put(21, new Tile("Kentucky Avenue", TileType.PROPERTY, 220,
                new int[]{18,90,250,700,875,1050}, "Red", 150, 110));
        tiles.put(22, new Tile("Chance", TileType.CHANCE, 0, new int[]{0}, null, 0, 0));
        tiles.put(23, new Tile("Indiana Avenue", TileType.PROPERTY, 220,
                new int[]{18,90,250,700,875,1050}, "Red", 150, 110));
        tiles.put(24, new Tile("Illinois Avenue", TileType.PROPERTY, 240,
                new int[]{20,100,300,750,925,1100}, "Red", 150, 120));
        tiles.put(25, new Tile("B. & O. Railroad", TileType.PROPERTY, 200,
                new int[]{25,50,100,200}, "Railroad", 0, 100));
        tiles.put(26, new Tile("Atlantic Avenue", TileType.PROPERTY, 260,
                new int[]{22,110,330,800,975,1150}, "Yellow", 150, 130));
        tiles.put(27, new Tile("Ventnor Avenue", TileType.PROPERTY, 260,
                new int[]{22,110,330,800,975,1150}, "Yellow", 150, 130));
        tiles.put(28, new Tile("Water Works", TileType.PROPERTY, 150,
                new int[]{4,10}, "Utility", 0, 75));
        tiles.put(29, new Tile("Marvin Gardens", TileType.PROPERTY, 280,
                new int[]{24,120,360,850,1025,1200}, "Yellow", 150, 140));
        tiles.put(30, new Tile("Go To Jail", TileType.GO_TO_JAIL, 0, new int[]{0}, null, 0, 0));
        tiles.put(31, new Tile("Pacific Avenue", TileType.PROPERTY, 300,
                new int[]{26,130,390,900,1100,1275}, "Green", 200, 150));
        tiles.put(32, new Tile("North Carolina Avenue", TileType.PROPERTY, 300,
                new int[]{26,130,390,900,1100,1275}, "Green", 200, 150));
        tiles.put(33, new Tile("Community Chest", TileType.COMMUNITY_CHEST, 0, new int[]{0}, null, 0, 0));
        tiles.put(34, new Tile("Pennsylvania Avenue", TileType.PROPERTY, 320,
                new int[]{28,150,450,1000,1200,1400}, "Green", 200, 160));
        tiles.put(35, new Tile("Short Line Railroad", TileType.PROPERTY, 200,
                new int[]{25,50,100,200}, "Railroad", 0, 100));
        tiles.put(36, new Tile("Chance", TileType.CHANCE, 0, new int[]{0}, null, 0, 0));
        tiles.put(37, new Tile("Park Place", TileType.PROPERTY, 350,
                new int[]{35,175,500,1100,1300,1500}, "Dark Blue", 200, 175));
        tiles.put(38, new Tile("Luxury Tax", TileType.TAX, 100, new int[]{0}, null, 0, 0));
        tiles.put(39, new Tile("Boardwalk", TileType.PROPERTY, 400,
                new int[]{50,200,600,1400,1700,2000}, "Dark Blue", 200, 200));
        return tiles;
    }
}
