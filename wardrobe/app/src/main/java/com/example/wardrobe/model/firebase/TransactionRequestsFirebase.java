package com.example.wardrobe.model.firebase;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

import java.util.List;

public class TransactionRequestsFirebase {
    public static void getLentFromMeTransactions(String owner_id, final TransactionRequestsModel.Listener<List<TransactionRequest>> listener){

    }

    public static void getBorrowedByMeTransactions(String owner_id,  final TransactionRequestsModel.Listener<List<TransactionRequest>> listener){

    }

    public static void addTransaction(TransactionRequest transaction,  final TransactionRequestsModel.Listener<Boolean> listener){

    }

    public static void deleteTransaction(String transactionId, final TransactionRequestsModel.Listener<Boolean> listener){

    }

}
