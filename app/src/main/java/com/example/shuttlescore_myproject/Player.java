package com.example.shuttlescore_myproject;

public class Player {

    private int id;
    private String name;
    private String team;
    private String gender;

    public Player(int id, String name, String team, String gender) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
