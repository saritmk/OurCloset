package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.FriendshipModel;
import com.example.wardrobe.model.entities.Friendship;

import java.util.List;

public class FriendsListViewModel extends ViewModel {
    LiveData<List<Friendship>> liveData;
    String user_id= "1";

    public LiveData<List<Friendship>> getData() {
        if (liveData == null) {
            liveData = FriendshipModel.instance.getAllFriendships(user_id);
        }
        return liveData;
    }

    public void refresh(FriendshipModel.CompListener listener) {
        FriendshipModel.instance.refreshFriendshipsList(user_id,listener);
    }
}
