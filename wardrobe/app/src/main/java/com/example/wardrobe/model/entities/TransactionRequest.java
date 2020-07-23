package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TransactionRequest {
    @PrimaryKey
    @NonNull
    private String transaction_id;
    @NonNull
    private String borrow_user_id;
    @NonNull
    private String borrow_user_name;
    @NonNull
    private String lend_user_id;
    @NonNull
    private String lend_user_name;
    @NonNull
    private String status;
    private String request_text;
    @NonNull
    private String garment_id;
    @NonNull
    private String imgUrl;

    private long lastUpdated;

    public TransactionRequest(){}

    public TransactionRequest(String transaction_id, String borrow_user_id,String borrow_user_name, String lend_user_id, String lend_user_name,String garment_id, String imgUrl, String status, String request_text){
        this.transaction_id = transaction_id;
        this.borrow_user_id = borrow_user_id;
        this.lend_user_id = lend_user_id;
        this.status = status;
        this.request_text = request_text;
        this.garment_id = garment_id;
        this.borrow_user_name = borrow_user_name;
        this.lend_user_name = lend_user_name;
        this.imgUrl = imgUrl;
    }

    public String getTransaction_id(){ return this.transaction_id; };
    public void setTransaction_id(String id){ this.transaction_id = id; };
    public String getBorrow_user_id(){ return this.borrow_user_id; };
    public void setBorrow_user_id(String borrow_user_id){ this.borrow_user_id = borrow_user_id; };
    public String getLend_user_id(){ return this.lend_user_id; };
    public void setLend_user_id(String lend_user_id){ this.lend_user_id = lend_user_id; };
    public String getStatus(){ return this.status; };
    public void setStatus(String status){ this.status = status; };
    public String getRequest_text(){ return this.request_text; };
    public void setRequest_text(String request_text){ this.request_text = request_text; };
    public void setGarment_id(String garment_id){ this.garment_id = garment_id;}
    @NonNull
    public String getGarment_id() {return garment_id;}

    @NonNull
    public String getBorrow_user_name() {
        return borrow_user_name;
    }

    public void setBorrow_user_name(@NonNull String borrow_user_name) {
        this.borrow_user_name = borrow_user_name;
    }

    @NonNull
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(@NonNull String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @NonNull
    public String getLend_user_name() {
        return lend_user_name;
    }

    public void setLend_user_name(@NonNull String lend_user_name) {
        this.lend_user_name = lend_user_name;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }
}
