package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wardrobe.model.entities.Garment;


public class AddTransactionFragment extends Fragment {
    Garment garment;
    TextView id;
    AddTransactionViewModel viewModel;

    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(AddTransactionViewModel.class);

        // Only Test
        viewModel.addNewTransaction("1","2","I want this");
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
