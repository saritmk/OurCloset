package com.example.wardrobe.model;

import com.example.wardrobe.model.entities.Garment;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();


    List<Garment> data = new LinkedList<>();

    private Model(){
        for (int i = 0; i < 10; i++) {
            data.add(new Garment(""+i,null));
        }
    }


    public List<Garment> getAllGarments(){
        return data;
    }


    public Garment getGarment(String id){
        return null;
    }
}
