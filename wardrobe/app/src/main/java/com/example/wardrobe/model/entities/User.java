package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.auth.FirebaseUser;

@Entity
public class User {

    @PrimaryKey
    @NonNull
    private String user_id;
    private String name;
    private String img_url;
    private String email;

    public User(){}

    public User(String user_id, String name, String img_url, String email){
        this.setUser_id(user_id);
        this.setImg_url(img_url);
        this.setName(name);
        this.setEmail(email);
    }
    public User(FirebaseUser currentUser) {
        this.setUser_id(currentUser.getUid());
        this.setImg_url(currentUser.getPhotoUrl().toString());
        this.setName(currentUser.getDisplayName());
        this.setEmail(currentUser.getEmail());
    }

    @NonNull
    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(@NonNull String user_id) {
        this.user_id = user_id;
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
