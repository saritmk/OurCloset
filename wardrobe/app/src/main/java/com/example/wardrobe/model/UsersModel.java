package com.example.wardrobe.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.User;
import com.example.wardrobe.model.firebase.GarmentsFirebase;
import com.example.wardrobe.model.firebase.UsersFirebase;

import java.util.List;

public class UsersModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    public static final UsersModel instance = new UsersModel();

    private UsersModel() {
    }

    public void refreshUsersList(final CompListener listener){
        UsersFirebase.getAllUsersList(new Listener<List<User>>() {
            @Override
            public void onComplete(final List<User> usersList) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (User currUser : usersList) {
                            AppLocalDb.db.usersDao().insertAll(currUser);
                        }
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public LiveData<List<User>> getAllUsers(){
        LiveData<List<User>> liveData = AppLocalDb.db.usersDao().getAll();
        refreshUsersList(null);
        return liveData;
    }



    public void addUser(final User user, final Listener<Boolean> listener){
        UsersFirebase.addUser(user, new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if(data){
                    AppLocalDb.db.usersDao().insertAll(user);
                    if(listener!=null)
                        listener.onComplete(true);
                }
            }
        });
    }
}
