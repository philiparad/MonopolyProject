package com.example.monopoly;

package com.example.monopoly;

public class Tile {
    public String name;
    public TileType type;
    public int price;
    public boolean isOwned;
    public int ownerId;

    public Tile(String name, TileType type, int price) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.isOwned = false;
        this.ownerId = -1;
    }
}