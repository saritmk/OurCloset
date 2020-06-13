package com.example.wardrobe.model.firebase;

import androidx.annotation.NonNull;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GarmentsFirebase {

    public  GarmentsFirebase(){
    }

    public static  void getGarmentsList(int owner_id, final OnSuccessListener listener) {
        final ArrayList<Garment> garments = new ArrayList<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Garments");
        db.equalTo(owner_id,"owner_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Garment garment = dataSnapshot.getValue(Garment.class);
                garments.add(garment);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // something went wrong
            }
        });

        listener.onSuccess(garments);
    }

    public void AddGarment(Garment garmentToAdd, final GarmentsModel.Listener<Boolean> listener){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Garments");
        db.child("users").child(garmentToAdd.id).setValue(garmentToAdd);
    }

    public void deleteGarment(){

    }

    public void updateGarment(){
        
    }
}
