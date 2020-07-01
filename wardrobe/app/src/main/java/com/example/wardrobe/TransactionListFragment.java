package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.LinkedList;
import java.util.List;


public class TransactionListFragment extends Fragment {
    RecyclerView list;
    List<TransactionRequest> data = new LinkedList<TransactionRequest>();
    TransactionListAdapter adapter;
    TransactionListViewModel viewModel;
    LiveData<List<TransactionRequest>> liveData;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_transaction_list, container, false);
        list = view.findViewById(R.id.transactions_list);
        list.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        list.setLayoutManager(layoutManager);

        adapter = new TransactionListAdapter();
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
        });

        LentButton = view.findViewById(R.id.button_lent);

        BorrowedButton = view.findViewById(R.id.button_borewed);

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<TransactionRequest>>() {
            @Override
            public void onChanged(List<TransactionRequest> transactionRequests) {
                data = transactionRequests;
                adapter.notifyDataSetChanged();
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
                onLentToMeButtonClick(v);
            }
        });

        BorrowedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBorrwedFromMeButtonClick(v);
            }
        });

        return view;
    }


    private void onLentToMeButtonClick(View view){
        viewModel.SetBorrowedFromMe(false);
        liveData = viewModel.getData();

        viewModel.refresh(new TransactionRequestsModel.CompListener() {
            @Override
            public void onComplete() {
                liveData = viewModel.getData();
            }
        });
    }

    private void onBorrwedFromMeButtonClick(View view){
        viewModel.SetBorrowedFromMe(true);
        viewModel.refresh(new TransactionRequestsModel.CompListener() {
            @Override
            public void onComplete() {
                liveData = viewModel.getData();
            }
        });    }

    static class TransactionItemViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TransactionRequest transactionRequest;

        public TransactionItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.transaction_item_id);

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
        }

        public void bind(TransactionRequest tr) {
            transactionRequest = tr;
            id.setText(transactionRequest.getTransaction_id());

        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class TransactionListAdapter extends RecyclerView.Adapter<TransactionItemViewHolder>{
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public TransactionItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.transaction_item, viewGroup,false );
            TransactionItemViewHolder vh = new TransactionItemViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionItemViewHolder holder, int position) {
            TransactionRequest tr = data.get(position);
            holder.bind(tr);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
