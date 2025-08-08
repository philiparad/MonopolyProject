package com.example.monopoly;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1)
public abstract class MonopolyDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
}
