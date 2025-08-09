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
    private int lastRollTotal;

    // Track remaining houses and hotels available for building
    private int housesAvailable = 32;
    private int hotelsAvailable = 12;

    /**
     * Emitted when a player lands on an unowned property and should be asked to
     * purchase it. The value contains the player and the tile in question.
     */
    public final MutableLiveData<PurchaseEvent> purchaseEvent = new MutableLiveData<>();

    /** Simple holder for a purchase prompt consisting of the player and tile. */
    public static class PurchaseEvent {
        public final Player player;
        public final Tile tile;

        public PurchaseEvent(Player player, Tile tile) {
            this.player = player;
            this.tile = tile;
        }
    }

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

    public int getHousesAvailable() {
        return housesAvailable;
    }

    public int getHotelsAvailable() {
        return hotelsAvailable;
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
        lastRollTotal = rollTotal;

        Tile tile = tileMap.get(newPos);
        if (tile != null) {
            processTile(player, tile);
        }
        // ensure observers are notified of position change even if nothing else happens
        players.setValue(players.getValue());
    }

    public void processTile(Player player, Tile tile) {
        if (tile.type != TileType.PROPERTY) {
            return;
        }

        if (!tile.isOwned) {
            // prompt the UI to offer a purchase
            purchaseEvent.setValue(new PurchaseEvent(player, tile));
            return;
        }

        if (tile.ownerId != player.id) {
            if (tile.mortgaged) {
                return;
            }
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

            int rent;
            if ("Utility".equals(tile.colorGroup)) {
                int owned = countOwned(tile.ownerId, "Utility");
                int multiplier = tile.rent[Math.max(0, owned - 1)];
                rent = multiplier * lastRollTotal;
            } else if ("Railroad".equals(tile.colorGroup)) {
                int owned = countOwned(tile.ownerId, "Railroad");
                rent = tile.rent[Math.max(0, owned - 1)];
            } else {
                rent = tile.rent[tile.houseCount];
                if (tile.houseCount == 0 && ownsGroup(tile.ownerId, tile.colorGroup)) {
                    rent *= 2;
                }
            }
            player.money -= rent;
            owner.money += rent;

            // Trigger observers only after successful transaction
            players.setValue(currentPlayers);
        }
    }

    /**
     * Purchase the given tile for the provided player, deducting the tile price
     * and updating ownership. Observers are notified of the change.
     */
    public void buyProperty(Player player, Tile tile) {
        if (tile.type != TileType.PROPERTY || tile.isOwned) {
            return;
        }

        if (player.money >= tile.price) {
            player.money -= tile.price;
            tile.isOwned = true;
            tile.ownerId = player.id;
            players.setValue(players.getValue());
        }
        // clear any existing purchase prompt
        purchaseEvent.setValue(null);
    }

    /**
     * Clear any outstanding purchase prompt without buying the property.
     */
    public void declinePurchase() {
        purchaseEvent.setValue(null);
    }

    /**
     * Mortgage a property, giving the owner cash equal to the mortgage value
     * and marking the tile as mortgaged.
     */
    public void mortgageProperty(Player player, Tile tile) {
        if (tile.ownerId != player.id || tile.mortgaged) {
            return;
        }
        tile.mortgaged = true;
        player.money += tile.mortgageValue;
        players.setValue(players.getValue());
    }

    /**
     * Remove a mortgage from a property if the owner can afford it.
     */
    public void unmortgageProperty(Player player, Tile tile) {
        if (tile.ownerId != player.id || !tile.mortgaged) {
            return;
        }
        if (player.money >= tile.mortgageValue) {
            player.money -= tile.mortgageValue;
            tile.mortgaged = false;
            players.setValue(players.getValue());
        }
    }

    public void upgradeHouse(int playerId, Tile tile) {
        // Must own the tile and entire color group
        if (tile.ownerId != playerId || !ownsGroup(playerId, tile.colorGroup)) {
            return;
        }

        // Cannot upgrade beyond a hotel
        if (tile.houseCount >= 5) {
            return;
        }

        List<Player> currentPlayers = players.getValue();
        if (currentPlayers == null) {
            return;
        }

        for (Player p : currentPlayers) {
            if (p.id != playerId) {
                continue;
            }

            if (p.money < tile.houseCost) {
                return;
            }

            if (tile.houseCount < 4) {
                // Building houses
                if (housesAvailable <= 0) {
                    return;
                }
                p.money -= tile.houseCost;
                tile.houseCount++;
                housesAvailable--;
            } else {
                // Converting four houses to a hotel
                if (hotelsAvailable <= 0) {
                    return;
                }
                p.money -= tile.houseCost;
                tile.houseCount = 5;
                hotelsAvailable--;
                housesAvailable += 4; // return houses to supply
            }
            break;
        }
        // notify observers of changes
        players.setValue(currentPlayers);
    }

    private int countOwned(int ownerId, String color) {
        int count = 0;
        for (Tile t : tileMap.values()) {
            if (color.equals(t.colorGroup) && t.ownerId == ownerId) {
                count++;
            }
        }
        return count;
    }

    private boolean ownsGroup(int ownerId, String color) {
        int total = 0;
        int owned = 0;
        for (Tile t : tileMap.values()) {
            if (color.equals(t.colorGroup)) {
                total++;
                if (t.ownerId == ownerId) {
                    owned++;
                }
            }
        }
        return total > 0 && total == owned;
    }
}
