package com.example.wardrobe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wardrobe.model.entities.Garment;

public class GarmentDetailsFragment extends Fragment {
    Garment garment;
    TextView id;

    public GarmentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_garment_details, container, false);

        id = view.findViewById(R.id.garment_details_id_tv);
        garment = GarmentDetailsFragmentArgs.fromBundle(getArguments()).getGarment();
        if (garment != null){
            update_display();
        }
        Button b = view.findViewById(R.id.addTransactionForGarment);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarmentDetailsFragment.this.onAddTransactionButton();
            }
        });

        return view;
    }

    private void onAddTransactionButton(){
        GarmentDetailsFragmentDirections.ActionGarmentDetailsFragmentToAddTransactionFragment diraction = GarmentDetailsFragmentDirections.actionGarmentDetailsFragmentToAddTransactionFragment(garment);
        //Navigation.findNavController(getView()).navigate(diraction);
    }

    private void update_display() {
        id.setText(garment.getId());
    }
}
