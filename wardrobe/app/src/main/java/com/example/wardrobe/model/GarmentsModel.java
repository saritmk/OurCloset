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
    public static final GarmentsModel instance = new GarmentsModel();

    private GarmentsModel() {
    }
    public interface GetAllGarmentsListener {
        void onComplete(List<Garment> data);
    }

    public void refreshGarmentsList(int owner_id){
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
                }.execute("");
            }
        });
    }

    public void getAllGarments(final int owner_id, final GetAllGarmentsListener listenr){
        class MyAsyncTask extends AsyncTask<String,String,String> {
            List<Garment> data;
            @Override
            protected String doInBackground(String... strings) {
              for (int i = 0; i < 10; i++) {
                    AppLocalDb.db.garmentDao().insertAll(new Garment(""+i,null,null,null,null,null));
                }

                data = AppLocalDb.db.garmentDao().getAll();
                refreshGarmentsList(owner_id);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listenr.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public Garment getGarment(String id){
        return null;
    }

    public LiveData<List<Garment>> getAllGarments(int owner_id){
        LiveData<List<Garment>> liveData = (LiveData<List<Garment>>) AppLocalDb.db.garmentDao().getAll();
        refreshGarmentsList(owner_id);
        return liveData;
    }


}
