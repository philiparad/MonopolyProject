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

    // Track the current turn and doubles rolled in succession
    private int currentPlayerIndex;
    private int doublesCount;
    public final MutableLiveData<Player> currentTurn = new MutableLiveData<>();

    // Track remaining houses and hotels available for building
    private int housesAvailable = 32;
    private int hotelsAvailable = 12;

    /**
     * Emitted when a player lands on an unowned property and should be asked to
     * purchase it. The value contains the player and the tile in question.
     */
    public final MutableLiveData<PurchaseEvent> purchaseEvent = new MutableLiveData<>();
    public final MutableLiveData<Card> cardDrawn = new MutableLiveData<>();
    // Simple string messages to prompt the user when landing on special tiles
    public final MutableLiveData<String> tileMessage = new MutableLiveData<>();
    // Emits a tile when an auction should be started for it
    public final MutableLiveData<Tile> auctionTile = new MutableLiveData<>();
    private final CardDeck chanceDeck = new CardDeck("CHANCE");
    private final CardDeck communityDeck = new CardDeck("COMMUNITY_CHEST");

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
        currentPlayerIndex = 0;
        doublesCount = 0;
        currentTurn.setValue(initialPlayers.get(currentPlayerIndex));

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
     * Advance to the next player's turn, accounting for doubles. If a double
     * is rolled the same player retains the turn unless it is the third
     * consecutive double, which sends the player to jail and advances the
     * turn. Observers are notified of the active player through
     * {@link #currentTurn}.
     *
     * @param rolledDouble whether the current player rolled doubles
     */
    public void nextTurn(boolean rolledDouble) {
        List<Player> currentPlayers = players.getValue();
        if (currentPlayers == null || currentPlayers.isEmpty()) {
            return;
        }
        if (rolledDouble) {
            doublesCount++;
            if (doublesCount >= 3) {
                Player jailed = currentPlayers.get(currentPlayerIndex);
                sendToJail(jailed);
                players.setValue(currentPlayers);
                doublesCount = 0;
                currentPlayerIndex = (currentPlayerIndex + 1) % currentPlayers.size();
            }
        } else {
            doublesCount = 0;
            currentPlayerIndex = (currentPlayerIndex + 1) % currentPlayers.size();
        }
        currentTurn.setValue(currentPlayers.get(currentPlayerIndex));
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
        switch (tile.type) {
            case PROPERTY:
                if (!tile.isOwned) {
                    tileMessage.setValue("Landed on " + tile.name + ". Buy for $" + tile.price + "?");
                    purchaseEvent.setValue(new PurchaseEvent(player, tile));
                    return;
                }
                if (tile.ownerId != player.id) {
                    if (tile.mortgaged) {
                        tileMessage.setValue(tile.name + " is mortgaged. No rent due.");
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
                    tileMessage.setValue("Paid $" + rent + " to " + owner.name + " for " + tile.name + ".");
                    players.setValue(currentPlayers);
                } else {
                    tileMessage.setValue("You landed on your own property: " + tile.name + ".");
                }
                break;
            case CHANCE:
                Card chanceCard = chanceDeck.drawCard();
                applyCardEffect(player, chanceCard);
                cardDrawn.setValue(chanceCard);
                break;
            case COMMUNITY_CHEST:
                Card chestCard = communityDeck.drawCard();
                applyCardEffect(player, chestCard);
                cardDrawn.setValue(chestCard);
                break;
            case TAX:
                player.money -= tile.price;
                tileMessage.setValue("Paid $" + tile.price + " in taxes.");
                players.setValue(players.getValue());
                break;
            case GO_TO_JAIL:
                sendToJail(player);
                tileMessage.setValue("Go to Jail! Do not pass GO, do not collect $200.");
                players.setValue(players.getValue());
                break;
            case JAIL:
                if (player.inJail) {
                    tileMessage.setValue("You are in Jail.");
                } else {
                    tileMessage.setValue("Just visiting Jail.");
                }
                players.setValue(players.getValue());
                break;
            case GO:
                tileMessage.setValue("Landed on GO and collected $200.");
                players.setValue(players.getValue());
                break;
            case FREE_PARKING:
                tileMessage.setValue("Free Parking - take a break!");
                players.setValue(players.getValue());
                break;
            default:
                // no action
        }
    }

    public void applyCardEffect(Player player, Card card) {
        List<Player> currentPlayers = players.getValue();
        if (currentPlayers == null) {
            return;
        }

        player.money += card.moneyChange;

        if (card.collectFromEachPlayer > 0) {
            for (Player p : currentPlayers) {
                if (p.id != player.id) {
                    p.money -= card.collectFromEachPlayer;
                    player.money += card.collectFromEachPlayer;
                }
            }
        }

        if (card.payEachPlayer > 0) {
            for (Player p : currentPlayers) {
                if (p.id != player.id) {
                    p.money += card.payEachPlayer;
                    player.money -= card.payEachPlayer;
                }
            }
        }

        if (card.houseRepairCost > 0 || card.hotelRepairCost > 0) {
            int houses = 0;
            int hotels = 0;
            for (Tile t : tileMap.values()) {
                if (t.ownerId == player.id) {
                    if (t.houseCount < 5) {
                        houses += t.houseCount;
                    } else if (t.houseCount == 5) {
                        hotels++;
                    }
                }
            }
            int cost = houses * card.houseRepairCost + hotels * card.hotelRepairCost;
            player.money -= cost;
        }

        if (card.getOutOfJailFree) {
            player.jailFreeCards++;
        }

        if (card.goToJail) {
            sendToJail(player);
        }

        if (card.moveTo != null) {
            movePlayerTo(player, card.moveTo);
        } else if (card.moveBy != null) {
            movePlayerBy(player, card.moveBy);
        } else if (card.advanceToNearestRailroad) {
            int target = findNearestRailroad(player.position);
            boolean passedGo = target < player.position;
            player.position = target;
            if (passedGo) {
                player.money += 200;
            }
            Tile tile = tileMap.get(target);
            if (tile != null) {
                if (tile.isOwned && tile.ownerId != player.id) {
                    int rent = tile.rent[Math.max(0, countOwned(tile.ownerId, "Railroad") - 1)];
                    if (card.payDoubleRent) {
                        rent *= 2;
                    }
                    player.money -= rent;
                    Player owner = getPlayer(tile.ownerId);
                    if (owner != null) {
                        owner.money += rent;
                    }
                } else if (!tile.isOwned) {
                    purchaseEvent.setValue(new PurchaseEvent(player, tile));
                }
            }
        } else if (card.advanceToNearestUtility) {
            int target = findNearestUtility(player.position);
            boolean passedGo = target < player.position;
            player.position = target;
            if (passedGo) {
                player.money += 200;
            }
            Tile tile = tileMap.get(target);
            if (tile != null) {
                if (tile.isOwned && tile.ownerId != player.id) {
                    int rent = 10 * lastRollTotal;
                    player.money -= rent;
                    Player owner = getPlayer(tile.ownerId);
                    if (owner != null) {
                        owner.money += rent;
                    }
                } else if (!tile.isOwned) {
                    purchaseEvent.setValue(new PurchaseEvent(player, tile));
                }
            }
        }

        players.setValue(currentPlayers);
    }

    private void movePlayerTo(Player player, int target) {
        int oldPos = player.position;
        player.position = target;
        if (target < oldPos) {
            player.money += 200;
        }
        Tile tile = tileMap.get(target);
        if (tile != null) {
            processTile(player, tile);
        }
    }

    private void movePlayerBy(Player player, int delta) {
        int target = (player.position + delta) % 40;
        if (target < 0) {
            target += 40;
        }
        player.position = target;
        Tile tile = tileMap.get(target);
        if (tile != null) {
            processTile(player, tile);
        }
    }

    private void sendToJail(Player player) {
        player.position = 10;
        player.inJail = true;
    }

    private int findNearestRailroad(int position) {
        int pos = (position + 1) % 40;
        while (true) {
            Tile t = tileMap.get(pos);
            if (t != null && "Railroad".equals(t.colorGroup)) {
                return pos;
            }
            pos = (pos + 1) % 40;
        }
    }

    private int findNearestUtility(int position) {
        int pos = (position + 1) % 40;
        while (true) {
            Tile t = tileMap.get(pos);
            if (t != null && "Utility".equals(t.colorGroup)) {
                return pos;
            }
            pos = (pos + 1) % 40;
        }
    }

    private Player getPlayer(int id) {
        List<Player> currentPlayers = players.getValue();
        if (currentPlayers == null) {
            return null;
        }
        for (Player p : currentPlayers) {
            if (p.id == id) {
                return p;
            }
        }
        return null;
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
    public void declinePurchase(Tile tile) {
        purchaseEvent.setValue(null);
        startAuction(tile);
    }

    /**
     * Begin an auction for an unowned property. Observers can listen to
     * {@link #auctionTile} to prompt players for bids.
     */
    public void startAuction(Tile tile) {
        auctionTile.setValue(tile);
    }

    /**
     * Complete an auction by assigning the property to the highest bidder and
     * deducting the winning bid from their money.
     */
    public void completeAuction(Player winner, Tile tile, int bid) {
        if (winner != null && tile != null && bid > 0 && winner.money >= bid) {
            winner.money -= bid;
            tile.isOwned = true;
            tile.ownerId = winner.id;
            players.setValue(players.getValue());
            tileMessage.setValue(winner.name + " won " + tile.name + " for $" + bid + ".");
        }
        auctionTile.setValue(null);
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

    /**
     * Trade a set of properties from one player to another for the specified
     * cash amount. Validates that the "from" player owns each property and
     * that the "to" player can afford the cash portion before completing the
     * trade.
     */
    public void trade(Player from, Player to, List<Tile> properties, int cash) {
        if (from == null || to == null || properties == null) {
            return;
        }

        // Ensure the recipient has enough money for the trade
        if (cash < 0 || to.money < cash) {
            return;
        }

        // Ensure all properties are owned by the "from" player
        for (Tile t : properties) {
            if (t.ownerId != from.id) {
                return;
            }
        }

        // Transfer cash
        if (cash > 0) {
            from.money += cash;
            to.money -= cash;
        }

        // Transfer property ownership
        for (Tile t : properties) {
            t.ownerId = to.id;
        }

        players.setValue(players.getValue());
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
