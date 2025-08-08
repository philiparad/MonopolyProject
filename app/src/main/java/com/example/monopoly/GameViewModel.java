package com.example.monopoly;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel holding in-memory game state. This replaces the previously referenced
 * database layer so that activities can function without undefined dependencies.
 */
public class GameViewModel extends ViewModel {
    // Public so that existing activities can observe or query the list directly
    public final MutableLiveData<List<Player>> players = new MutableLiveData<>();
    private final Map<Integer, Tile> tileMap = new HashMap<>();

    public GameViewModel() {
        // Initialise some default players
        List<Player> initialPlayers = new ArrayList<>();
        Player p1 = new Player("Player 1");
        p1.id = 1;
        Player p2 = new Player("Player 2");
        p2.id = 2;
        initialPlayers.add(p1);
        initialPlayers.add(p2);
        players.setValue(initialPlayers);

        // Populate the board with the standard Monopoly tiles
        tileMap.putAll(Board.createTiles());
    }

    public Map<Integer, Tile> getTileMap() {
        return tileMap;
    }

    public void processTile(Player player, Tile tile) {
        if (tile.type == TileType.PROPERTY && tile.isOwned && tile.ownerId != player.id) {
            List<Player> currentPlayers = players.getValue();
            if (currentPlayers == null) {
                return;
            }

            Player owner = null;
            for (Player p : currentPlayers) {
                if (p.id == tile.ownerId) {
                    owner = p;
                    break;
                }
            }

            if (owner == null) {
                Log.w("GameViewModel", "Owned tile has no corresponding player: " + tile.name);
                return;
            }

            int rent = tile.rent + 5 * tile.houseCount;
            player.money -= rent;
            owner.money += rent;

            // Trigger observers only after successful transaction
            players.setValue(currentPlayers);
        }
    }

    public void upgradeHouse(int playerId, Tile tile) {
        if (tile.ownerId == playerId && tile.houseCount < 5) {
            tile.houseCount++;
            for (Player p : players.getValue()) {
                if (p.id == playerId && p.money >= 50) {
                    p.money -= 50;
                    break;
                }
            }
            players.setValue(players.getValue());
        }
    }
}
