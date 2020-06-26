package com.example.wardrobe.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wardrobe.model.entities.Friendship;

import java.util.List;

@Dao
public interface FriendshipDao {
    @Query("SELECT * FROM Friendship WHERE id_1 = :user_id OR id_2=:user_id")
    LiveData<List<Friendship>> getAll(String user_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Friendship... friendships);

    @Delete
    void delete(Friendship friendship);
}
