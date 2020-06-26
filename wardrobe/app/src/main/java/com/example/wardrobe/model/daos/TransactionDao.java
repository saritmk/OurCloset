package com.example.wardrobe.model.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wardrobe.model.entities.TransactionRequest;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM TransactionRequest")
    LiveData<List<TransactionRequest>> getAll();

    @Query("SELECT * FROM TransactionRequest WHERE borrow_user_id= :user_id")
    LiveData<List<TransactionRequest>> getAllLentTo(String user_id);

    @Query("SELECT * FROM TransactionRequest WHERE lend_user_id = :user_id")
    LiveData<List<TransactionRequest>> getAllBorrowedFrom(String user_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(TransactionRequest... transactions);

    @Delete
    void delete(TransactionRequest transaction);
}
