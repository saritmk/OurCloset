package com.example.wardrobe.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.wardrobe.model.daos.GarmentDao;
import com.example.wardrobe.model.entities.Garment;

@Database(entities = {Garment.class}, version = 2)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract GarmentDao garmentDao();
}
