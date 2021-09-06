package com.example.midterm_proj.FaceID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.R;

import java.util.ArrayList;
import java.util.Arrays;

public class FaceViewAdapter extends RecyclerView.Adapter<FaceViewAdapter.FaceViewHolder> {
    private final Context mContext;
    private ArrayList<FaceID> mFaceList;

    public FaceViewAdapter (Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public FaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View faceContainer = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.face_item, parent, false);
        FaceViewHolder viewHolder = new FaceViewHolder(faceContainer, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FaceViewHolder holder, int position) {
        holder.setFace(mFaceList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFaceList.size();
    }

    public void setFaceList(ArrayList<FaceID> faceList) {
        mFaceList = faceList;
    }

    public class FaceViewHolder extends RecyclerView.ViewHolder {

        FaceViewAdapter mAdapter;
        ImageView faceImage;
        TextView faceInfo;

        public FaceViewHolder(@NonNull View itemView, FaceViewAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
            faceImage = itemView.findViewById(R.id.faceImage);
            faceInfo = itemView.findViewById(R.id.faceInfo);
        }

        public void setFace(FaceID faceID) {
//            faceImage.setImageURI(faceID.mImageUri);
            faceImage.setImageBitmap(faceID.mBitmap);

            String info = "";
            info += "Uri: " + faceID.mImageUri + "\n";
            info += "Cluster ID: " + faceID.clusterID;

            faceInfo.setText(info);
        }
    }
}
