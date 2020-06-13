package com.example.wardrobe.model;

import android.os.AsyncTask;

import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.firebase.GarmentsFirebase;
import com.google.android.gms.tasks.OnSuccessListener;

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
        GarmentsFirebase.getGarmentsList(owner_id,new OnSuccessListener<List<Garment>>() {
            @Override
            public void onSuccess(final List<Garment> garmentsList) {
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

    public void getAllGarments(final GetAllGarmentsListener listenr){
        class MyAsyncTask extends AsyncTask<String,String,String> {
            List<Garment> data;
            @Override
            protected String doInBackground(String... strings) {
              for (int i = 0; i < 10; i++) {
                    AppLocalDb.db.garmentDao().insertAll(new Garment(""+i,null));
                }

                data = AppLocalDb.db.garmentDao().getAll();
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


}
