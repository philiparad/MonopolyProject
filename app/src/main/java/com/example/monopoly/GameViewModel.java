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

    /**
     * Roll two six-sided dice.
     *
     * @return array containing die1, die2 and their total.
     */
    public int[] rollDice() {
        int die1 = 1 + (int) (Math.random() * 6);
        int die2 = 1 + (int) (Math.random() * 6);
        return new int[] { die1, die2, die1 + die2 };
    }

    /**
     * Move the given player forward by the supplied roll amount.
     * Wraps around the board and awards $200 for passing GO.
     */
    public void movePlayer(Player player, int rollTotal) {
        int oldPos = player.position;
        int newPos = (oldPos + rollTotal) % 40;
        if (oldPos + rollTotal >= 40) {
            player.money += 200;
        }
        player.position = newPos;

        Tile tile = tileMap.get(newPos);
        if (tile != null) {
            processTile(player, tile);
        }
        // ensure observers are notified of position change even if nothing else happens
        players.setValue(players.getValue());
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
