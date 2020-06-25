package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GarmentsFirebase {
    final static String GARMENTS_COLLECTION = "Garments";

    public static  void getGarmentsList(String owner_id, final GarmentsModel.Listener<List<Garment>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Garment> stData = null;
                if (task.isSuccessful()){
                    stData = new LinkedList<Garment>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Garment garment = doc.toObject(Garment.class);
                        stData.add(garment);
                    }
                }
                listener.onComplete(stData);
            }
        });
    }

    public void addGarment(Garment garmentToAdd, final GarmentsModel.Listener<Boolean> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).document().set(toJson(garmentToAdd)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public void deleteGarment(String garmentId, final GarmentsModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).document(garmentId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
    }

    private static Map<String, Object> toJson(Garment garment){
        HashMap<String, Object> result = new HashMap<>();
        result.put("size", garment.getSize());
        result.put("id", garment.getId());
        result.put("imageUrl", garment.getImageUrl());
        result.put("color", garment.getColor());
        result.put("owner_id", garment.getOwner_id());
        result.put("type", garment.getType());
        return result;
    }
}
