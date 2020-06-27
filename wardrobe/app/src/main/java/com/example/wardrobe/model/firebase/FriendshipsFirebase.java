package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.FriendshipModel;
import com.example.wardrobe.model.entities.Friendship;
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

public class FriendshipsFirebase {
    final static String FRIENDSHIPS_COLLECTION = "Friendship";

    public static void getFriendsList(final String userId, final FriendshipModel.Listener<List<Friendship>> listener){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FRIENDSHIPS_COLLECTION).whereEqualTo("id_1", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                final List<Friendship> friendshipsData = new LinkedList<Friendship>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Friendship friendship = doc.toObject(Friendship.class);
                        friendshipsData.add(friendship);
                    }
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(FRIENDSHIPS_COLLECTION).whereEqualTo("id_2", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Friendship friendship = doc.toObject(Friendship.class);
                                friendshipsData.add(friendship);
                            }
                        }
                        listener.onComplete(friendshipsData);
                    }
                });
            }
        });
    }

    public void addFriendship(Friendship friendshipToAdd, final FriendshipModel.Listener<Boolean> listener){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FRIENDSHIPS_COLLECTION).add(toJson(friendshipToAdd)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                String friendship_id = task.getResult().getId();
                db.collection(FRIENDSHIPS_COLLECTION).document(friendship_id).update("friendship_id",friendship_id).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void deleteFriendship(String friendshipId, final FriendshipModel.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FRIENDSHIPS_COLLECTION).document(friendshipId)
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

    private static Map<String, Object> toJson(Friendship friendship){
        HashMap<String, Object> result = new HashMap<>();
        result.put("friendship_id", friendship.getFriendship_id());
        result.put("id_1", friendship.getId_1());
        result.put("id_2", friendship.getId_2());
        return result;
    }
}


