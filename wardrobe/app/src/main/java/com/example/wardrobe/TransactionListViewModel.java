package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

import java.util.List;

public class TransactionListViewModel extends ViewModel {
    LiveData<List<TransactionRequest>> borrowedFromMeliveData;
    LiveData<List<TransactionRequest>> lentToMeliveData;
    Boolean borrowedFromMe = true;
    String user_id = "2";

    public void SetBorrowedFromMe(Boolean isBorrowed) {
        borrowedFromMe = isBorrowed;
    }
    public Boolean getBorrowedFromMe() {
        return borrowedFromMe;
    }
    public LiveData<List<TransactionRequest>> getData() {
        if(borrowedFromMe) {
            if(borrowedFromMeliveData == null)
                borrowedFromMeliveData = TransactionRequestsModel.instance.getAllBorrowedFromTransactions(user_id);
            return borrowedFromMeliveData;
        }
        else {
            if(lentToMeliveData == null)
                lentToMeliveData = TransactionRequestsModel.instance.getAllLentToTransactions(user_id);
            return lentToMeliveData;
        }
    }

    public void refresh(TransactionRequestsModel.CompListener listener) {
        if(borrowedFromMe)
            TransactionRequestsModel.instance.refreshBorrowedFromTransactionsList(user_id,listener);
        else
            TransactionRequestsModel.instance.refreshLentToTransactionsList(user_id,listener);

    }
}
