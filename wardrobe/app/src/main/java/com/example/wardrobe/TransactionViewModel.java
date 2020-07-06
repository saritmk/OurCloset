package com.example.wardrobe;

import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

public class TransactionViewModel extends ViewModel {
    String curr_user_id = "";

    public void setCurrentUserId(String currUserId){
        this.curr_user_id = currUserId;
    }

    public void addNewTransaction(String garment_id, String owner_id, String requestText, final TransactionRequestsModel.Listener<Boolean> listener){
        TransactionRequest transaction = new TransactionRequest();
        transaction.setBorrow_user_id(curr_user_id);
        transaction.setLend_user_id(owner_id);
        transaction.setRequest_text(requestText);
        transaction.setStatus("Requested");
        transaction.setGarment_id(garment_id);

        TransactionRequestsModel.instance.addNewTransaction(transaction, new TransactionRequestsModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                listener.onComplete(data);
            }
        });
    }

    public void updateTransactionStatus(String transactionId, String newStatus){

    }
}
