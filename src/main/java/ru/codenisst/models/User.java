package ru.codenisst.models;

public class User {

    private final String username;
    private final int score;

    public User(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return this.username + " - " + this.score;
    }
}
