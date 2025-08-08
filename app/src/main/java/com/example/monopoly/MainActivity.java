package com.example.monopoly;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_game:
                    startActivity(new Intent(this, VisualBoardActivity.class));
                    return true;
                case R.id.navigation_stats:
                    startActivity(new Intent(this, StatsActivity.class));
                    return true;
                case R.id.navigation_settings:
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }
}