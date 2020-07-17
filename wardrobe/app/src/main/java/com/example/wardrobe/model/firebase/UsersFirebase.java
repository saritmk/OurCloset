package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UsersFirebase {
    final static String USERS_COLLECTION = "users";

    public static void getAllUsersList(final UsersModel.Listener<List<User>> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> usersData = null;
                if (task.isSuccessful()){
                    usersData = new LinkedList<User>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        User user = doc.toObject(User.class);
                        if(user.getUser_id()!=null) {
                            usersData.add(user);
                        }
                    }
                }
                listener.onComplete(usersData);
            }
        });
    }

    public static void addUser(User userToAdd, final UsersModel.Listener<Boolean> listener){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USERS_COLLECTION).document(userToAdd.getUser_id()).set(toJson(userToAdd)).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(listener != null)
                {
                    listener.onComplete(true);
                }

            }
        });
    }
    private static Map<String, Object> toJson(User user){
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user.getUser_id());
        result.put("email",user.getEmail());
        result.put("name",user.getName());
        result.put("img_url",user.getImg_url());
        return result;
    }
}


