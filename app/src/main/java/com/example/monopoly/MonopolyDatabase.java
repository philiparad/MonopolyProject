package com.example.monopoly;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class, TileEntity.class, Ownership.class, GameSession.class}, version = 2)
public abstract class MonopolyDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
    public abstract TileDao tileDao();
    public abstract OwnershipDao ownershipDao();
    public abstract GameSessionDao gameSessionDao();
}
