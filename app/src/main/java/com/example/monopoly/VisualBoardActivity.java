package com.example.monopoly;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualBoardActivity extends AppCompatActivity {
    private GameViewModel viewModel;
    private GridLayout grid;
    private FrameLayout boardContainer;
    private final Map<Integer, ImageView> playerTokens = new HashMap<>();
    private final Map<Integer, View> tileViews = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_board);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        boardContainer = findViewById(R.id.board_container);
        grid = findViewById(R.id.visual_board);

        Map<Integer, Tile> tiles = viewModel.getTileMap();
        for (int i = 0; i < 40; i++) {
            Tile t = tiles.get(i);
            ImageView tileView = new ImageView(this);
            if (t.type == TileType.PROPERTY) {
                tileView.setImageResource(R.drawable.tile_property);
            } else if (t.type == TileType.CHANCE) {
                tileView.setImageResource(R.drawable.tile_chance);
            } else {
                tileView.setImageResource(R.drawable.tile_property);
            }
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i / 10);
            params.columnSpec = GridLayout.spec(i % 10);
            tileView.setLayoutParams(params);
            final int index = i;
            tileView.setOnClickListener(v -> showTileDialog(index));
            grid.addView(tileView);
            tileViews.put(index, tileView);
        }

        addPlayerTokens(viewModel.players.getValue());
        viewModel.players.observe(this, this::addPlayerTokens);

        viewModel.purchaseEvent.observe(this, event -> {
            if (event != null) {
                showPurchaseDialog(event);
            }
        });

        viewModel.auctionTile.observe(this, tile -> {
            if (tile != null) {
                showAuctionDialog(tile);
            }
        });

        viewModel.cardDrawn.observe(this, card -> {
            if (card != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Card Drawn")
                        .setMessage(card.description)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        viewModel.tileMessage.observe(this, msg -> {
            if (msg != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Tile")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        viewModel.currentTurn.observe(this, player -> {
            if (player != null) {
                Toast.makeText(this, player.name + "'s turn", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.gameOver.observe(this, winner -> {
            if (winner != null) {
                Intent intent = new Intent(this, GameOverActivity.class);
                intent.putExtra("winner", winner.name);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addPlayerTokens(List<Player> players) {
        if (players == null) {
            return;
        }
        for (Player p : players) {
            ImageView token = playerTokens.get(p.id);
            if (token == null) {
                token = new ImageView(this);
                token.setImageResource(R.drawable.icon_player);
                int size = (int) (24 * getResources().getDisplayMetrics().density);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
                boardContainer.addView(token, lp);
                playerTokens.put(p.id, token);
                final int pos = p.position;
                boardContainer.post(() -> {
                    View tileView = tileViews.get(pos);
                    if (tileView != null) {
                        token.setX(tileView.getX());
                        token.setY(tileView.getY());
                    }
                });
            } else {
                moveToken(token, p.position);
            }
        }
    }

    private void moveToken(ImageView token, int position) {
        View tileView = tileViews.get(position);
        if (tileView == null) {
            return;
        }
        float targetX = tileView.getX();
        float targetY = tileView.getY();
        token.animate().x(targetX).y(targetY).setDuration(300).start();
    }

    private void showAuctionDialog(Tile tile) {
        List<Player> bidders = viewModel.players.getValue();
        if (bidders == null) {
            return;
        }
        promptBid(tile, bidders, 0, 0, null);
    }

    private void promptBid(Tile tile, List<Player> bidders, int index, int highestBid, Player highestBidder) {
        if (index >= bidders.size()) {
            if (highestBidder != null) {
                viewModel.completeAuction(highestBidder, tile, highestBid);
            } else {
                viewModel.completeAuction(null, tile, 0);
            }
            return;
        }
        Player bidder = bidders.get(index);
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(this)
                .setTitle("Auction: " + tile.name)
                .setMessage(bidder.name + ", enter bid or leave blank to pass")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) -> {
                    String text = input.getText().toString().trim();
                    if (!text.isEmpty()) {
                        int bid = Integer.parseInt(text);
                        if (bid <= bidder.money && bid > highestBid) {
                            promptBid(tile, bidders, index + 1, bid, bidder);
                        } else {
                            promptBid(tile, bidders, index + 1, highestBid, highestBidder);
                        }
                    } else {
                        promptBid(tile, bidders, index + 1, highestBid, highestBidder);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showPurchaseDialog(GameViewModel.PurchaseEvent event) {
        new AlertDialog.Builder(this)
                .setTitle("Purchase Property")
                .setMessage("Buy " + event.tile.name + " for $" + event.tile.price + "?")
                .setPositiveButton("Buy", (dialog, which) ->
                        viewModel.buyProperty(event.player, event.tile))
                .setNegativeButton("Cancel", (dialog, which) -> viewModel.declinePurchase(event.tile))
                .show();
    }

    private void showTileDialog(int index) {
        Tile tile = viewModel.getTileMap().get(index);
        if (tile == null) {
            return;
        }
        Player current = viewModel.currentTurn.getValue();
        String ownerName = "Unowned";
        List<Player> players = viewModel.players.getValue();
        if (tile.isOwned && players != null) {
            for (Player p : players) {
                if (p.id == tile.ownerId) {
                    ownerName = p.name;
                    break;
                }
            }
        }
        String message = "Price: $" + tile.price + "\nOwner: " + ownerName;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(tile.name)
                .setMessage(message)
                .setNegativeButton("Close", null);

        boolean onCurrentTile = current != null && current.position == index;
        if (tile.type == TileType.PROPERTY && onCurrentTile && current != null) {
            if (!tile.isOwned) {
                builder.setPositiveButton("Buy", (d, w) -> viewModel.buyProperty(current, tile));
            } else if (tile.ownerId == current.id) {
                if (tile.mortgaged) {
                    builder.setPositiveButton("Unmortgage", (d, w) -> viewModel.unmortgageProperty(current, tile));
                } else {
                    builder.setPositiveButton("Mortgage", (d, w) -> viewModel.mortgageProperty(current, tile));
                    builder.setNeutralButton("Upgrade", (d, w) -> viewModel.upgradeHouse(current.id, tile));
                }
            }
        }
        builder.show();
    }
}