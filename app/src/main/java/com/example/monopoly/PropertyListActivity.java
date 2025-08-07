package com.example.monopoly;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import java.util.Map;

public class PropertyListActivity extends AppCompatActivity {
    private GameViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_list);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        TextView output = findViewById(R.id.property_output);

        Map<Integer, Tile> tiles = viewModel.getTileMap();
        List<Player> players = viewModel.players.getValue();

        StringBuilder sb = new StringBuilder();
        for (Tile t : tiles.values()) {
            if (t.isOwned) {
                String owner = "Player " + t.ownerId;
                for (Player p : players) {
                    if (p.id == t.ownerId) {
                        owner = p.name;
                        break;
                    }
                }
                sb.append(t.name)
                  .append(" - Owned by: ")
                  .append(owner)
                  .append(", Houses: ")
                  .append(t.houseCount)
                  .append("\n");
            }
        }
        output.setText(sb.toString());
    }
}