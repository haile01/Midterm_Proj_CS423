package com.example.midterm_proj.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.midterm_proj.FaceID.ClusterViewAdapter;
import com.example.midterm_proj.FaceID.FaceCluster;
import com.example.midterm_proj.FaceID.FaceIDManager;
import com.example.midterm_proj.Image;
import com.example.midterm_proj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAlbumFragment extends Fragment {

    private RecyclerView mClusterRecyclerView;
    private ClusterViewAdapter mClusterViewAdapter;
    private FaceIDManager mFaceIDManager;

    public FaceAlbumFragment() {
        // Required empty public constructor
    }
    public static FaceAlbumFragment newInstance() {
        FaceAlbumFragment fragment = new FaceAlbumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_face_album, container, false);
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        mClusterViewAdapter = new ClusterViewAdapter(getContext());

//        Prepare FaceIDManager
        mFaceIDManager = FaceIDManager.getInstance(getContext(), getContext().getContentResolver());
        mFaceIDManager.getClusterList().observe(getViewLifecycleOwner(), new Observer<ArrayList<FaceCluster>>() {
            @Override
            public void onChanged(ArrayList<FaceCluster> faceClusters) {
                Log.d("FaceAlbumFragment::initialize", "changed " + faceClusters.size() + " " + mFaceIDManager.getFaceList().getValue().size());
                mClusterViewAdapter.setFaceClusterList(faceClusters);
                mClusterViewAdapter.setFaceList(mFaceIDManager.getFaceList().getValue());
                mClusterViewAdapter.notifyDataSetChanged();
            }
        });

        mClusterRecyclerView = root.findViewById(R.id.clusterContainer);

        mClusterRecyclerView.setAdapter(mClusterViewAdapter);
        mClusterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setImageList(List<Image> imageList) {
        ArrayList<Image> list = (ArrayList<Image>) imageList;
        list = new ArrayList<>(list.subList(0, 10));
        mFaceIDManager.updateFaceId(list);
        Log.d("FaceAlbumFragment::setImageList", "DONE???");
    }
}