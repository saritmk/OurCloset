package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String img_url;
    private String email;

    public User(){}
    public User(FirebaseUser currentUser) {
        this.setId(currentUser.getUid());
        this.setImg_url(currentUser.getPhotoUrl().toString());
        this.setName(currentUser.getDisplayName());
        this.setEmail(currentUser.getEmail());
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String uid) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
