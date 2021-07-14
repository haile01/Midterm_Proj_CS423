package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridRecyclerHolder> {

    private final List<Image> mImageList;
    private Context mContext;
    OpenPopupHandler mOpenPopupHandler;
    SizeConfig size;

    public GridRecyclerAdapter(Context c, List<Image> imageList) {
        this.mContext = c;

        this.mImageList = imageList;
        size = new SizeConfig();
    }

    @NonNull
    @Override
    public GridRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new GridRecyclerHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecyclerHolder holder, int position) {
        holder.photo.setLayoutParams(new GridView.LayoutParams(size.getWidth() / size.getNumOfImagesRow(), size.getWidth() / size.getNumOfImagesRow()));
        holder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.photo.setImageResource(R.drawable.dog1);
//        holder.photo.setOnClickListener(v -> {
//            if (mOpenPopupHandler != null) {
//                Toast.makeText(mContext, mImageList.get(position).getUri().toString(), Toast.LENGTH_LONG).show();
//                mOpenPopupHandler.openSinglePhoto(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public String getName(){
        return mImageList.get(0).getUri().toString();
    }

    public int getSize() {
        return mImageList.size();
    }

    public static class GridRecyclerHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public GridRecyclerHolder(@NonNull View itemView)
        {
            super(itemView);
            photo = itemView.findViewById(R.id.view_image);
        }
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }
}
