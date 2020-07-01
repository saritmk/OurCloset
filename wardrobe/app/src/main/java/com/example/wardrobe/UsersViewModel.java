package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.User;

import java.util.List;

public class UsersViewModel extends ViewModel {
    LiveData<List<User>> liveData;

    public LiveData<List<User>> getData() {
        if (liveData == null) {
            liveData = UsersModel.instance.getAllUsers();
        }
        return liveData;
    }

    public void refresh(UsersModel.CompListener listener) {
        UsersModel.instance.refreshUsersList(listener);
    }

    public void saveUser(User user) {
        UsersModel.instance.addUser(user,null);
    }
}
