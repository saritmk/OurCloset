package com.example.wardrobe.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wardrobe.model.entities.Garment;

import java.util.List;

@Dao
public interface GarmentDao {
    @Query("SELECT * FROM Garment")
    LiveData<List<Garment>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Garment... garments);

    @Delete
    void delete(Garment garment);
}
