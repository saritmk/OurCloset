package com.example.wardrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wardrobe.model.entities.Garment;


public class AddTransactionFragment extends Fragment {
    Garment garment;
    TextView id;

    public AddTransactionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        id = view.findViewById(R.id.add_transaction_garment_id);
        garment = AddTransactionFragmentArgs.fromBundle(getArguments()).getGarment();
        if (garment != null){
            update_display();
        }

        return view;
    }
    private void update_display() {
        //id.setText(garment.id);
    }

}
