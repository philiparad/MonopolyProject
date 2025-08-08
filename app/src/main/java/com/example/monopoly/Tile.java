package com.example.monopoly;

public class Tile {
    public String name;
    public TileType type;
    public int price;
    public int rent;
    public String colorGroup;
    public boolean isOwned;
    public int ownerId;
    public int houseCount;

    public Tile(String name, TileType type, int price, int rent, String colorGroup) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.rent = rent;
        this.colorGroup = colorGroup;
        this.isOwned = false;
        this.ownerId = -1;
        this.houseCount = 0;
    }
}