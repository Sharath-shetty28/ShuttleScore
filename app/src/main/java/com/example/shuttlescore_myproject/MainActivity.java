package com.example.shuttlescore_myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // --- UI Elements ---
    private TextView scorePlayerA, scorePlayerB;
    private TextView gamesWonA, gamesWonB;
    private View servingIndicatorA, servingIndicatorB;
    private TextView textViewPlayerA, textViewPlayerB;
    // Main controls
    private ImageButton buttonAdd, buttonSubtract, buttonReset;
    // New individual penalty buttons
    private ImageButton buttonPenaltyA, buttonPenaltyB;

    // --- BWF Match State Variables ---
    private long matchId;
    private int playerAScore = 0;
    private int playerBScore = 0;
    private int playerAGamesWon = 0;
    private int playerBGamesWon = 0;
    private ArrayList<String> gameScores = new ArrayList<>();

    // Player 1 is Player A, Player 2 is Player B
    private int servingPlayer = 1;
    private int initialServerForCurrentGame = 1;

    private boolean isGameOver = false;
    private boolean isMatchOver = false;

    // --- Constants ---
    private static final int WINNING_SCORE = 21;
    private static final int MATCH_GAMES_TO_WIN = 2; // Best of 3 games

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();

        Intent intent = getIntent();
        matchId = intent.getLongExtra("MATCH_ID", -1);
        String playerAName = intent.getStringExtra("PLAYER_A_NAME");
        String playerBName = intent.getStringExtra("PLAYER_B_NAME");

        if (playerAName != null && !playerAName.isEmpty()) {
            textViewPlayerA.setText(playerAName);
        }

        if (playerBName != null && !playerBName.isEmpty()) {
            textViewPlayerB.setText(playerBName);
        }

        resetMatch(); // Start the match from a clean state
    }

    private void initializeViews() {
        scorePlayerA = findViewById(R.id.scorePlayerA);
        scorePlayerB = findViewById(R.id.scorePlayerB);
        gamesWonA = findViewById(R.id.gamesWonA);
        gamesWonB = findViewById(R.id.gamesWonB);
        servingIndicatorA = findViewById(R.id.servingIndicatorA);
        servingIndicatorB = findViewById(R.id.servingIndicatorB);
        buttonAdd = findViewById(R.id.button_add);
        buttonSubtract = findViewById(R.id.button_subtract);
        buttonReset = findViewById(R.id.button_reset);
        textViewPlayerA = findViewById(R.id.textViewPlayerA);
        textViewPlayerB = findViewById(R.id.textViewPlayerB);

        // Initialize new penalty buttons
        buttonPenaltyA = findViewById(R.id.button_penalty_A);
        buttonPenaltyB = findViewById(R.id.button_penalty_B);
    }

    private void setupClickListeners() {
        buttonAdd.setOnClickListener(v -> showAssignActionDialog(true, "Assign Point"));
        buttonSubtract.setOnClickListener(v -> showAssignActionDialog(false, "Subtract Point"));
        buttonReset.setOnClickListener(v -> handleResetClick());

        // Set listeners for individual penalty buttons
        buttonPenaltyA.setOnClickListener(v -> showPenaltyCardSelectionDialog(1));
        buttonPenaltyB.setOnClickListener(v -> showPenaltyCardSelectionDialog(2));
    }

    private void awardPointTo(int player) {
        if (isMatchOver || isGameOver) return;

        if (player == 1) {
            playerAScore++;
        } else {
            playerBScore++;
        }

        servingPlayer = player;
        checkForGameWin();
        updateUI();
    }

    private void checkForGameWin() {
        boolean gameWon = false;

        if (playerAScore >= WINNING_SCORE && (playerAScore - playerBScore) >= 2) {
            playerAGamesWon++;
            gameWon = true;
        } else if (playerBScore >= WINNING_SCORE && (playerBScore - playerAScore) >= 2) {
            playerBGamesWon++;
            gameWon = true;
        } else if (playerAScore == 30 && playerBScore == 29) {
            playerAGamesWon++;
            gameWon = true;
        } else if (playerBScore == 30 && playerAScore == 29) {
            playerBGamesWon++;
            gameWon = true;
        }

        if (gameWon) {
            gameScores.add(playerAScore + " - " + playerBScore);
            isGameOver = true;
            initialServerForCurrentGame = (playerAGamesWon > playerBGamesWon) ? 1 : 2;
            checkForMatchWin();
        }
    }

    private void checkForMatchWin() {
        if (playerAGamesWon == MATCH_GAMES_TO_WIN || playerBGamesWon == MATCH_GAMES_TO_WIN) {
            if (!isMatchOver) { // Prevent multiple navigations
                isMatchOver = true;
                navigateToMatchResults();
            }
        }
    }

    private void navigateToMatchResults() {
        Intent intent = new Intent(MainActivity.this, MatchResultsActivity.class);

        String winnerName = playerAGamesWon > playerBGamesWon ? textViewPlayerA.getText().toString() : textViewPlayerB.getText().toString();
        String finalScore = playerAGamesWon + " - " + playerBGamesWon;
        String gameScoresString = String.join(", ", gameScores);

        intent.putExtra("MATCH_ID", matchId);
        intent.putExtra("WINNER_NAME", winnerName);
        intent.putExtra("FINAL_SCORE", finalScore);
        intent.putExtra("GAME_SCORES", gameScoresString);

        startActivity(intent);
        finish(); // Finish MainActivity so the user can't go back to a finished match
    }

    private void showAssignActionDialog(boolean isIncrementing, String title) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.dialog_assign_action, (LinearLayout) findViewById(R.id.dialog_container));

        TextView dialogTitle = bottomSheetView.findViewById(R.id.dialogTitle);
        dialogTitle.setText(title);

        bottomSheetView.findViewById(R.id.buttonTeamA).setOnClickListener(v -> {
            if (isIncrementing) awardPointTo(1); else undoPoint(1);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.buttonTeamB).setOnClickListener(v -> {
            if (isIncrementing) awardPointTo(2); else undoPoint(2);
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.buttonCancel).setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void undoPoint(int player) {
        if (player == 1) {
            if (playerAScore > 0) playerAScore--;
        } else {
            if (playerBScore > 0) playerBScore--;
        }
        updateUI();
    }

    private void handleResetClick() {
        if (isMatchOver || (playerAScore == 0 && playerBScore == 0 && playerAGamesWon == 0 && playerBGamesWon == 0)) {
            resetMatch();
        } else if (isGameOver) {
            startNextGame();
        } else {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Reset Match?")
                    .setMessage("Are you sure you want to reset all scores and start a new match?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Reset", (dialog, which) -> resetMatch())
                    .show();
        }
    }

    private void resetMatch() {
        playerAScore = 0;
        playerBScore = 0;
        playerAGamesWon = 0;
        playerBGamesWon = 0;
        servingPlayer = 1;
        initialServerForCurrentGame = 1;
        isGameOver = false;
        isMatchOver = false;
        gameScores.clear();
        updateUI();
    }

    private void startNextGame() {
        playerAScore = 0;
        playerBScore = 0;
        servingPlayer = initialServerForCurrentGame;
        isGameOver = false;
        isMatchOver = false;
        updateUI();
    }

    private void updateUI() {
        scorePlayerA.setText(String.valueOf(playerAScore));
        scorePlayerB.setText(String.valueOf(playerBScore));
        gamesWonA.setText(String.valueOf(playerAGamesWon));
        gamesWonB.setText(String.valueOf(playerBGamesWon));

        if (servingPlayer == 1) {
            servingIndicatorA.setVisibility(View.VISIBLE);
            servingIndicatorB.setVisibility(View.INVISIBLE);
            scorePlayerA.setTextColor(Color.parseColor("#38E07B"));
            scorePlayerB.setTextColor(Color.WHITE);
        } else {
            servingIndicatorA.setVisibility(View.INVISIBLE);
            servingIndicatorB.setVisibility(View.VISIBLE);
            scorePlayerB.setTextColor(Color.parseColor("#38E07B"));
            scorePlayerA.setTextColor(Color.WHITE);
        }
    }

    // --- Penalty Logic (SIMPLIFIED) ---

    private void showPenaltyCardSelectionDialog(int penalizedPlayer) {
        final String[] cards = {"Yellow Card (Warning)", "Red Card (Award Point to Opponent)", "Black Card (Disqualify)"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Select Penalty for Player " + (penalizedPlayer == 1 ? "A" : "B"))
                .setItems(cards, (dialog, which) -> {
                    switch (which) {
                        case 0: issueYellowCard(penalizedPlayer); break;
                        case 1: issueRedCard(penalizedPlayer); break;
                        case 2: issueBlackCard(penalizedPlayer); break;
                    }
                })
                .show();
    }

    private void issueYellowCard(int penalizedPlayer) {
        String playerName = (penalizedPlayer == 1) ? textViewPlayerA.getText().toString() : textViewPlayerB.getText().toString();
        new MaterialAlertDialogBuilder(this)
                .setTitle("Warning Issued")
                .setMessage(playerName + " has received a Yellow Card.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void issueRedCard(int penalizedPlayer) {
        if (isMatchOver) return;
        int opponent = (penalizedPlayer == 1) ? 2 : 1;
        awardPointTo(opponent);

        String penalizedName = (penalizedPlayer == 1) ? textViewPlayerA.getText().toString() : textViewPlayerB.getText().toString();
        String opponentName = (opponent == 1) ? textViewPlayerA.getText().toString() : textViewPlayerB.getText().toString();
        new MaterialAlertDialogBuilder(this)
                .setTitle("Red Card Issued")
                .setMessage(penalizedName + " receives a fault. A point has been awarded to " + opponentName + ".")
                .setPositiveButton("OK", null)
                .show();
    }

    private void issueBlackCard(int penalizedPlayer) {
        if (isMatchOver) return;

        int winner = (penalizedPlayer == 1) ? 2 : 1;
        if (winner == 1) {
            playerAGamesWon = MATCH_GAMES_TO_WIN;
        } else {
            playerBGamesWon = MATCH_GAMES_TO_WIN;
        }

        isMatchOver = true;
        updateUI();
        navigateToMatchResults();
    }
}
