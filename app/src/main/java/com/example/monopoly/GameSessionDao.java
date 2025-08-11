package com.example.monopoly;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface GameSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameSession session);

    @Query("SELECT * FROM GameSession WHERE id = :id")
    GameSession getSession(int id);

    @Query("DELETE FROM GameSession")
    void clear();
}
