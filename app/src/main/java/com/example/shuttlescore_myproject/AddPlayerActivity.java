package com.example.shuttlescore_myproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        EditText etPlayerName = findViewById(R.id.et_player_name);
        EditText etTeamName = findViewById(R.id.et_team_name);
        RadioGroup rgGender = findViewById(R.id.rg_gender);
        Button btnSave = findViewById(R.id.btn_save);
        ImageButton btnBack = findViewById(R.id.btn_back);

        btnSave.setOnClickListener(v -> {
            String name = etPlayerName.getText().toString().trim();
            String team = etTeamName.getText().toString().trim();

            int selectedGenderId = rgGender.getCheckedRadioButtonId();
            RadioButton selectedGender = findViewById(selectedGenderId);
            String gender = selectedGender != null ? selectedGender.getText().toString() : "";

            if (name.isEmpty() || team.isEmpty() || gender.isEmpty()) {
                Toast.makeText(AddPlayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Player player = new Player(0, name, team, gender);
            long result;
            try (DatabaseHelper dbHelper = new DatabaseHelper(AddPlayerActivity.this)) {
                result = dbHelper.addPlayer(player);
            }

            if (result != -1) {
                Toast.makeText(AddPlayerActivity.this, "Player added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddPlayerActivity.this, "Error adding player", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}
