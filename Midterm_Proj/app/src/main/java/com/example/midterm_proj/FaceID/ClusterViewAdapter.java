package com.example.midterm_proj.FaceID;

import android.content.Context;
import android.media.FaceDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.ImageAdapter;
import com.example.midterm_proj.R;

import java.util.ArrayList;
import java.util.Arrays;

public class ClusterViewAdapter extends RecyclerView.Adapter <ClusterViewAdapter.ClusterViewHolder> {

    private final Context mContext;
    private ArrayList<FaceCluster> mFaceClusters;
    private ArrayList<FaceID> mFaces;

    public ClusterViewAdapter (Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ClusterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View clusterContainer = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cluster_item, parent, false);
        ClusterViewHolder viewHolder = new ClusterViewHolder(clusterContainer, this);
        viewHolder.mFaceViewAdapter = new FaceViewAdapter(mContext);
        viewHolder.faceView.setAdapter(viewHolder.mFaceViewAdapter);
        viewHolder.faceView.setLayoutManager(new LinearLayoutManager(mContext));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClusterViewHolder holder, int position) {
        holder.setCluster(mFaceClusters.get(position), mFaces);

        ArrayList <FaceID> tmpList = new ArrayList<>();
        for (int id : mFaceClusters.get(position).faceId) {
            tmpList.add(mFaces.get(id));
        }
        holder.mFaceViewAdapter.setFaceList(tmpList);
    }

    @Override
    public int getItemCount() {
        if (mFaceClusters == null)
            return 0;
        return mFaceClusters.size();
    }

    public void setFaceClusterList(ArrayList<FaceCluster> faceClusters) {
        mFaceClusters = faceClusters;
    }

    public void setFaceList(ArrayList<FaceID> faces) {
        mFaces = faces;
    }

    public class ClusterViewHolder extends RecyclerView.ViewHolder {

        ClusterViewAdapter mClusterViewAdapter;
        RecyclerView faceView;
        TextView clusterInfo;
        FaceViewAdapter mFaceViewAdapter;

        public ClusterViewHolder(@NonNull View itemView, ClusterViewAdapter clusterViewAdapter) {
            super(itemView);
            mClusterViewAdapter = clusterViewAdapter;
            faceView = itemView.findViewById(R.id.faceContainer);
            clusterInfo = itemView.findViewById(R.id.clusterInfo);
        }

        public void setCluster(FaceCluster faceCluster, ArrayList<FaceID> mFaces) {
            String info = "" + faceCluster.getID();
            clusterInfo.setText(info);
        }
    }
}
