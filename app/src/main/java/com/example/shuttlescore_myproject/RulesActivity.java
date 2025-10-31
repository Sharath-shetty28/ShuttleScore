package com.example.shuttlescore_myproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RulesActivity extends AppCompatActivity {

    private LinearLayout rulesContainer;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        rulesContainer = findViewById(R.id.rulesContainer);
        bottomNav = findViewById(R.id.bottom_nav);

        String rulesContent = getString(R.string.badminton_rules_content);
        String[] rules = rulesContent.trim().split("\n\n");

        displayRules(rules);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_players) {
                Intent intent = new Intent(RulesActivity.this, PlayersActivity.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_new_match) {
                Intent intent = new Intent(RulesActivity.this, NewMatchActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_rules);
    }

    private void displayRules(String[] rulesToDisplay) {
        rulesContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (String rule : rulesToDisplay) {
            String trimmedRule = rule.trim();
            if (trimmedRule.isEmpty()) continue;

            if (trimmedRule.startsWith("@@")) {
                TextView header = new TextView(this);
                header.setText(trimmedRule.substring(2).trim());
                header.setTextColor(Color.parseColor("#66bb6a"));
                header.setTextSize(22);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 24, 0, 12);
                header.setLayoutParams(params);
                rulesContainer.addView(header);
            } else {
                String[] parts = trimmedRule.split("\\|\\|");
                if (parts.length == 2) {
                    View card = inflater.inflate(R.layout.rule_card, rulesContainer, false);
                    TextView title = card.findViewById(R.id.ruleTitle);
                    TextView description = card.findViewById(R.id.ruleDescription);

                    title.setText(parts[0].trim());
                    description.setText(parts[1].trim());

                    rulesContainer.addView(card);
                }
            }
        }
    }
}
