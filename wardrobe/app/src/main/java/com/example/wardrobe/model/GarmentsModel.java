package com.example.wardrobe.model;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.firebase.GarmentsFirebase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

public class GarmentsModel {
    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }
    public static final GarmentsModel instance = new GarmentsModel();

    private GarmentsModel() {
    }

    public void refreshGarmentsList(String owner_id, final CompListener listener){
        GarmentsFirebase.getGarmentsList(owner_id,new Listener<List<Garment>>() {
            @Override
            public void onComplete(final List<Garment> garmentsList) {
                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (Garment currGarment : garmentsList) {
                            AppLocalDb.db.garmentDao().insertAll(currGarment);
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

    public LiveData<List<Garment>> getAllGarments(String owner_id){
        LiveData<List<Garment>> liveData = AppLocalDb.db.garmentDao().getAll(owner_id);
        refreshGarmentsList(owner_id,null);
        return liveData;
    }

    public Garment getGarment(String id)
    {
        Garment garment = AppLocalDb.db.garmentDao().getGarment(id);
        return garment;
    }

    public void addNewGarment(Garment garment, Listener<Boolean> listener){
        GarmentsFirebase.addGarment(garment, listener);
        //AppLocalDb.db.garmentDao().insertAll(garment);
    }

    public void update(Garment garment, Listener<Boolean> listener){
        GarmentsFirebase.updateGarment(garment,listener);
    }

    public void delete(final Garment garment, final Listener<Boolean> listener){
        GarmentsFirebase.deleteGarment(garment.getId(), new Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                if(data) {
                    AppLocalDb.db.garmentDao().delete(garment);
                    listener.onComplete(true);
                }
            }
        });
    }


    public static void saveImage(File image, String uid, final OnSuccessListener<Object> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference imageRef = storage.getReference().child("images").child(uid);
        Uri fileUri = Uri.fromFile(image);

        UploadTask uploadTask = imageRef.putFile(fileUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Exception e = task.getException();
                    Log.e("CHANGE IT", e.toString());
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnSuccessListener(new OnSuccessListener<Object>() {
            @Override
            public void onSuccess(Object task) {
                listener.onSuccess(task);
            }
        });
    }



}
