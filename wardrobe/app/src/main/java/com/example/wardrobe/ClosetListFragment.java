package com.example.wardrobe;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wardrobe.model.Model;
import com.example.wardrobe.model.entities.Garment;

import java.util.List;


public class ClosetListFragment extends Fragment {
    RecyclerView list;
    List<Garment> data;

    interface Delegate{
        void onItemSelected(Garment student);
    }

    Delegate parent;

    public ClosetListFragment() {
        data = Model.instance.getAllGarments();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Delegate) {
            parent = (Delegate) getActivity();
        } else {
            throw new RuntimeException(context.toString()
                    + "student list parent activity must implement dtudent ;list fragment Delegate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_closet_list, container, false);
        list = view.findViewById(R.id.closet_list_list);
        list.setHasFixedSize(true);

        GridLayoutManager  layoutManager = new GridLayoutManager(getContext(),3);
        list.setLayoutManager(layoutManager);

        GarmentListAdapter adapter = new GarmentListAdapter();
        list.setAdapter(adapter);

        adapter.setOnIntemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG","row was clicked" + position);
                Garment garment = data.get(position);
                parent.onItemSelected(garment);
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
            id = itemView.findViewById(R.id.garment_item_id_tv);
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
            id.setText(gr.id);
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