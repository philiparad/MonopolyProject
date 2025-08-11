package com.example.monopoly;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlayer(Player player);

    @Update
    void updatePlayer(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM Player")
    void clear();

    @Query("SELECT * FROM Player WHERE id = :id")
    Player getPlayerById(int id);

    @Query("SELECT * FROM Player")
    List<Player> getAllPlayers();
}
