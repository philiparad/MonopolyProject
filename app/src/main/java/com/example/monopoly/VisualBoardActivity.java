package com.example.monopoly;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.Map;

public class VisualBoardActivity extends AppCompatActivity {
    private GameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_board);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        ViewGroup grid = findViewById(R.id.visual_board);

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
            grid.addView(tileView);
        }
    }
}