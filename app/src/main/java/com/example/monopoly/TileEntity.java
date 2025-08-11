package com.example.monopoly;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room representation of a board tile. Only stores immutable
 * information about the tile so that the static board can be
 * reconstructed or queried from the database if needed.
 */
@Entity
public class TileEntity {
    @PrimaryKey
    public int id;
    public String name;
    public String type;
    public int price;
    public String colorGroup;
}
