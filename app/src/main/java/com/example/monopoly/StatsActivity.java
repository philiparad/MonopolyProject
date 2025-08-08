package com.example.monopoly;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class StatsActivity extends AppCompatActivity {
    private GameViewModel viewModel;
    private TextView statsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        statsText = findViewById(R.id.stats_text);

        updateStats(viewModel.players.getValue());
        viewModel.players.observe(this, this::updateStats);
    }

    private void updateStats(List<Player> players) {
        if (players == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Player p : players) {
            sb.append(p.name).append(" at tile ").append(p.position).append("\n");
        }
        statsText.setText(sb.toString());
    }
}