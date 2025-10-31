package com.example.shuttlescore_myproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private List<Player> playerList;
    private DatabaseHelper dbHelper;

    public PlayerAdapter(Context context, List<Player> playerList) {
        this.context = context;
        this.playerList = playerList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.playerName.setText(player.getName());
        holder.teamName.setText(player.getTeam());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPlayerActivity.class);
            intent.putExtra("PLAYER_ID", player.getId());
            intent.putExtra("PLAYER_NAME", player.getName());
            intent.putExtra("PLAYER_TEAM", player.getTeam());
            intent.putExtra("PLAYER_GENDER", player.getGender());
            context.startActivity(intent);
        });

        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deletePlayer(player);
            playerList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView playerName, teamName;
        ImageButton editButton, deleteButton;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.tv_player_name);
            teamName = itemView.findViewById(R.id.tv_team_name);
            editButton = itemView.findViewById(R.id.btn_edit_player);
            deleteButton = itemView.findViewById(R.id.btn_delete_player);
        }
    }
}
