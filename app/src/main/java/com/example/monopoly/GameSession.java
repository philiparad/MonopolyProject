package com.example.monopoly;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Holds global state for an in-progress game so that a session
 * can be persisted and restored across application launches.
 */
@Entity
public class GameSession {
    @PrimaryKey
    public int id;
    public int currentPlayerIndex;
    public int housesAvailable;
    public int hotelsAvailable;
}
