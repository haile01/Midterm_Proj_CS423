package com.example.midterm_proj.ui.main;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.midterm_proj.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SinglePhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SinglePhotoFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SinglePhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SinglePhotoFragment newInstance() {
        SinglePhotoFragment fragment = new SinglePhotoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SinglePhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_single_photo, container, false);
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        Button closePhoto = root.findViewById(R.id.closePhoto);
        closePhoto.setOnClickListener(this::closePhoto);
    }

    public void closePhoto(View v) {
        FragmentManager manager = ((Fragment) this).getFragmentManager();
        manager.beginTransaction().remove(this).commit();
    }
}