package com.example.monopoly;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Player {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public int position;
    public int money;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.money = 1500;
    }
}