package com.example.wardrobe.model;

import com.example.wardrobe.model.entities.User;
import com.example.wardrobe.model.firebase.UsersFirebase;

public class UsersModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public static final UsersModel instance = new UsersModel();

    private UsersModel() {
    }

    public void addUser(final User user, final Listener<Boolean> listener){
        UsersFirebase.addUser(user, new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if(data){
                    AppLocalDb.db.usersDao().insertAll(user);
                    listener.onComplete(true);
                }
            }
        });
    }
}
