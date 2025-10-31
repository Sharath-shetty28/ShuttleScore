package com.example.shuttlescore_myproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditPlayerActivity extends AppCompatActivity {

    private EditText etPlayerName, etTeamName;
    private RadioGroup rgGender;
    private Button btnSaveChanges;
    private ImageButton btnBack;

    private int playerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_player);

        etPlayerName = findViewById(R.id.et_player_name);
        etTeamName = findViewById(R.id.et_team_name);
        rgGender = findViewById(R.id.rg_gender);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnBack = findViewById(R.id.btn_back);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerId = extras.getInt("PLAYER_ID");
            etPlayerName.setText(extras.getString("PLAYER_NAME"));
            etTeamName.setText(extras.getString("PLAYER_TEAM"));
            String gender = extras.getString("PLAYER_GENDER");
            if (gender != null) {
                if (gender.equals("Male")) {
                    rgGender.check(R.id.rb_male);
                } else if (gender.equals("Female")) {
                    rgGender.check(R.id.rb_female);
                }
            }
        }

        btnSaveChanges.setOnClickListener(v -> saveChanges());

        btnBack.setOnClickListener(v -> finish());
    }

    private void saveChanges() {
        String name = etPlayerName.getText().toString().trim();
        String team = etTeamName.getText().toString().trim();

        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";

        if (name.isEmpty() || team.isEmpty() || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Player player = new Player(playerId, name, team, gender);

        try (DatabaseHelper dbHelper = new DatabaseHelper(this)) {
            int result = dbHelper.updatePlayer(player);
            if (result > 0) {
                Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error saving changes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
