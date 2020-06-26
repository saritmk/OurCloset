package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wardrobe.model.FriendshipModel;
import com.example.wardrobe.model.entities.Friendship;

import java.util.LinkedList;
import java.util.List;



public class FriendsListFragment extends Fragment {
    RecyclerView list;
    List<Friendship> data = new LinkedList<Friendship>();
    FriendshipListAdapter adapter;
    FriendsListViewModel viewModel;
    LiveData<List<Friendship>> liveData;

    interface Delegate{
        void onItemSelected(Friendship friend);
    }

    Delegate parent;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof Delegate) {
//            parent = (Delegate) getActivity();
//        } else {
//            throw new RuntimeException(context.toString()
//                    + "student list parent activity must implement dtudent ;list fragment Delegate");
//        }

        viewModel = new ViewModelProvider(this).get(FriendsListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_friends_list, container, false);
        list = view.findViewById(R.id.friends_list_list);
        list.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        list.setLayoutManager(layoutManager);

        adapter = new FriendshipListAdapter();
        list.setAdapter(adapter);

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Friendship>>() {
            @Override
            public void onChanged(List<Friendship> friends) {
                data = friends;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.friends_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new FriendshipModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }


    static class FriendshipItemViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        ImageView image;
        Friendship friendship;

        public FriendshipItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.friend_item_id_tv);
            image = itemView.findViewById(R.id.friend_item_image);

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

        public void bind(Friendship friend) {
            id.setText(friend.getId_2());
            friendship = friend;
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class FriendshipListAdapter extends RecyclerView.Adapter<FriendshipItemViewHolder>{
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public FriendshipItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.friendship_item, viewGroup,false );
            FriendshipItemViewHolder vh = new FriendshipItemViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull FriendshipItemViewHolder friendshipItemViewHolder, int position) {
            Friendship friend = data.get(position);
            friendshipItemViewHolder.bind(friend);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
