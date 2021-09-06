package com.example.midterm_proj.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.midterm_proj.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaceAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAlbumFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_face_album, container, false);
    }
}