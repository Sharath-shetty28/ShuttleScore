package com.example.shuttlescore_myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4; // Incremented database version
    private static final String DATABASE_NAME = "UserManager.db";
    private static final String TABLE_USER = "user";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    private static final String TABLE_PLAYER = "player";
    private static final String KEY_PLAYER_ID = "id";
    private static final String KEY_PLAYER_NAME = "name";
    private static final String KEY_PLAYER_TEAM = "team";
    private static final String KEY_PLAYER_GENDER = "gender";

    private static final String TABLE_MATCH = "match";
    private static final String KEY_MATCH_ID = "id";
    private static final String KEY_MATCH_TYPE = "match_type";
    private static final String KEY_SCORING_FORMAT = "scoring_format";
    private static final String KEY_PLAYER1_ID = "player1_id";
    private static final String KEY_PLAYER2_ID = "player2_id";
    private static final String KEY_PLAYER3_ID = "player3_id";
    private static final String KEY_PLAYER4_ID = "player4_id";
    private static final String KEY_WINNER_NAME = "winner_name";
    private static final String KEY_FINAL_SCORE = "final_score";
    private static final String KEY_GAME_SCORES = "game_scores";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_PLAYER_TABLE = "CREATE TABLE " + TABLE_PLAYER + "("
                + KEY_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PLAYER_NAME + " TEXT,"
                + KEY_PLAYER_TEAM + " TEXT,"
                + KEY_PLAYER_GENDER + " TEXT" + ")";
        db.execSQL(CREATE_PLAYER_TABLE);

        String CREATE_MATCH_TABLE = "CREATE TABLE " + TABLE_MATCH + "("
                + KEY_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_MATCH_TYPE + " TEXT,"
                + KEY_SCORING_FORMAT + " TEXT,"
                + KEY_PLAYER1_ID + " INTEGER,"
                + KEY_PLAYER2_ID + " INTEGER,"
                + KEY_PLAYER3_ID + " INTEGER,"
                + KEY_PLAYER4_ID + " INTEGER,"
                + KEY_WINNER_NAME + " TEXT,"
                + KEY_FINAL_SCORE + " TEXT,"
                + KEY_GAME_SCORES + " TEXT" + ")";
        db.execSQL(CREATE_MATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        onCreate(db);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_NAME, user.getUserName());
        contentValues.put(KEY_EMAIL, user.getEmail());
        contentValues.put(KEY_PASSWORD, user.getPassword());
        long success = db.insert(TABLE_USER, null, contentValues);
        db.close();
        return success;
    }

    public User checkUser(String email, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER,
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},
                KEY_EMAIL + " = ?",
                new String[]{email},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            User user = new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            if (user.getPassword().equals(pass)) {
                cursor.close();
                return user;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public long addPlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLAYER_NAME, player.getName());
        contentValues.put(KEY_PLAYER_TEAM, player.getTeam());
        contentValues.put(KEY_PLAYER_GENDER, player.getGender());
        long success = db.insert(TABLE_PLAYER, null, contentValues);
        db.close();
        return success;
    }

    public List<Player> getAllPlayers() {
        List<Player> playerList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Player player = new Player(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playerList;
    }

    public int updatePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_NAME, player.getName());
        values.put(KEY_PLAYER_TEAM, player.getTeam());
        values.put(KEY_PLAYER_GENDER, player.getGender());

        return db.update(TABLE_PLAYER, values, KEY_PLAYER_ID + " = ?",
                new String[]{String.valueOf(player.getId())});
    }

    public void deletePlayer(Player player) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYER, KEY_PLAYER_ID + " = ?",
                new String[]{String.valueOf(player.getId())});
        db.close();
    }

    public long addMatch(Match match) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MATCH_TYPE, match.getMatchType());
        contentValues.put(KEY_SCORING_FORMAT, match.getScoringFormat());
        contentValues.put(KEY_PLAYER1_ID, match.getPlayer1Id());
        contentValues.put(KEY_PLAYER2_ID, match.getPlayer2Id());
        contentValues.put(KEY_PLAYER3_ID, match.getPlayer3Id());
        contentValues.put(KEY_PLAYER4_ID, match.getPlayer4Id());
        long matchId = db.insert(TABLE_MATCH, null, contentValues);
        db.close();
        return matchId;
    }

    public void updateMatchResults(long matchId, String winnerName, String finalScore, String gameScores) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_WINNER_NAME, winnerName);
        contentValues.put(KEY_FINAL_SCORE, finalScore);
        contentValues.put(KEY_GAME_SCORES, gameScores);

        db.update(TABLE_MATCH, contentValues, KEY_MATCH_ID + " = ?", new String[]{String.valueOf(matchId)});
        db.close();
    }

    public ArrayList<Match> getAllMatches() {
        ArrayList<Match> matchList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MATCH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Match match = new Match(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getInt(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9));
                matchList.add(match);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return matchList;
    }
}
