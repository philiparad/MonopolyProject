package com.example.monopoly;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OwnershipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ownership ownership);

    @Query("SELECT * FROM Ownership")
    List<Ownership> getAll();

    @Query("DELETE FROM Ownership")
    void clear();
}
