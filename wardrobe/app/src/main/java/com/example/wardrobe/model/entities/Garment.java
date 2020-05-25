package com.example.wardrobe.model.entities;

import java.io.Serializable;

public class Garment implements Serializable {
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
