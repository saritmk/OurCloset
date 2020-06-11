package com.example.wardrobe.model;

import androidx.room.Room;

import com.example.wardrobe.WardrobeApplication;

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(WardrobeApplication.context,
                    AppLocalDbRepository.class, "wardrobeDB.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
}
