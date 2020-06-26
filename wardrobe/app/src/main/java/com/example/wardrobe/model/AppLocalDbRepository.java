package com.example.wardrobe.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.wardrobe.model.daos.FriendshipDao;
import com.example.wardrobe.model.daos.GarmentDao;
import com.example.wardrobe.model.daos.TransactionDao;
import com.example.wardrobe.model.entities.Friendship;
import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.TransactionRequest;

@Database(entities = {Garment.class, TransactionRequest.class, Friendship.class}, version = 4)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract GarmentDao garmentDao();
    public abstract TransactionDao transactionDao();
    public abstract FriendshipDao friendshipDao();
}
