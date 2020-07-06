package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.wardrobe.model.entities.Garment;
import com.squareup.picasso.Picasso;

public class FriendsGarmentDetailsFragment extends Fragment {
    Garment garment;
    TextView id;
    TextView ColorTextView;
    TextView SizeTextView;
    TextView TypeTextView;
    ImageView ImageView;
    Button TransactionButton;

    public FriendsGarmentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_garment_details, container, false);

        ColorTextView = view.findViewById(R.id.friends_garment_details_color);
        SizeTextView = view.findViewById(R.id.friends_garment_details_size);
        TypeTextView = view.findViewById(R.id.friends_garment_details_Type);
        ImageView = view.findViewById(R.id.friends_garment_details_img);
        TransactionButton = view.findViewById(R.id.friends_garment_details_transaction_button);

        garment = GarmentDetailsFragmentArgs.fromBundle(getArguments()).getGarment();
        if (garment != null){
            update_display();
        }
        TransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendsGarmentDetailsFragment.this.onAddTransactionButton();
            }
        });

        return view;
    }

    private void onAddTransactionButton(){
        FriendsGarmentDetailsFragmentDirections.ActionGarmentDetailsFragmentToAddTransactionFragment direction = FriendsGarmentDetailsFragmentDirections.actionGarmentDetailsFragmentToAddTransactionFragment(garment);
        Navigation.findNavController(getView()).navigate(direction);
    }

    private void update_display() {
        ColorTextView.setText(garment.getColor());
        SizeTextView.setText(garment.getSize());
        TypeTextView.setText(garment.getType());
        if (garment.getImageUrl() != null && garment.getImageUrl() != "") {
            Picasso.get().load(garment.getImageUrl()).into(ImageView);
        }
    }
}
