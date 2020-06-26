package com.example.wardrobe.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wardrobe.model.entities.Friendship;
import com.example.wardrobe.model.firebase.FriendshipsFirebase;

import java.util.List;

public class FriendshipModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    public static final FriendshipModel instance = new FriendshipModel();

    private FriendshipModel() {
    }

    public void refreshFriendshipsList(String user_id, final CompListener listener){
        FriendshipsFirebase.getFriendsList(user_id,new Listener<List<Friendship>>() {
            @Override
            public void onComplete(final List<Friendship> friendshipsList) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (Friendship currFriendship : friendshipsList) {
                            AppLocalDb.db.friendshipDao().insertAll(currFriendship);
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

    public void delete(Friendship friendship){

    }

    public LiveData<List<Friendship>> getAllFriendships(String user_id){
        LiveData<List<Friendship>> liveData = AppLocalDb.db.friendshipDao().getAll(user_id);
        refreshFriendshipsList(user_id,null);
        return liveData;
    }
}
