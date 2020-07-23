package com.example.wardrobe.model.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GarmentsFirebase {
    final static String GARMENTS_COLLECTION = "Garments";

    public static void getGarmentListSince(long since,String owner_id, final GarmentsModel.Listener<List<Garment>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp timestamp = new Timestamp(since,0);
        db.collection(GARMENTS_COLLECTION).whereEqualTo("owner_id",owner_id).whereGreaterThanOrEqualTo("lastUpdated", timestamp)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Garment> garments = null;
                if (task.isSuccessful()){
                    garments = new LinkedList<Garment>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String,Object> json = doc.getData();
                        Garment garment = toGarment(json);
                        garments.add(garment);
                    }
                }
                listener.onComplete(garments);
            }
        });
    }
    public static  void getGarmentsList(String owner_id, final GarmentsModel.Listener<List<Garment>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).whereEqualTo("owner_id", owner_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Garment> garmentsData = null;
                if (task.isSuccessful()){
                    garmentsData = new LinkedList<Garment>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Garment garment =toGarment(doc.getData());
                        if(garment.getId()!=null) {
                            garmentsData.add(garment);
                        }
                    }
                }
                listener.onComplete(garmentsData);
            }
        });
    }

    public static void addGarment(Garment garmentToAdd, final GarmentsModel.Listener<Boolean> listener){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).add(toJson(garmentToAdd)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String id = task.getResult().getId();
                db.collection(GARMENTS_COLLECTION).document(id).update("id",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
            }
        });
    }

    public static void deleteGarment(String garmentId, final GarmentsModel.Listener<Boolean> listener) {
        HashMap<String, Object> deleteObj = new HashMap<>();
        deleteObj.put("isDeleted", true);
        deleteObj.put("lastUpdated", FieldValue.serverTimestamp());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).document(garmentId)
                .update(deleteObj)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
    }

    public static void updateGarment(Garment garment, final GarmentsModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(GARMENTS_COLLECTION).document(garment.getId())
                .set(toJson(garment))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
    }

    public static void saveImage(File image, String imgId, final OnSuccessListener<Object> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference imageRef = storage.getReference().child("images").child(imgId);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int x = 0;
            }
        });
    }

    public static void deleteImage(String imgUrl, final OnSuccessListener<Object> listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference imageRef = storage.getReferenceFromUrl(imgUrl);
        imageRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Object>() {
                    @Override
                    public void onSuccess(Object object) {
                        listener.onSuccess(object);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int x = 0;
            }
        });
    }
    private static Garment toGarment(Map<String, Object> json){
        Garment garment = new Garment();
        garment.setId((String)json.get("id"));
        garment.setOwner_id( (String)json.get("owner_id"));
        garment.setImageUrl( (String)json.get("imageUrl"));
        garment.setType((String)json.get("type"));
        garment.setSize((String)json.get("size"));
        garment.setColor((String)json.get("color"));
        garment.setDeleted((boolean)json.get("isDeleted"));
        Timestamp timestamp = (Timestamp)json.get("lastUpdated");
        if (timestamp != null) garment.setLastUpdated(timestamp.getSeconds());
        return garment;
    }
    private static Map<String, Object> toJson(Garment garment){
        HashMap<String, Object> result = new HashMap<>();
        result.put("size", garment.getSize());
        result.put("id", garment.getId());
        result.put("imageUrl", garment.getImageUrl());
        result.put("color", garment.getColor());
        result.put("owner_id", garment.getOwner_id());
        result.put("type", garment.getType());
        result.put("lastUpdated", FieldValue.serverTimestamp());
        result.put("isDeleted", garment.getDeleted());
        return result;
    }


}
