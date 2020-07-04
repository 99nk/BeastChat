package com.example.beastchat.entities;

public class User
{
    //Just like firebase....model class
    private String email;
    private String userPicture;
    private String username;
    private boolean hasLoggedIn;

    public User() {
    }

    public User(String email, String userPicture, String username, boolean hasLoggedIn) {
        this.email = email;
        this.userPicture = userPicture;
        this.username = username;
        this.hasLoggedIn = hasLoggedIn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isHasLoggedIn() {
        return hasLoggedIn;
    }

    public void setHasLoggedIn(boolean hasLoggedIn) {
        this.hasLoggedIn = hasLoggedIn;
    }
}
