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

import com.example.wardrobe.model.UsersModel;
import com.example.wardrobe.model.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;



public class FriendsListFragment extends Fragment {
    RecyclerView list;
    List<User> data = new LinkedList<User>();
    FriendListAdapter adapter;
    UsersViewModel viewModel;
    LiveData<List<User>> liveData;
    String currentUserId="";

    interface Delegate{
        void onItemSelected(User friend);
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

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_friends_list, container, false);
        list = view.findViewById(R.id.friends_list_list);
        list.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
        list.setLayoutManager(layoutManager);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currUser = auth.getCurrentUser();
        if(currUser != null){
            currentUserId = currUser.getUid();
        }

        adapter = new FriendListAdapter();
        list.setAdapter(adapter);

        adapter.setOnIntemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                User friend = data.get(position);
                FriendsListFragmentDirections.ActionFriendsListFragmentToFriendsGarmentsListFragment direction = FriendsListFragmentDirections.actionFriendsListFragmentToFriendsGarmentsListFragment(friend.getUser_id());
                Navigation.findNavController(view).navigate(direction);
            }
        });

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                List<User> friends = removeOwnUserFromFriendsList(users);
                data = friends;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.friends_list_swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new UsersModel.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private List<User> removeOwnUserFromFriendsList(List<User> allUsers){
        User ownUser = null;
        List<User> friends = allUsers;

        for (User currUser : allUsers) {
            if (currUser.getUser_id().equals(this.currentUserId)) {
                ownUser = currUser;
                break;
            }
        }

        friends.remove(ownUser);

        return friends;
    }


    static class FriendItemViewHolder extends RecyclerView.ViewHolder{
        TextView id;
        TextView name;
        ImageView image;
        User friend;

        public FriendItemViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.friend_item_id_tv);
            name = itemView.findViewById(R.id.friend_item_name_tv);
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

        public void bind(User friend) {
            id.setText(friend.getUser_id());
            name.setText(friend.getName());
            if (friend.getImg_url() != null && friend.getImg_url() != "") {
                Picasso.get().load(friend.getImg_url()).into(image);
            }
            this.friend = new User();
            this.friend.setName(friend.getName());
            this.friend.setImg_url(friend.getImg_url());
            this.friend.setUser_id(friend.getUser_id());
            this.friend.setEmail(friend.getEmail());
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }

    class FriendListAdapter extends RecyclerView.Adapter<FriendItemViewHolder>{
        private OnItemClickListener listener;

        void setOnIntemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public FriendItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.friendship_item, viewGroup,false );
            FriendItemViewHolder vh = new FriendItemViewHolder(v, listener);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull FriendItemViewHolder friendItemViewHolder, int position) {
            User friend = data.get(position);
            friendItemViewHolder.bind(friend);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }


    }
}
