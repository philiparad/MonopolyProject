package com.example.monopoly;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualBoardActivity extends AppCompatActivity {
    private GameViewModel viewModel;
    private GridLayout grid;
    private final Map<Integer, ImageView> playerTokens = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_board);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
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
            grid.addView(tileView);
        }

        addPlayerTokens(viewModel.players.getValue());
        viewModel.players.observe(this, this::addPlayerTokens);

        viewModel.purchaseEvent.observe(this, event -> {
            if (event != null) {
                showPurchaseDialog(event);
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
                grid.addView(token);
                playerTokens.put(p.id, token);
            }
            moveToken(token, p.position);
        }
    }

    private void moveToken(ImageView token, int position) {
        GridLayout.LayoutParams params = (GridLayout.LayoutParams) token.getLayoutParams();
        if (params == null) {
            params = new GridLayout.LayoutParams();
        }
        params.rowSpec = GridLayout.spec(position / 10);
        params.columnSpec = GridLayout.spec(position % 10);
        token.setLayoutParams(params);
    }

    private void showPurchaseDialog(GameViewModel.PurchaseEvent event) {
        new AlertDialog.Builder(this)
                .setTitle("Purchase Property")
                .setMessage("Buy " + event.tile.name + " for $" + event.tile.price + "?")
                .setPositiveButton("Buy", (dialog, which) ->
                        viewModel.buyProperty(event.player, event.tile))
                .setNegativeButton("Cancel", (dialog, which) -> viewModel.declinePurchase())
                .show();
    }
}