package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

// https://www.youtube.com/watch?v=dNlql4zVMEw
// https://stackoverflow.com/questions/24471109/recyclerview-onclick/26196831#26196831

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<MarkerOptions> markedList;
    private LayoutInflater mInflator;
    private static itemClickListener itemListener;

    // interface for click listener
    public interface itemClickListener{
        public void onItemClick(View view, int position);
    }

    // adaptor constructor
    public MainAdapter(Context context, ArrayList<MarkerOptions> addresses, itemClickListener listen){
        mInflator = LayoutInflater.from(context);
        markedList = addresses;
        itemListener = listen;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, final int position) {
        String address = markedList.get(position).getTitle();
        holder.textView.setText(address);
    }

    @Override
    public int getItemCount() {
        return markedList.size();
    }

    // viewholder design pattern
    // holds reference to view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview_addresses);

            // listens to clicks to view in recycler view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = ViewHolder.super.getAdapterPosition();

                    itemListener.onItemClick(view, position);
                }
            });
        }
    }
}
