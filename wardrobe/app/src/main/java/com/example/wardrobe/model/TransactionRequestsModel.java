package com.example.wardrobe.model;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wardrobe.WardrobeApplication;
import com.example.wardrobe.model.entities.TransactionRequest;
import com.example.wardrobe.model.firebase.TransactionRequestsFirebase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TransactionRequestsModel {
    public interface Listener<T>{
        void onComplete(T data);
    }

    public interface CompListener{
        void onComplete();
    }

    public static final TransactionRequestsModel instance = new TransactionRequestsModel();

    private TransactionRequestsModel() {
    }

    public void refreshBorrowedFromTransactionsList(String user_id, final CompListener listener){
        long lastUpdated = WardrobeApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("TransactionsLastUpdateDate",0);
        TransactionRequestsFirebase.getBorrowedFromTransactionsSince(lastUpdated,user_id,new Listener<List<TransactionRequest>>() {
            @Override
            public void onComplete(final List<TransactionRequest> TransactionsList) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        if(TransactionsList!=null) {
                            long lastUpdated = 0;
                            for (TransactionRequest currTransaction : TransactionsList) {
                                if(currTransaction.getDeleted()){
                                    AppLocalDb.db.transactionDao().delete(currTransaction);
                                }
                                else {
                                    AppLocalDb.db.transactionDao().insertAll(currTransaction);
                                }
                                if (currTransaction.getLastUpdated() > lastUpdated)
                                    lastUpdated = currTransaction.getLastUpdated();
                            }
                            SharedPreferences.Editor edit = WardrobeApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                            edit.putLong("GarmentsLastUpdateDate", lastUpdated);
                            edit.commit();
                        }
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });

    }

    public void refreshLentToTransactionsList(String user_id, final CompListener listener){
        long lastUpdated = WardrobeApplication.context.getSharedPreferences("TAG",MODE_PRIVATE).getLong("TransactionsLastUpdateDate",0);
        TransactionRequestsFirebase.getLentToTransactionsSince(lastUpdated, user_id,new Listener<List<TransactionRequest>>() {
            @Override
            public void onComplete(final List<TransactionRequest> TransactionsList) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        if(TransactionsList!=null) {
                            long lastUpdated = 0;
                            for (TransactionRequest currTransaction : TransactionsList) {
                                if(currTransaction.getDeleted()){
                                    AppLocalDb.db.transactionDao().delete(currTransaction);
                                }
                                else {
                                    AppLocalDb.db.transactionDao().insertAll(currTransaction);
                                }
                                if (currTransaction.getLastUpdated() > lastUpdated)
                                    lastUpdated = currTransaction.getLastUpdated();
                            }
                            SharedPreferences.Editor edit = WardrobeApplication.context.getSharedPreferences("TAG", MODE_PRIVATE).edit();
                            edit.putLong("GarmentsLastUpdateDate", lastUpdated);
                            edit.commit();
                        }
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (listener!=null)  listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    public TransactionRequest getTransactionRequest(String id){
        return null;
    }

    public void addNewTransaction(TransactionRequest transactionRequest, Listener<Boolean> listener){
        TransactionRequestsFirebase.addTransaction(transactionRequest, listener);
    }

    public void deleteTransaction(final TransactionRequest transactionRequest, final Listener<Boolean> listener){
        TransactionRequestsFirebase.deleteTransaction(transactionRequest.getTransaction_id(), new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if(data) {
                    AppLocalDb.db.transactionDao().delete(transactionRequest);
                    if(listener!=null){
                        listener.onComplete(true);
                    }
                }
            }
        });
    }

    public void updateTransactionStatus(String transaction_id, String newStatus, final Listener<Boolean> listener){
        TransactionRequestsFirebase.updateTransactionStatus(transaction_id, newStatus, listener);
    }

    public void updateTransactionWhenGarmentChanged(String garment_id, String img_url, final Listener<Boolean> listener){
        TransactionRequestsFirebase.updateTransactionWhenGarmentChanged(garment_id, img_url, listener);
    }

    public LiveData<List<TransactionRequest>> getAllBorrowedFromTransactions(String user_id){
        LiveData<List<TransactionRequest>> liveData = AppLocalDb.db.transactionDao().getAllBorrowedFrom(user_id);
        refreshBorrowedFromTransactionsList(user_id,null);
        return liveData;
    }

    public LiveData<List<TransactionRequest>> getAllLentToTransactions(String user_id){
        LiveData<List<TransactionRequest>> liveData = AppLocalDb.db.transactionDao().getAllLentTo(user_id);
        refreshLentToTransactionsList(user_id,null);
        return liveData;
    }
    public void deleteTransactionByGarmentId(String garment_id, final Listener<Boolean> listener) {
        AppLocalDb.db.transactionDao().deleteTransactionByGarmentId(garment_id);
        listener.onComplete(true);
    }
}
