package com.example.midterm_proj.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.midterm_proj.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {

    private SinglePhotoView mPopupView = new SinglePhotoView();
    private PopupWindow mPopupWindow;
    private ViewGroup mContainer;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_photos, container, false);
        mContainer = container;
        initialize(root);
        return root;
    }

    private void initialize(View root) {
        mPopupView.setView(LayoutInflater.from(getActivity()).inflate(R.layout.single_photo_view, null));
        mPopupWindow = new PopupWindow(mPopupView.getView(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupView.initialize(mPopupWindow);

        Button openSinglePhoto = root.findViewById(R.id.openSinglePhoto);
        openSinglePhoto.setOnClickListener(this::openSinglePhoto);
    }

    public void openSinglePhoto (View v) {
        mPopupWindow.showAsDropDown(mContainer, 0, 0);
    }
}