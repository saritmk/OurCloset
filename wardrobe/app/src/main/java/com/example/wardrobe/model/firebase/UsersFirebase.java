package com.example.wardrobe.model.firebase;

public class UsersFirebase {
    final static String USERS_COLLECTION = "users";

//    public static void getFriendsList(final String userId, final UsersModel.Listener<List<Friendship>> listener){
//        FriendshipsFirebase.getFriendsList(userId, new UsersModel.Listener<List<Friendship>>() {
//            @Override
//            public void onComplete(List<Friendship> data) {
//
//            }
//        });
//    }

//    public void addGarment(Garment garmentToAdd, final GarmentsModel.Listener<Boolean> listener){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(GARMENTS_COLLECTION).document().set(toJson(garmentToAdd)).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (listener!=null){
//                    listener.onComplete(task.isSuccessful());
//                }
//            }
//        });
//    }
}


