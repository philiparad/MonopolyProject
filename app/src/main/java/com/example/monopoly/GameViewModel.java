public void processTile(Player player, Tile tile) {
    if (tile.type == TileType.PROPERTY && tile.isOwned && tile.ownerId != player.id) {
        int rent = 10 + 5 * tile.houseCount;
        player.money -= rent;
        for (Player p : players.getValue()) {
            if (p.id == tile.ownerId) {
                p.money += rent;
                db.playerDao().update(p);
                break;
            }
        }
        db.playerDao().update(player);
        players.setValue(db.playerDao().getAll());
    }
}

public void upgradeHouse(int playerId, Tile tile) {
    if (tile.ownerId == playerId && tile.houseCount < 5) {
        tile.houseCount++;
        for (Player p : players.getValue()) {
            if (p.id == playerId && p.money >= 50) {
                p.money -= 50;
                db.playerDao().update(p);
                break;
            }
        }
    }
    players.setValue(db.playerDao().getAll());
}