package com.example.shuttlescore_myproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class NewMatchActivity extends AppCompatActivity {

    private RadioGroup rgMatchType;
    private AutoCompleteTextView spScoringFormat, spTeamAPlayer1, spTeamAPlayer2, spTeamBPlayer1, spTeamBPlayer2;
    private TextInputLayout layoutAPlayer2, layoutBPlayer2;
    private Button btnStartMatch;
    private ImageButton btnBack;

    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_match);

        rgMatchType = findViewById(R.id.rg_match_type);
        spScoringFormat = findViewById(R.id.spinner_scoring_format);
        spTeamAPlayer1 = findViewById(R.id.spinner_team_a_player1);
        spTeamAPlayer2 = findViewById(R.id.spinner_team_a_player2);
        spTeamBPlayer1 = findViewById(R.id.spinner_team_b_player1);
        spTeamBPlayer2 = findViewById(R.id.spinner_team_b_player2);
        layoutAPlayer2 = findViewById(R.id.layout_a_player2);
        layoutBPlayer2 = findViewById(R.id.layout_b_player2);
        btnStartMatch = findViewById(R.id.btn_start_match);
        btnBack = findViewById(R.id.btn_back);

        loadPlayers();
        setupScoringFormatSpinner();

        rgMatchType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_doubles) {
                layoutAPlayer2.setVisibility(View.VISIBLE);
                layoutBPlayer2.setVisibility(View.VISIBLE);
            } else {
                layoutAPlayer2.setVisibility(View.GONE);
                layoutBPlayer2.setVisibility(View.GONE);
            }
        });

        btnStartMatch.setOnClickListener(v -> saveMatch());

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadPlayers() {
        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            playerList = dbHelper.getAllPlayers();
        }

        List<String> playerNames = new ArrayList<>();
        for (Player player : playerList) {
            playerNames.add(player.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item_white, playerNames);

        spTeamAPlayer1.setAdapter(adapter);
        spTeamAPlayer2.setAdapter(adapter);
        spTeamBPlayer1.setAdapter(adapter);
        spTeamBPlayer2.setAdapter(adapter);
    }

    private void setupScoringFormatSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scoring_formats, R.layout.dropdown_item_white);
        spScoringFormat.setAdapter(adapter);
    }

    private void saveMatch() {
        boolean isDoubles = rgMatchType.getCheckedRadioButtonId() == R.id.rb_doubles;

        String teamAPlayer1Name = spTeamAPlayer1.getText().toString();
        String teamBPlayer1Name = spTeamBPlayer1.getText().toString();

        if (teamAPlayer1Name.isEmpty() || teamBPlayer1Name.isEmpty() ||
            (isDoubles && (spTeamAPlayer2.getText().toString().isEmpty() || spTeamBPlayer2.getText().toString().isEmpty()))) {
            new AlertDialog.Builder(this)
                .setTitle("Invalid Player Selection")
                .setMessage("Please select all players for the match.")
                .setPositiveButton("OK", null)
                .show();
            return;
        }

        Player teamAPlayer1 = getPlayerByName(teamAPlayer1Name);
        Player teamBPlayer1 = getPlayerByName(teamBPlayer1Name);
        
        if(teamAPlayer1 == null || teamBPlayer1 == null) {
            new AlertDialog.Builder(this)
                .setTitle("Invalid Player")
                .setMessage("One of the selected players could not be found.")
                .setPositiveButton("OK", null)
                .show();
            return;
        }

        String matchType = ((RadioButton) findViewById(rgMatchType.getCheckedRadioButtonId())).getText().toString();
        String scoringFormat = spScoringFormat.getText().toString();


        if (teamAPlayer1.getId() == teamBPlayer1.getId()) {
            new AlertDialog.Builder(this)
                    .setTitle("Invalid Player Selection")
                    .setMessage("The same player cannot be selected on both teams.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        if (matchType.equals("Singles")) {
            if (!teamAPlayer1.getGender().equals(teamBPlayer1.getGender())) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Gender Selection")
                        .setMessage("Players must be of the same gender for a singles match.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        } else { // Doubles
            Player teamAPlayer2 = getPlayerByName(spTeamAPlayer2.getText().toString());
            Player teamBPlayer2 = getPlayerByName(spTeamBPlayer2.getText().toString());
            
            if(teamAPlayer2 == null || teamBPlayer2 == null) {
                new AlertDialog.Builder(this)
                    .setTitle("Invalid Player")
                    .setMessage("One of the selected players could not be found.")
                    .setPositiveButton("OK", null)
                    .show();
                return;
            }

            if (teamAPlayer1.getId() == teamAPlayer2.getId() || teamBPlayer1.getId() == teamBPlayer2.getId()) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Player Selection")
                        .setMessage("The same player cannot be selected twice on the same team.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            if (teamAPlayer1.getId() == teamBPlayer2.getId() || teamAPlayer2.getId() == teamBPlayer1.getId()) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Player Selection")
                        .setMessage("The same player cannot be selected on both teams.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            String gender = teamAPlayer1.getGender();
            if (!gender.equals(teamAPlayer2.getGender()) || !gender.equals(teamBPlayer1.getGender()) || !gender.equals(teamBPlayer2.getGender())) {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid Gender Selection")
                        .setMessage("All players must be of the same gender for a doubles match.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        }

        int teamAPlayer1Id = teamAPlayer1.getId();
        int teamBPlayer1Id = teamBPlayer1.getId();
        int teamAPlayer2Id = 0;
        int teamBPlayer2Id = 0;

        if (matchType.equals("Doubles")) {
            teamAPlayer2Id = getPlayerByName(spTeamAPlayer2.getText().toString()).getId();
            teamBPlayer2Id = getPlayerByName(spTeamBPlayer2.getText().toString()).getId();
        }

        Match match = new Match(0, matchType, scoringFormat, teamAPlayer1Id, teamBPlayer1Id, teamAPlayer2Id, teamBPlayer2Id);

        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            long matchId = dbHelper.addMatch(match);
            if (matchId != -1) {
                Toast.makeText(this, "Match started successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewMatchActivity.this, MainActivity.class);
                String teamAName;
                String teamBName;

                if (matchType.equals("Singles")) {
                    teamAName = teamAPlayer1.getName();
                    teamBName = teamBPlayer1.getName();
                } else { // Doubles
                    String teamAPlayer2Name = getPlayerByName(spTeamAPlayer2.getText().toString()).getName();
                    String teamBPlayer2Name = getPlayerByName(spTeamBPlayer2.getText().toString()).getName();
                    teamAName = teamAPlayer1.getName() + " & " + teamAPlayer2Name;
                    teamBName = teamBPlayer1.getName() + " & " + teamBPlayer2Name;
                }

                intent.putExtra("MATCH_ID", matchId);
                intent.putExtra("PLAYER_A_NAME", teamAName);
                intent.putExtra("PLAYER_B_NAME", teamBName);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error starting match", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private Player getPlayerByName(String name) {
        for (Player player : playerList) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }
}
