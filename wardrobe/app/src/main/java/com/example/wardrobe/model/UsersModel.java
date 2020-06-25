package com.example.wardrobe.model;

public class UsersModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public static final UsersModel instance = new UsersModel();

    private UsersModel() {
    }
}
