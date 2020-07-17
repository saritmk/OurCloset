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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

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

    public void addNewGarment(final Garment garment, File image, final CompListener listener){
        if(image!=null) {
            GarmentsFirebase.saveImage(image, UUID.randomUUID().toString(), new OnSuccessListener<Object>() {
                @Override
                public void onSuccess(Object imgUrl) {
                    garment.setImageUrl(imgUrl.toString());
                    GarmentsFirebase.addGarment(garment, new Listener<Boolean>() {
                        @Override
                        public void onComplete(Boolean data) {
                            if (data) {
                                listener.onComplete();
                            }
                        }
                    });
                }
            });
        }
    }

    public void updateGarment(final String garmentId,
                       final String owner_id,
                       final String oldImageUrl,
                       final File newImage,
                       final String type,
                       final String size,
                       final String color,
                       final Listener<Boolean> listener) {
        Tasks.call(Executors.newSingleThreadExecutor(), new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                String oldImageUri = oldImageUrl;
                final Garment updateGarment = new Garment();
                updateGarment.setId(garmentId);
                updateGarment.setOwner_id(owner_id);
                updateGarment.setColor(color);
                updateGarment.setSize(size);
                updateGarment.setType(type);
                if (newImage == null) {
                    updateGarment.setImageUrl(oldImageUri);
                    GarmentsFirebase.updateGarment(updateGarment,listener);
                } else {
                    GarmentsFirebase.deleteImage(oldImageUri, new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            GarmentsFirebase.saveImage(newImage, UUID.randomUUID().toString(), new OnSuccessListener<Object>() {
                                @Override
                                public void onSuccess(Object imgUrl) {
                                    updateGarment.setImageUrl(imgUrl.toString());
                                    GarmentsFirebase.updateGarment(updateGarment,listener);
                                }
                            });
                        }
                    });
                }


                return null;
            }
        });
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






}
