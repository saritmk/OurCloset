package com.example.wardrobe;

import androidx.lifecycle.LiveData;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

import java.util.List;

public class TransactionListViewModel {
    LiveData<List<TransactionRequest>> liveData;
    Boolean borrowedFromMe = true;
    String user_id = "1";

    public LiveData<List<TransactionRequest>> getData() {
        if (liveData == null) {
            if(borrowedFromMe)
                liveData = TransactionRequestsModel.instance.getAllBorrowedFromTransactions(user_id);
            else
                liveData = TransactionRequestsModel.instance.getAllLentToTransactions(user_id);
        }
        return liveData;
    }

    public void refresh(TransactionRequestsModel.CompListener listener) {
        if(borrowedFromMe)
            TransactionRequestsModel.instance.refreshBorrowedFromTransactionsList(user_id,listener);
        else
            TransactionRequestsModel.instance.refreshLentToTransactionsList(user_id,listener);

    }
}
