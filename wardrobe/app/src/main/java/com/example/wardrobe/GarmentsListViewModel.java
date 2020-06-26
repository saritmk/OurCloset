package com.example.wardrobe;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;

import java.util.List;

public class GarmentsListViewModel extends ViewModel {
    LiveData<List<Garment>> liveData;
    String owner_id= "1";

    public LiveData<List<Garment>> getData() {
        if (liveData == null) {
            liveData = GarmentsModel.instance.getAllGarments(owner_id);
        }
        return liveData;
    }

    public void refresh(GarmentsModel.CompListener listener) {
        GarmentsModel.instance.refreshGarmentsList(owner_id,listener);
    }
}
