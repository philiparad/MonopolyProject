package com.example.monopoly;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a relationship between a player and a tile they own.
 * Includes dynamic information about the property such as house
 * count and mortgage status.
 */
@Entity
public class Ownership {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int playerId;
    public int tileId;
    public int houseCount;
    public boolean mortgaged;
}
