package com.example.wardrobe;

import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

public class TransactionViewModel extends ViewModel {
    String curr_user_id = "";

    public void setCurrentUserId(String currUserId){
        this.curr_user_id = currUserId;
    }

    public void addNewTransaction(String garment_id, String owner_id, String garment_owner_name, String current_user_name, String garmentImageUrl, String requestText, final TransactionRequestsModel.Listener<Boolean> listener){
        TransactionRequest transaction = new TransactionRequest();
        transaction.setBorrow_user_id(curr_user_id);
        transaction.setBorrow_user_name(current_user_name);
        transaction.setLend_user_id(owner_id);
        transaction.setLend_user_name(garment_owner_name);
        transaction.setRequest_text(requestText);
        transaction.setStatus("Requested");
        transaction.setImgUrl(garmentImageUrl);
        transaction.setGarment_id(garment_id);

        TransactionRequestsModel.instance.addNewTransaction(transaction, new TransactionRequestsModel.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                listener.onComplete(data);
            }
        });
    }

    public void updateTransactionStatus(String transactionId, String newStatus){
        TransactionRequestsModel.instance.updateTransactionStatus(transactionId,newStatus, null);
    }

    public void deleteTransaction(TransactionRequest transaction){
        TransactionRequestsModel.instance.deleteTransaction(transaction,null);
    }
}
