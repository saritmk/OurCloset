package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersFirebase {
    final static String USERS_COLLECTION = "users";

    public static void addUser(User userToAdd, final UsersModel.Listener<Boolean> listener){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COLLECTION).add(toJson(userToAdd)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String user_id = task.getResult().getId();
                db.collection(USERS_COLLECTION).document(user_id).update("user_id",user_id).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    private static Map<String, Object> toJson(User user){
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user.getUid());
        result.put("email",user.getEmail());
        result.put("name",user.getName());
        result.put("img_url",user.getImg_url());
        return result;
    }
}


