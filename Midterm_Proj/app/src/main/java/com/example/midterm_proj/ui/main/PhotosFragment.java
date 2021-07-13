package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.midterm_proj.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

interface OpenPopupHandler {
    void openSinglePhoto(int position);
}

public class PhotosFragment extends Fragment implements OpenPopupHandler {

    private SinglePhotoView mPopupView = new SinglePhotoView();
    private PopupWindow mPopupWindow;
    private ViewGroup mContainer;


    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    List<PhotoDate> photosDate ;
    LinearLayout rootLayout;
    SimpleDateFormat ft;

    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View root = inflater.inflate(R.layout.fragment_photos, container, false);
        mContainer = container;
        initialize(root);
        return root;
    }

    @SuppressLint({"SimpleDateFormat", "ResourceAsColor"})
    private void initialize(View root) {
        mPopupView.setView(LayoutInflater.from(getActivity()).inflate(R.layout.single_photo_view, null));
        mPopupWindow = new PopupWindow(mPopupView.getView(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mPopupView.initialize(mPopupWindow);

//        Button openSinglePhoto = root.findViewById(R.id.openSinglePhoto);
//        openSinglePhoto.setOnClickListener(this::openSinglePhoto);

        ft = new SimpleDateFormat("yyyy-MM-dd");
        photosDate = getPhotoDates();
        Toast.makeText(getContext(), Integer.toString(photosDate.get(0).photos.size()), Toast.LENGTH_LONG).show();
        recyclerView = root.findViewById(R.id.news_rv);
        adapter = new RecyclerViewAdapter(this, photosDate);
        adapter.setOpenPopupHandler((OpenPopupHandler) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        rootLayout = root.findViewById(R.id.layout_outside);
//        rootLayout.setBackgroundColor(R.color.white);
    }

    List<PhotoDate> getPhotoDates() {
        List<Photo> photos = new ArrayList<Photo>();
        photos.add(new Photo(R.drawable.dog1));
        photos.add(new Photo(R.drawable.dog2));
        photos.add(new Photo(R.drawable.dog3));
        photos.add(new Photo(R.drawable.dog1));
        photos.add(new Photo(R.drawable.dog2));
        photos.add(new Photo(R.drawable.dog3));
        photos.add(new Photo(R.drawable.dog1));

        List<PhotoDate> photosDate = new ArrayList<PhotoDate>();
        try {
            photosDate.add(new PhotoDate(ft.parse("2021-7-12"), photos));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            photosDate.add(new PhotoDate(ft.parse("2021-7-13"), photos));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            photosDate.add(new PhotoDate(ft.parse("2020-11-11"), photos));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            photosDate.add(new PhotoDate(ft.parse("2019-11-11"), photos));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return photosDate;
    }

    public void openSinglePhoto (int position) {
        mPopupView.setPosition(position);
        mPopupWindow.showAsDropDown(mContainer, 0, 0);
    }
}