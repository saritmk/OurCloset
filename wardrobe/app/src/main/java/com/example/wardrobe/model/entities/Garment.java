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

    public String image_url;
    public String size;
    public int owner_id;
    public String type;

    public Garment(String id, String image_url) {
        this.id = id;
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUri() {
        return image_url;
    }

    public void setImageUri(String image_url) {
        this.image_url = image_url;
    }

    public void setSize(String size) { this.size = size; }

    public String getSize() {
        return size;
    }

    public void setType(String type) { this.type = type; }

    public String getType() {
        return size;
    }

    public int getOwner_Id() {
        return owner_id;
    }

    public void setOwner_Id(int owner_id) { this.owner_id = owner_id; }
}

