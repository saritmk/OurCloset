package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Garment implements Serializable {

    @PrimaryKey
    @NonNull
    public String id;
    public String imageUri;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Garment(String id, String imageUri) {
        this.id = id;
        this.imageUri = imageUri;
    }
}
