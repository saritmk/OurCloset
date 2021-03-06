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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class AddTransactionFragment extends Fragment {
    String defaultRequestText = "Hey! I really like this item, can I borrow it please?";
    Garment garment;
    String garment_owner_id;
    String garment_owner_name;
    String current_user_id;
    String current_user_name;
    ImageView garmentImg;
    TextView color;
    TextView type;
    TextView size;
    EditText requestText;
    Button submitTransactionBtn;
    TransactionViewModel viewModel;
    UsersViewModel usersViewModel;

    public AddTransactionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
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
            usersViewModel.getUser(garment_owner_id, new UsersModel.Listener<User>() {
                @Override
                public User onComplete(User data) {
                    garment_owner_name = data.getName();
                    return null;
                }
            });

            update_display();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currUser = auth.getCurrentUser();
        if(currUser!=null){
            current_user_id = currUser.getUid();
            current_user_name = currUser.getDisplayName();
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

            viewModel.addNewTransaction(garment.getId(), garment_owner_id, garment_owner_name, current_user_name, garment.getImageUrl(), requestTextString, new TransactionRequestsModel.Listener<Boolean>() {
                @Override
                public void onComplete(Boolean data) {
                    submitTransactionBtn.setEnabled(false);
                    Toast.makeText(getActivity(), "your request successfully submitted", Toast.LENGTH_SHORT).show();
                    AddTransactionFragmentDirections.ActionAddTransactionFragmentToClosetListFragment2 direction= AddTransactionFragmentDirections.actionAddTransactionFragmentToClosetListFragment2(garment_owner_id);
                    Navigation.findNavController(getView()).navigate(direction);
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
