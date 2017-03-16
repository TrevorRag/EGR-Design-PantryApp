package com.example.trevorragland.myapplication.POJO;

import java.util.HashMap;

/**
 * Created by AD on 2/28/2017.
 */

public class User {
    private String username;
    private String email;
    private HashMap<String, Object> timestampJoined;
    private String phone;
    private String photoUrl;


    /**
     * Required public constructor
     */
    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email, phone number, and timestampJoined as params
     *
     * @param username
     * @param email
     * @param timestampJoined
     * @param phone
     * @param photoUrl
     */
    public User(String username, String email, HashMap<String, Object> timestampJoined, String phone, String photoUrl) {
        this.username = username;
        this.timestampJoined = timestampJoined;
        this.phone = phone;
        this.email = email;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public String getPhone() { return phone; }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

