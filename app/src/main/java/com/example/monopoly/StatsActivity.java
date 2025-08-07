package com.example.monopoly;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class StatsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        TextView statsText = findViewById(R.id.stats_text);

        StringBuilder sb = new StringBuilder();
        for (Player p : viewModel.players.getValue()) {
            sb.append(p.name).append(" at tile ").append(p.position).append("\n");
        }
        statsText.setText(sb.toString());
    }
}