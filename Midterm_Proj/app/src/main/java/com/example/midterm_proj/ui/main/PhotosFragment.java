package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

interface OpenPopupHandler {
    void openSinglePhoto(int position);
}

public class PhotosFragment extends Fragment implements OpenPopupHandler {

    private SinglePhotoView mPopupView = new SinglePhotoView();
    private PopupWindow mPopupWindow;
    private ViewGroup mContainer;
    private List<Image> mImageList = new ArrayList<Image>();
    private List<ImageDate> mImageDateList = new ArrayList<ImageDate>();

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;
//    List<PhotoDate> photosDate ;
    LinearLayout rootLayout;
    SimpleDateFormat ft;

    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance(List<Image> imageList) {
        PhotosFragment fragment = new PhotosFragment();
        fragment.preInit(imageList);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void preInit (List<Image> imageList) {
        mImageList = imageList;
        if (mPopupWindow != null) {
            mPopupView.initialize(mPopupWindow, mImageList);
        }
        if (recyclerView != null) {
            updateImageListByDate();
            adapter = new RecyclerViewAdapter(getContext(), mImageDateList);
            adapter.setOpenPopupHandler((OpenPopupHandler) this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
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
        mPopupWindow = new PopupWindow(mPopupView.getView(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mPopupView.initialize(mPopupWindow, mImageList);

//        Button openSinglePhoto = root.findViewById(R.id.openSinglePhoto);
//        openSinglePhoto.setOnClickListener(this::openSinglePhoto);

        ft = new SimpleDateFormat("yyyy-MM-dd");
        updateImageListByDate();
//        Toast.makeText(getContext(), Integer.toString(photosDate.get(0).photos.size()), Toast.LENGTH_LONG).show();
        recyclerView = root.findViewById(R.id.news_rv);
        adapter = new RecyclerViewAdapter(getContext(), mImageDateList);
        adapter.setOpenPopupHandler((OpenPopupHandler) this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        rootLayout = root.findViewById(R.id.layout_outside);
//        rootLayout.setBackgroundColor(R.color.white);
    }

    private void updateImageListByDate() {
        mImageDateList = new ArrayList<ImageDate>();
        ImageDate temp = null;
        for (int i = 0; i < mImageList.size(); i++) {
            Image image = mImageList.get(i);
            if (temp == null) {
                temp = new ImageDate(image.getDateAdded(), new ArrayList<Image>());
                temp.mImageList.add(image);
            }
            else {
                if (isSameDate(temp.getDate(), image.getDateAdded())) {
                    temp.mImageList.add(image);
                }
                else {
                    mImageDateList.add(temp);
                    temp = new ImageDate(image.getDateAdded(), new ArrayList<Image>());
                    temp.mImageList.add(image);
                }
            }
        }
        if (temp != null) {
            mImageDateList.add(temp);
        }
//        Toast.makeText(getContext(), "Total dates : " +  Integer.toString(mImageDateList.size()), Toast.LENGTH_LONG).show();
    }

    private boolean isSameDate(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public void openSinglePhoto (int position) {
        if (mImageList.size() > position) {
            mPopupView.setPosition(position);
        }
        mPopupWindow.showAsDropDown(mContainer, 0, 0);
    }
}