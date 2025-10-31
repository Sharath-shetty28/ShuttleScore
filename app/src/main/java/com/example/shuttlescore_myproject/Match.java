package com.example.shuttlescore_myproject;

public class Match {

    private int id;
    private String matchType;
    private String scoringFormat;
    private int player1Id;
    private int player2Id;
    private int player3Id;
    private int player4Id;
    private String winnerName;
    private String finalScore;
    private String gameScores;

    // Constructor for creating a new match before it's played
    public Match(int id, String matchType, String scoringFormat, int player1Id, int player2Id, int player3Id, int player4Id) {
        this.id = id;
        this.matchType = matchType;
        this.scoringFormat = scoringFormat;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player3Id = player3Id;
        this.player4Id = player4Id;
    }

    // Constructor for retrieving a completed match from the database
    public Match(int id, String matchType, String scoringFormat, int player1Id, int player2Id, int player3Id, int player4Id, String winnerName, String finalScore, String gameScores) {
        this.id = id;
        this.matchType = matchType;
        this.scoringFormat = scoringFormat;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.player3Id = player3Id;
        this.player4Id = player4Id;
        this.winnerName = winnerName;
        this.finalScore = finalScore;
        this.gameScores = gameScores;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public String getScoringFormat() {
        return scoringFormat;
    }

    public void setScoringFormat(String scoringFormat) {
        this.scoringFormat = scoringFormat;
    }

    public int getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(int player1Id) {
        this.player1Id = player1Id;
    }

    public int getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(int player2Id) {
        this.player2Id = player2Id;
    }

    public int getPlayer3Id() {
        return player3Id;
    }

    public void setPlayer3Id(int player3Id) {
        this.player3Id = player3Id;
    }

    public int getPlayer4Id() {
        return player4Id;
    }

    public void setPlayer4Id(int player4Id) {
        this.player4Id = player4Id;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(String finalScore) {
        this.finalScore = finalScore;
    }

    public String getGameScores() {
        return gameScores;
    }

    public void setGameScores(String gameScores) {
        this.gameScores = gameScores;
    }
}
