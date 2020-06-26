package com.example.wardrobe.model.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TransactionRequest {
    @PrimaryKey
    @NonNull
    private String id;
    private String borrow_user_id;
    private String lend_user_id;
    private String status;
    private String request_text;

    public TransactionRequest(){}

    public TransactionRequest(String id, String borrow_user_id, String lend_user_id, String status, String request_text){
        this.id = id;
        this.borrow_user_id = borrow_user_id;
        this.lend_user_id = lend_user_id;
        this.status = status;
        this.request_text = request_text;
    }

    public String getId(){ return this.id; };
    public void setId(String id){ this.id = id; };
    public String getBorrow_user_id(){ return this.borrow_user_id; };
    public void setBorrow_user_id(String borrow_user_id){ this.borrow_user_id = borrow_user_id; };
    public String getLend_user_id(){ return this.lend_user_id; };
    public void setLend_user_id(String lend_user_id){ this.lend_user_id = lend_user_id; };
    public String getStatus(){ return this.status; };
    public void setStatus(String status){ this.status = status; };
    public String getRequest_text(){ return this.request_text; };
    public void setRequest_text(String request_text){ this.request_text = request_text; };
}
