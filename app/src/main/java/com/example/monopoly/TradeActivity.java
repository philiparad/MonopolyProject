package com.example.monopoly;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeActivity extends AppCompatActivity {
    private GameViewModel viewModel;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private LinearLayout propertyContainer;
    private EditText cashInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        fromSpinner = findViewById(R.id.spinner_from);
        toSpinner = findViewById(R.id.spinner_to);
        propertyContainer = findViewById(R.id.property_container);
        cashInput = findViewById(R.id.cash_input);
        Button tradeButton = findViewById(R.id.button_trade);

        List<Player> players = viewModel.players.getValue();
        if (players == null) {
            players = new ArrayList<>();
        }
        ArrayAdapter<Player> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, players);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        viewModel.players.observe(this, updated -> {
            adapter.clear();
            if (updated != null) {
                adapter.addAll(updated);
            }
            adapter.notifyDataSetChanged();
            refreshProperties(getSelectedFrom());
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                refreshProperties(getSelectedFrom());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tradeButton.setOnClickListener(v -> {
            Player from = getSelectedFrom();
            Player to = getSelectedTo();
            if (from == null || to == null || from.id == to.id) {
                return;
            }
            List<Tile> selected = new ArrayList<>();
            for (int i = 0; i < propertyContainer.getChildCount(); i++) {
                View child = propertyContainer.getChildAt(i);
                if (child instanceof CheckBox) {
                    CheckBox cb = (CheckBox) child;
                    if (cb.isChecked()) {
                        selected.add((Tile) cb.getTag());
                    }
                }
            }
            int cash = 0;
            String text = cashInput.getText().toString().trim();
            if (!text.isEmpty()) {
                cash = Integer.parseInt(text);
            }
            viewModel.trade(from, to, selected, cash);
            finish();
        });

        refreshProperties(getSelectedFrom());
    }

    private Player getSelectedFrom() {
        return (Player) fromSpinner.getSelectedItem();
    }

    private Player getSelectedTo() {
        return (Player) toSpinner.getSelectedItem();
    }

    private void refreshProperties(Player owner) {
        propertyContainer.removeAllViews();
        if (owner == null) {
            return;
        }
        Map<Integer, Tile> tiles = viewModel.getTileMap();
        for (Tile t : tiles.values()) {
            if (t.ownerId == owner.id) {
                CheckBox cb = new CheckBox(this);
                cb.setText(t.name);
                cb.setTag(t);
                propertyContainer.addView(cb);
            }
        }
    }
}
