package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.List;

public class GarmentsViewModel extends ViewModel {
    LiveData<List<Garment>> liveData;
    String owner_id;

    public void SetOwnerId(String owner_id){
        this.owner_id = owner_id;
    }

    public LiveData<List<Garment>> getData() {
        if (liveData == null) {
            liveData = GarmentsModel.instance.getAllGarments(owner_id);
        }
        return liveData;
    }

    public void refresh(GarmentsModel.CompListener listener) {
        GarmentsModel.instance.refreshGarmentsList(owner_id,listener);
    }

    public void addNewGarment(Garment newGarment, GarmentsModel.CompListener listener){
        GarmentsModel.instance.addNewGarment(newGarment,null);
    }
    public void saveImage(File image, String Uid, OnSuccessListener listener){
        GarmentsModel.saveImage(image,Uid,listener);
    }
}
