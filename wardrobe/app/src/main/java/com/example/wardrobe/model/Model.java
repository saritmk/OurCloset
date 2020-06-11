package com.example.wardrobe.model;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.entities.Garment;

import java.util.LinkedList;
import java.util.List;

public class Model {
    public static final Model instance = new Model();

    private Model() {
    }
    public interface GetAllGarmentsListener {
        void onComplete(List<Garment> data);
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
