package com.example.shuttlescore_myproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchHistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private MatchHistoryAdapter matchHistoryAdapter;
    private ArrayList<Match> matchHistoryList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_history);

        dbHelper = new DatabaseHelper(this);
        matchHistoryList = dbHelper.getAllMatches();

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        matchHistoryAdapter = new MatchHistoryAdapter(this, matchHistoryList);
        historyRecyclerView.setAdapter(matchHistoryAdapter);
    }
}
