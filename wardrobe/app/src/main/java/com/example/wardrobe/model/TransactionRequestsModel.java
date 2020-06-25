package com.example.wardrobe.model;

public class TransactionRequestsModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public static final TransactionRequestsModel instance = new TransactionRequestsModel();

    private TransactionRequestsModel() {
    }
}
