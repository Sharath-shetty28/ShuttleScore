package com.example.shuttlescore_myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PlayersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;
    private ImageButton btnAddPlayer;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        recyclerView = findViewById(R.id.rv_players);
        btnAddPlayer = findViewById(R.id.btn_add_player);
        bottomNav = findViewById(R.id.bottom_nav);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        playerList = new ArrayList<>();
        playerAdapter = new PlayerAdapter(this, playerList);
        recyclerView.setAdapter(playerAdapter);

        btnAddPlayer.setOnClickListener(v -> {
            Intent intent = new Intent(PlayersActivity.this, AddPlayerActivity.class);
            startActivity(intent);
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_players) {
                // Already on the Players screen, do nothing
                return true;
            } else if (itemId == R.id.nav_new_match) {
                Intent intent = new Intent(PlayersActivity.this, NewMatchActivity.class);
                startActivity(intent);
                return true;
//            }  else if (itemId == R.id.nav_history) {
//                Intent intent = new Intent(PlayersActivity.this, HistoryActivity.class);
//                startActivity(intent);
//                return true;
          } else if (itemId == R.id.nav_rules) {
                Intent intent = new Intent(PlayersActivity.this, RulesActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set the "Players" icon as active every time the activity resumes
        bottomNav.setSelectedItemId(R.id.nav_players);
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            playerList.clear();
            playerList.addAll(dbHelper.getAllPlayers());
            playerAdapter.notifyDataSetChanged();
        }
    }
}
