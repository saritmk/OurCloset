package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TransactionRequestsFirebase {
    final static String TRANSACTION_REQUESTS_COLLECTION = "Transaction_request";

    public static void getLentToTransactions(String user_id, final TransactionRequestsModel.Listener<List<TransactionRequest>> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).whereEqualTo("borrow_user_id", user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<TransactionRequest> transactionsData = null;
                if (task.isSuccessful()){
                    transactionsData = new LinkedList<TransactionRequest>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        TransactionRequest transactionRequest = doc.toObject(TransactionRequest.class);
                        if(transactionRequest.getTransaction_id()!=null){
                            transactionsData.add(transactionRequest);
                        }
                    }
                }
                listener.onComplete(transactionsData);
            }
        });
    }

    public static void getBorrowedFromTransactions(String user_id,  final TransactionRequestsModel.Listener<List<TransactionRequest>> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).whereEqualTo("lend_user_id", user_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<TransactionRequest> transactionsData = null;
                if (task.isSuccessful()){
                    transactionsData = new LinkedList<TransactionRequest>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        TransactionRequest transactionRequest = doc.toObject(TransactionRequest.class);
                        if(transactionRequest.getTransaction_id()!=null) {
                            transactionsData.add(transactionRequest);
                        }
                    }
                }
                listener.onComplete(transactionsData);
            }
        });
    }

    public static void addTransaction(TransactionRequest transactionToAdd,  final TransactionRequestsModel.Listener<Boolean> listener){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).add(toJson(transactionToAdd)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String transaction_id = task.getResult().getId();
                db.collection(TRANSACTION_REQUESTS_COLLECTION).document(transaction_id).update("transaction_id",transaction_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });

            }
        });
    }

    public static void updateTransactionStatus(String transactionId, String newStatus, final TransactionRequestsModel.Listener<Boolean> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).document(transactionId)
                .update("status", newStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void deleteTransaction(String transactionId, final TransactionRequestsModel.Listener<Boolean> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).document(transactionId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
    }
    public static void deleteTransactionByGarmentId(String garmentId, final TransactionRequestsModel.Listener<Boolean> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRANSACTION_REQUESTS_COLLECTION).whereEqualTo("garment_id",garmentId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                querySnapshot.forEach(new Consumer<QueryDocumentSnapshot>() {
                    @Override
                    public void accept(QueryDocumentSnapshot doc) {
                        doc.getReference().delete();
                    }
                });
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot x) {
                listener.onComplete(true);
            }
        });
    }
    private static Map<String, Object> toJson(TransactionRequest transaction){
        HashMap<String, Object> result = new HashMap<>();
        result.put("borrow_user_id", transaction.getBorrow_user_id());
        result.put("borrow_user_name", transaction.getBorrow_user_name());
        result.put("transaction_id", transaction.getTransaction_id());
        result.put("lend_user_id", transaction.getLend_user_id());
        result.put("lend_user_name", transaction.getLend_user_name());
        result.put("request_text", transaction.getRequest_text());
        result.put("status", transaction.getStatus());
        result.put("imgUrl",transaction.getImgUrl());
        result.put("garment_id",transaction.getGarment_id());
        return result;
    }

}
