package com.example.shuttlescore_myproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.MatchHistoryViewHolder> {

    private Context context;
    private ArrayList<Match> matchHistoryList;

    public MatchHistoryAdapter(Context context, ArrayList<Match> matchHistoryList) {
        this.context = context;
        this.matchHistoryList = matchHistoryList;
    }

    @NonNull
    @Override
    public MatchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_match_history, parent, false);
        return new MatchHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchHistoryViewHolder holder, int position) {
        Match match = matchHistoryList.get(position);
        if (match == null) {
            return; // Don't do anything if the match data is missing
        }

        holder.matchNumber.setText("Match " + (position + 1));
        holder.matchType.setText(match.getMatchType());

        // Determine the result based on the winner
        if (match.getWinnerName() != null && !match.getWinnerName().isEmpty()) {
            holder.matchResult.setText("Won");
            holder.matchResult.setTextColor(Color.parseColor("#00FF00"));
        } else {
            holder.matchResult.setText("-"); // Display a dash if there is no winner
            holder.matchResult.setTextColor(Color.WHITE);
        }

        String gameScores = match.getGameScores();
        if (gameScores != null && !gameScores.isEmpty()) {
            holder.matchScores.setText(gameScores);
        } else {
            holder.matchScores.setText("-"); // Provide a default value for missing scores
        }
    }

    @Override
    public int getItemCount() {
        return matchHistoryList != null ? matchHistoryList.size() : 0;
    }

    public class MatchHistoryViewHolder extends RecyclerView.ViewHolder {

        TextView matchNumber, matchType, matchResult, matchScores;

        public MatchHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            matchNumber = itemView.findViewById(R.id.matchNumber);
            matchType = itemView.findViewById(R.id.matchType);
            matchResult = itemView.findViewById(R.id.matchResult);
            matchScores = itemView.findViewById(R.id.matchScores);
        }
    }
}
