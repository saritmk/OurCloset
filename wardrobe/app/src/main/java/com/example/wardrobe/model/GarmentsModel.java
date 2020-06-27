package com.example.wardrobe.model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.firebase.GarmentsFirebase;

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

    public Garment getGarment(String id){
        return null;
    }

    public void addNewGarment(Garment garment, Listener<Boolean> listener){
        GarmentsFirebase.addGarment(garment, listener);
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





}
