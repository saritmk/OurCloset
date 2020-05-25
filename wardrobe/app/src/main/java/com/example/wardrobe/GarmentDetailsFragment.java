package com.example.wardrobe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wardrobe.model.entities.Garment;

import org.w3c.dom.Text;

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

        return view;
    }

    private void update_display() {
        id.setText(garment.id);
    }
}
