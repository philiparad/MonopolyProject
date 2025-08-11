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
    public boolean inJail;
    public int jailTurns;
    public int jailFreeCards;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.money = 1500;
        this.inJail = false;
        this.jailTurns = 0;
        this.jailFreeCards = 0;
    }

    @Override
    public String toString() {
        return name;
    }
}