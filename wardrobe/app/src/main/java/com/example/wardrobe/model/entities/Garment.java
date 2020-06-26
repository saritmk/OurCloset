package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Garment implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;
    private String imageUrl;
    private String color;
    private String owner_id;
    private String type;
    private String size;

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_id() {
        return this.owner_id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public Garment(){}
    public Garment(String id, String imageUrl, String owner_id, String type, String color, String size) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.color = color;
        this.owner_id = owner_id;
        this.type = type;
        this.size = size;
    }
}
