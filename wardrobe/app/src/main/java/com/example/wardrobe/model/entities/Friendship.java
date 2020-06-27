package com.example.wardrobe.model.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friendship {

    @PrimaryKey
    @NonNull
    private String friendship_id;

    @NonNull
    private String id_1;

    @NonNull
    private String id_2;

    public Friendship(){}

    @NonNull
    public String getId_1() {
        return id_1;
    }

    public void setId_1(@NonNull String id1) {
        this.id_1 = id1;
    }

    @NonNull
    public String getId_2() {
        return id_2;
    }

    public void setId_2(@NonNull String id2) {
        this.id_2 = id2;
    }

    @NonNull
    public String getFriendship_id() {
        return friendship_id;
    }

    public void setFriendship_id(@NonNull String friendship_id) {
        this.friendship_id = friendship_id;
    }
}