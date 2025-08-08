package com.example.monopoly;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, VisualBoardActivity.class);
        startActivity(intent);
    }

    public void showProperties(View view) {
        Intent intent = new Intent(this, PropertyListActivity.class);
        startActivity(intent);
    }

    public void showBoard(View view) {
        Intent intent = new Intent(this, VisualBoardActivity.class);
        startActivity(intent);
    }
}