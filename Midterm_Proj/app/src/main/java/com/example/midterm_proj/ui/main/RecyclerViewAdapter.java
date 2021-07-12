package com.example.midterm_proj.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    PhotosFragment mContext;
    List<PhotoDate> photosDate ;
    GridViewAdapter gridViewAdapter;
    public RecyclerViewAdapter(PhotosFragment mContext, List<PhotoDate> mData) {
        this.mContext = mContext;
        this.photosDate = mData;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_by_date, parent, false);
        return new RecyclerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        gridViewAdapter = new GridViewAdapter(mContext.getContext(), photosDate.get(position).photos);
        holder.title.setText(photosDate.get(position).getDate().toString());
        holder.photos.setAdapter(gridViewAdapter);
    }

    @Override
    public int getItemCount() {
        return photosDate.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        GridView photos;

        public RecyclerViewHolder(@NonNull View itemView)
        {
            super(itemView);
              title = (TextView)itemView.findViewById(R.id.date_title); // title
              photos = (GridView)itemView.findViewById(R.id.gridview); // description of that person
              itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
