package com.example.monopoly;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TileEntity> tiles);

    @Query("SELECT * FROM TileEntity")
    List<TileEntity> getAll();

    @Query("SELECT COUNT(*) FROM TileEntity")
    int count();
}
