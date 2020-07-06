package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.Garment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class AddTransactionFragment extends Fragment {
    String defaultRequestText = "Hey! I really like this item, can I borrow it please?";
    Garment garment;
    String garment_owner_id;
    String current_user_id;
    ImageView garmentImg;
    TextView color;
    TextView type;
    TextView size;
    EditText requestText;
    Button submitTransactionBtn;
    TransactionViewModel viewModel;

    public AddTransactionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        color = view.findViewById(R.id.transaction_garment_details_color);
        type = view.findViewById(R.id.transaction_garment_details_Type);
        size = view.findViewById(R.id.transaction_garment_details_size);
        requestText = view.findViewById(R.id.transaction_request_tv);
        garmentImg = view.findViewById(R.id.transaction_garment_details_img);
        submitTransactionBtn = view.findViewById(R.id.submit_transaction);

        garment = AddTransactionFragmentArgs.fromBundle(getArguments()).getGarment();
        if (garment != null){
            garment_owner_id = garment.getOwner_id();
            update_display();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currUser = auth.getCurrentUser();
        if(currUser!=null){
            current_user_id = currUser.getUid();
            viewModel.setCurrentUserId(current_user_id);
        }

        submitTransactionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSubmitTransaction();
            }
        });

        return view;
    }

    private void onSubmitTransaction(){
        String requestTextString = requestText.getText().toString();
        if(garment != null && garment_owner_id != "" && current_user_id != ""){
            if (requestTextString.equals("")) {
                requestTextString = this.defaultRequestText;
            }

            viewModel.addNewTransaction(garment.getId(), garment_owner_id, requestTextString, new TransactionRequestsModel.Listener<Boolean>() {
                @Override
                public void onComplete(Boolean data) {
                    submitTransactionBtn.setEnabled(false);
                }
            });
        }
    }

    private void update_display() {
        color.setText(garment.getColor());
        size.setText(garment.getSize());
        type.setText(garment.getType());
        if (garment.getImageUrl() != null && garment.getImageUrl() != "") {
            Picasso.get().load(garment.getImageUrl()).into(garmentImg);
        }
    }
}
