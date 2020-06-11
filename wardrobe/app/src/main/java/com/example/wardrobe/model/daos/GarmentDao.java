package com.example.wardrobe.model.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wardrobe.model.entities.Garment;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GarmentDao {
    @Query("SELECT * FROM Garment")
    List<Garment> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Garment... garments);

    @Delete
    void delete(Garment garment);
}
