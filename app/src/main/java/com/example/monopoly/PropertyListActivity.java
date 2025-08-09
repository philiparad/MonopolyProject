package com.example.monopoly;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
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
        refreshProperties();

        viewModel.players.observe(this, players -> refreshProperties());
    }

    private void refreshProperties() {
        LinearLayout container = findViewById(R.id.property_list);
        container.removeAllViews();

        Map<Integer, Tile> tiles = viewModel.getTileMap();
        List<Player> players = viewModel.players.getValue();
        if (tiles == null || players == null) {
            return;
        }

        for (Tile t : tiles.values()) {
            if (!t.isOwned) {
                continue;
            }

            Player owner = null;
            for (Player p : players) {
                if (p.id == t.ownerId) {
                    owner = p;
                    break;
                }
            }
            if (owner == null) {
                continue;
            }

            StringBuilder info = new StringBuilder();
            info.append(t.name)
                .append(" - Owned by: ")
                .append(owner.name)
                .append(", Color: ")
                .append(t.colorGroup)
                .append(", Rent: ");
            for (int i = 0; i < t.rent.length; i++) {
                info.append(t.rent[i]);
                if (i < t.rent.length - 1) {
                    info.append("/");
                }
            }
            info.append(", House Cost: ")
                .append(t.houseCost)
                .append(", Mortgage: ")
                .append(t.mortgageValue)
                .append(", Houses: ")
                .append(t.houseCount)
                .append(", Mortgaged: ")
                .append(t.mortgaged);

            TextView tv = new TextView(this);
            tv.setText(info.toString());
            container.addView(tv);

            LinearLayout buttons = new LinearLayout(this);
            buttons.setOrientation(LinearLayout.HORIZONTAL);

            Button mortgageBtn = new Button(this);
            mortgageBtn.setText("Mortgage");
            mortgageBtn.setEnabled(!t.mortgaged);
            Player finalOwner = owner;
            mortgageBtn.setOnClickListener(v -> {
                viewModel.mortgageProperty(finalOwner, t);
                refreshProperties();
            });
            buttons.addView(mortgageBtn);

            Button unmortgageBtn = new Button(this);
            unmortgageBtn.setText("Unmortgage");
            unmortgageBtn.setEnabled(t.mortgaged);
            unmortgageBtn.setOnClickListener(v -> {
                viewModel.unmortgageProperty(finalOwner, t);
                refreshProperties();
            });
            buttons.addView(unmortgageBtn);

            container.addView(buttons);
        }
    }
}