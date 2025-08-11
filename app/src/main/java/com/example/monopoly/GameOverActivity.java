package com.example.monopoly;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        String winner = getIntent().getStringExtra("winner");
        TextView text = findViewById(R.id.winner_text);
        if (winner != null) {
            text.setText(winner + " wins!");
        }
    }
}
