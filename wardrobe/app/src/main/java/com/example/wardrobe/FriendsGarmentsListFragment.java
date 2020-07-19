package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wardrobe.model.GarmentsModel;
import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.Garment;
import com.example.wardrobe.model.entities.User;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class FriendsGarmentsListFragment extends Fragment {
    String owner_id="";
    RecyclerView list;
    List<Garment> data = new LinkedList<Garment>();
    GarmentListAdapter adapter;
    GarmentsViewModel viewModel;
    LiveData<List<Garment>> liveData;
    TextView emptyTextView;
    TextView OwnerTextView;
    ImageView OwnerPicture;
    UsersViewModel usersViewModel;
    User Owner;

    interface Delegate{
        void onItemSelected(Garment student);
    }

    Delegate parent;

    public FriendsGarmentsListFragment() {
//        GarmentsModel.instance.getAllGarments(new GarmentsModel.GetAllGarmentsListener() {
//            @Override
//            public void onComplete(List<Garment> _data) {
//                data = _data;
//                if(adapter != null) {
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        viewModel = new ViewModelProvider(this).get(GarmentsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_closet_list, container, false);
        list = view.findViewById(R.id.closet_list_list);
        list.setHasFixedSize(true);

        GridLayoutManager  layoutManager = new GridLayoutManager(getContext(),3);
        list.setLayoutManager(layoutManager);

        emptyTextView = view.findViewById(R.id.empty_closet);
        emptyTextView.setVisibility(View.GONE);
        OwnerTextView = view.findViewById(R.id.closet_user_name);
        OwnerPicture = view.findViewById(R.id.img_closet_list);

        adapter = new GarmentListAdapter();
        list.setAdapter(adapter);

        adapter.setOnIntemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Garment garment = data.get(position);
                //parent.onItemSelected(garment);
                FriendsGarmentsListFragmentDirections.ActionFriendsGarmentsListFragmentToFriendsGarmentsDetailsFragment direction = FriendsGarmentsListFragmentDirections.actionFriendsGarmentsListFragmentToFriendsGarmentsDetailsFragment(garment);
                Navigation.findNavController(view).navigate(direction);
            }
        });

        owner_id = FriendsGarmentsListFragmentArgs.fromBundle(getArguments()).getOwnerId();
        viewModel.SetOwnerId(owner_id);
        if(owner_id != "") {
            usersViewModel.getUser(owner_id, new UsersModel.Listener<User>() {
                @Override
                public User onComplete(User user) {
                    Owner = user;
                    OwnerTextView.setText(user.getName());
                    if(user.getImg_url() != null && user.getImg_url() != "") {
                        Picasso.get().load(user.getImg_url()).into(OwnerPicture);
                    }
                    return user;
                }
            });
        }
        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Garment>>() {
            @Override
            public void onChanged(List<Garment> garments) {
                if(garments.isEmpty()) {
                    emptyTextView.setVisibility(View.VISIBLE);
                }
                data = garments;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.closet_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new GarmentsModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }



    static class GarmentItemViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        ImageView image;
        Garment garment;

        public GarmentItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            image = itemView.findViewById(R.id.garment_item_image);

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

        public void bind(Garment gr) {
            if (gr.getImageUrl() != null && gr.getImageUrl() != "") {
                Picasso.get().load(gr.getImageUrl()).into(image);
            }

            garment = gr;
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class GarmentListAdapter extends RecyclerView.Adapter<GarmentItemViewHolder>{
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public GarmentItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.garment_item, viewGroup,false );
            GarmentItemViewHolder vh = new GarmentItemViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull GarmentItemViewHolder garmentItemViewHolder, int position) {
            Garment gr = data.get(position);
            garmentItemViewHolder.bind(gr);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
