package com.example.wardrobe;

import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.User;

public class UsersViewModel extends ViewModel {
    public void saveUser(User user) {
        UsersModel.instance.addUser(user,null);
    }
}
