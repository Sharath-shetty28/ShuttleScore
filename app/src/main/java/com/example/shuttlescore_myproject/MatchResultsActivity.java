package com.example.shuttlescore_myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MatchResultsActivity extends AppCompatActivity {

    private TextView winnerName, finalScore, game1Score, game2Score, game3Score;
    private ImageButton backButton;
    private Button saveButton;
    private long matchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_results);

        initializeViews();
        displayResults();

        backButton.setOnClickListener(v -> finish());
        saveButton.setOnClickListener(v -> {
            saveMatchResults();
            // Navigate to Match History after saving
            Intent intent = new Intent(MatchResultsActivity.this, MatchHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initializeViews() {
        winnerName = findViewById(R.id.winnerName);
        finalScore = findViewById(R.id.finalScore);
        game1Score = findViewById(R.id.game1Score);
        game2Score = findViewById(R.id.game2Score);
        game3Score = findViewById(R.id.game3Score);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);
    }

    private void displayResults() {
        Intent intent = getIntent();
        matchId = intent.getLongExtra("MATCH_ID", -1);
        String winner = intent.getStringExtra("WINNER_NAME");
        String score = intent.getStringExtra("FINAL_SCORE");
        String games = intent.getStringExtra("GAME_SCORES");

        winnerName.setText(winner);
        finalScore.setText("Final Score: " + score);

        // Clear previous scores and hide text views
        game1Score.setVisibility(View.GONE);
        game2Score.setVisibility(View.GONE);
        game3Score.setVisibility(View.GONE);

        if (games != null && !games.isEmpty()) {
            String[] gameScoresArray = games.split(", ");
            if (gameScoresArray.length > 0) {
                game1Score.setText("Game 1   " + gameScoresArray[0]);
                game1Score.setVisibility(View.VISIBLE);
            }
            if (gameScoresArray.length > 1) {
                game2Score.setText("Game 2   " + gameScoresArray[1]);
                game2Score.setVisibility(View.VISIBLE);
            }
            if (gameScoresArray.length > 2) {
                game3Score.setText("Game 3   " + gameScoresArray[2]);
                game3Score.setVisibility(View.VISIBLE);
            }
        }
    }

    private void saveMatchResults() {
        if (matchId != -1) {
            Intent intent = getIntent();
            String winner = intent.getStringExtra("WINNER_NAME");
            String score = intent.getStringExtra("FINAL_SCORE");
            String games = intent.getStringExtra("GAME_SCORES");

            try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
                dbHelper.updateMatchResults(matchId, winner, score, games);
            }
        }
    }
}
