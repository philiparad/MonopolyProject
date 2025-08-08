package com.example.monopoly;

public class Tile {
    public String name;
    public TileType type;
    public int price;
    public boolean isOwned;
    public int ownerId;
    public int houseCount;

    public Tile(String name, TileType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.isOwned = false;
        this.ownerId = -1;
        this.houseCount = 0;
    }
}