package com.example.wardrobe;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wardrobe.model.TransactionRequestsModel;
import com.example.wardrobe.model.entities.TransactionRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class TransactionListFragment extends Fragment {
    RecyclerView list;
    boolean isBorrowedFromMe = true;
    List<TransactionRequest> borrowedData = new LinkedList<TransactionRequest>();
    List<TransactionRequest> lentData = new LinkedList<TransactionRequest>();
    List<TransactionRequest> data = new LinkedList<>();
    TransactionListAdapter adapter;
    TransactionListViewModel viewModel;
    TransactionViewModel viewModelTransactionItem;
    LiveData<List<TransactionRequest>> liveDataBorrowed;
    LiveData<List<TransactionRequest>> liveDataLent;
    TextView EmptyLentTextView;
    TextView EmptyBorrwedTextView;
    Button LentButton;
    Button BorrowedButton;

    interface Delegate{
        void onItemSelected(TransactionRequest transactionRequest);
    }

    GarmentsListFragment.Delegate parent;
    public TransactionListFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GarmentsListFragment.Delegate) {
            parent = (GarmentsListFragment.Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + "student list parent activity must implement dtudent ;list fragment Delegate");
        }

        viewModel = new ViewModelProvider(this).get(TransactionListViewModel.class);
        viewModelTransactionItem = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            viewModel.SetCurrentUserId(user.getUid());
        }


        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_transaction_list, container, false);
        list = view.findViewById(R.id.transactions_list);
        list.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        list.setLayoutManager(layoutManager);

        adapter = new TransactionListAdapter();
        adapter.SetBorrowedFromMe(isBorrowedFromMe);
        list.setAdapter(adapter);

        adapter.setOnIntemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                TransactionRequest transactionRequest = data.get(position);
                //parent.onItemSelected(garment);
                //NavGraphDirections.ActionGlobalGarmentDetailsFragment direction = GarmentDetailsFragmentDirections.actionGlobalGarmentDetailsFragment(garment);
                //Navigation.findNavController(view).navigate(direction);
            }

            @Override
            public void onApproveClick(int position) {
                TransactionRequest transactionRequest = data.get(position);
                viewModelTransactionItem.updateTransactionStatus(transactionRequest.getTransaction_id(), "Approved", new TransactionRequestsModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        viewModel.refresh(null);
                    }
                });
            }

            @Override
            public void onRejectClick(int position) {
                TransactionRequest transactionRequest = data.get(position);
                viewModelTransactionItem.updateTransactionStatus(transactionRequest.getTransaction_id(), "rejected", new TransactionRequestsModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        viewModel.refresh(null);
                    }
                });
            }

            @Override
            public void onCancelClick(int position) {
                TransactionRequest transactionRequest = data.get(position);
                viewModelTransactionItem.deleteTransaction(transactionRequest, new TransactionRequestsModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        viewModel.refresh(null);
                    }
                });
            }

            @Override
            public void onReturnClick(int position) {
                TransactionRequest transactionRequest = data.get(position);
                viewModelTransactionItem.deleteTransaction(transactionRequest, new TransactionRequestsModel.Listener<Boolean>() {
                    @Override
                    public void onComplete(Boolean data) {
                        viewModel.refresh(null);
                    }
                });
            }
        });

        LentButton = view.findViewById(R.id.button_lent);
        BorrowedButton = view.findViewById(R.id.button_borewed);
        EmptyLentTextView = view.findViewById(R.id.empty_tranactions_lent);
        EmptyBorrwedTextView = view.findViewById(R.id.empty_tranactions_borrwed);
        EmptyLentTextView.setVisibility(View.GONE);
        EmptyBorrwedTextView.setVisibility(View.GONE);
        BorrowedButton.setBackgroundColor(Color.parseColor("#FAFAFA"));
        LentButton.setBackgroundColor(Color.parseColor("#e1877d"));
        viewModel.SetBorrowedFromMe(isBorrowedFromMe);
        liveDataBorrowed = viewModel.getBorrowedData();
        liveDataBorrowed.observe(getViewLifecycleOwner(), new Observer<List<TransactionRequest>>() {
            @Override
            public void onChanged(List<TransactionRequest> transactionRequests) {
                borrowedData = transactionRequests;
                if(isBorrowedFromMe){
                    if(transactionRequests.isEmpty()) {
                        EmptyBorrwedTextView.setVisibility(View.VISIBLE);
                        EmptyLentTextView.setVisibility(View.GONE);

                    }
                    data = transactionRequests;
                    adapter.notifyDataSetChanged();
                }
            }
        });

        liveDataLent = viewModel.getLentData();
        liveDataLent.observe(getViewLifecycleOwner(), new Observer<List<TransactionRequest>>() {
            @Override
            public void onChanged(List<TransactionRequest> transactionRequests) {
                lentData = transactionRequests;
                if(!isBorrowedFromMe){
                    if(transactionRequests.isEmpty()) {
                        EmptyBorrwedTextView.setVisibility(View.GONE);
                        EmptyLentTextView.setVisibility(View.VISIBLE);
                    }
                    data = transactionRequests;
                    adapter.notifyDataSetChanged();
                }
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.transactions_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new TransactionRequestsModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        LentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BorrowedButton.setBackgroundColor(Color.parseColor("#e1877d"));
                LentButton.setBackgroundColor(Color.parseColor("#FAFAFA"));

                onLentToMeButtonClick(v);
            }
        });

        BorrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BorrowedButton.setBackgroundColor(Color.parseColor("#FAFAFA"));
                LentButton.setBackgroundColor(Color.parseColor("#e1877d"));

                onBorrwedFromMeButtonClick(v);
            }
        });

        return view;
    }


    private void onLentToMeButtonClick(View view){
        data = lentData;
        EmptyBorrwedTextView.setVisibility(View.GONE);
        adapter.SetBorrowedFromMe(false);
        adapter.notifyDataSetChanged();
        isBorrowedFromMe = false;
        viewModel.SetBorrowedFromMe(false);
        if(data.isEmpty()) {
            EmptyLentTextView.setVisibility(View.VISIBLE);
        }
    }

    private void onBorrwedFromMeButtonClick(View view){
        data = borrowedData;
        EmptyLentTextView.setVisibility(View.GONE);
        adapter.SetBorrowedFromMe(true);
        adapter.notifyDataSetChanged();
        isBorrowedFromMe = true;
        viewModel.SetBorrowedFromMe(true);

        if(data.isEmpty()) {
            EmptyBorrwedTextView.setVisibility(View.VISIBLE);
        }
    }

    static class TransactionItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView otherUserName;
        TextView status;
        EditText requestText;
        TransactionRequest transactionRequest;
        Boolean isBorrowedFromMe;
        TextView fromTo;
        Button approveTransaction;
        Button rejectTransaction;
        Button cancelTransaction;
        Button returnedTransaction;

        public TransactionItemViewHolder(@NonNull View itemView, Boolean isBorrowedFromMe, final OnItemClickListener listener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.transaction_garment_iv);
            status = itemView.findViewById(R.id.transaction_status_tv);
            requestText = itemView.findViewById(R.id.request_text_transaction_tv);
            otherUserName = itemView.findViewById(R.id.other_user_transaction_tv);
            fromTo = itemView.findViewById(R.id.from_to_transaction_tv);
            this.isBorrowedFromMe = isBorrowedFromMe;
            approveTransaction = itemView.findViewById(R.id.approve_transaction_btn);
            rejectTransaction = itemView.findViewById(R.id.reject_transaction_btn);
            cancelTransaction = itemView.findViewById(R.id.cancel_request_btn);
            returnedTransaction = itemView.findViewById(R.id.returned_request_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onClick(position);
                        }
                    }
                }
            });

            approveTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onApproveClick(position);
                        }
                    }
                }
            });

            rejectTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onRejectClick(position);
                        }
                    }
                }
            });

            cancelTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onCancelClick(position);
                        }
                    }
                }
            });

            returnedTransaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onReturnClick(position);
                        }
                    }
                }
            });
        }
        void SetBorrowedFromMe(Boolean isBorrowedFromMe){
            this.isBorrowedFromMe = isBorrowedFromMe;
        }
        public void bind(TransactionRequest tr) {
            transactionRequest = tr;
            status.setText(transactionRequest.getStatus());
            requestText.setText(transactionRequest.getRequest_text());
            updateItemView(tr);
            if (tr.getImgUrl() != null && tr.getImgUrl() != "") {
                Picasso.get().load(tr.getImgUrl()).into(imgView);
            }
        }

        private void updateItemView(TransactionRequest tr){
            if(isBorrowedFromMe){
                if (tr.getStatus().equals("Approved")) {
                    approveTransaction.setEnabled(false);
                    approveTransaction.setVisibility(View.GONE);
                    rejectTransaction.setEnabled(false);
                    rejectTransaction.setVisibility(View.GONE);
                    returnedTransaction.setEnabled(true);
                    returnedTransaction.setVisibility(View.VISIBLE);
                }
                else{
                    approveTransaction.setEnabled(true);
                    approveTransaction.setVisibility(View.VISIBLE);
                    rejectTransaction.setEnabled(true);
                    rejectTransaction.setVisibility(View.VISIBLE);
                    returnedTransaction.setEnabled(false);
                    returnedTransaction.setVisibility(View.GONE);
                }

                cancelTransaction.setEnabled(false);
                cancelTransaction.setVisibility(View.GONE);
                fromTo.setText("to:");
                otherUserName.setText(transactionRequest.getBorrow_user_name());
            }
            else{
                approveTransaction.setEnabled(false);
                approveTransaction.setVisibility(View.GONE);
                rejectTransaction.setEnabled(false);
                rejectTransaction.setVisibility(View.GONE);
                cancelTransaction.setEnabled(true);
                cancelTransaction.setVisibility(View.VISIBLE);
                returnedTransaction.setEnabled(false);
                returnedTransaction.setVisibility(View.GONE);
                fromTo.setText("from:");
                otherUserName.setText((transactionRequest.getLend_user_name()));
            }
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
        void onApproveClick(int position);
        void onRejectClick(int position);
        void onCancelClick(int position);
        void onReturnClick(int position);
    }

    class TransactionListAdapter extends RecyclerView.Adapter<TransactionItemViewHolder>{
        private OnItemClickListener listener;
        Boolean isBorrowedFromMe = true;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }
        void SetBorrowedFromMe(Boolean isBorrowedFromMe){
            this.isBorrowedFromMe = isBorrowedFromMe;
        }
        @NonNull
        @Override
        public TransactionItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.transaction_item, viewGroup,false );
            TransactionItemViewHolder vh = new TransactionItemViewHolder(v,isBorrowedFromMe, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionItemViewHolder holder, int position) {
            TransactionRequest tr = data.get(position);
            holder.SetBorrowedFromMe(isBorrowedFromMe);
            holder.bind(tr);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
