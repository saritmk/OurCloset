package com.example.wardrobe;

import android.text.BoringLayout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;

import java.util.List;

public class TransactionListViewModel extends ViewModel {
    LiveData<List<TransactionRequest>> borrowedFromMeliveData;
    LiveData<List<TransactionRequest>> lentToMeliveData;
    Boolean borrowedFromMe;
    String user_id;

    public void SetCurrentUserId(String user_id) { this.user_id = user_id; }

    public void SetBorrowedFromMe(Boolean isBorrowed) {
        borrowedFromMe = isBorrowed;
    }

    public Boolean getBorrowedFromMe() {
        return borrowedFromMe;
    }

    public LiveData<List<TransactionRequest>> getBorrowedData() {
        if(borrowedFromMeliveData == null)
            borrowedFromMeliveData = TransactionRequestsModel.instance.getAllBorrowedFromTransactions(user_id);
        return borrowedFromMeliveData;
    }

    public LiveData<List<TransactionRequest>> getLentData() {
        if(lentToMeliveData == null)
            lentToMeliveData = TransactionRequestsModel.instance.getAllLentToTransactions(user_id);
        return lentToMeliveData;
    }

    public void refresh(TransactionRequestsModel.CompListener listener) {
        if(borrowedFromMe)
            TransactionRequestsModel.instance.refreshBorrowedFromTransactionsList(user_id,listener);
        else
            TransactionRequestsModel.instance.refreshLentToTransactionsList(user_id,listener);

    }


}
