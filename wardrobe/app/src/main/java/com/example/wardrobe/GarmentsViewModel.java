package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;

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

    public void getGarment(String garmentId, GarmentsModel.Listener<Garment> listener){
        Garment garment = GarmentsModel.instance.getGarment(garmentId);
        listener.onComplete(garment);
    }

    public void addNewGarment(Garment newGarment, File image, GarmentsModel.CompListener listener){
        GarmentsModel.instance.addNewGarment(newGarment, image, listener);
    }

    public void updateGarment(Garment currGarment,
                              File newImg,
                              String newType,
                              String newSize,
                              String newColor,
                              GarmentsModel.Listener<Boolean> listener){
        GarmentsModel.instance.updateGarment(currGarment.getId(), currGarment.getOwner_id(), currGarment.getImageUrl(), newImg, newType, newSize, newColor, listener);
    }
    public void deleteGarment(Garment garment, GarmentsModel.Listener<Boolean> listener) {
        GarmentsModel.instance.delete(garment,listener);
    }
}
