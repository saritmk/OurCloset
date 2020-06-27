package com.example.wardrobe;

import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

public class TransactionViewModel extends ViewModel {
    String curr_user_id = "1";

    public void addNewTransaction(String garment_id, String owner_id, String requestText){
        TransactionRequest transaction = new TransactionRequest();
        transaction.setBorrow_user_id(curr_user_id);
        transaction.setLend_user_id(owner_id);
        transaction.setRequest_text(requestText);
        transaction.setStatus("Requested");
        transaction.setGarment_id(garment_id);

        TransactionRequestsModel.instance.addNewTransaction(transaction, new TransactionRequestsModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                // DO Somthing
            }
        });
    }

    public void updateTransactionStatus(String transactionId, String newStatus){

    }
}
