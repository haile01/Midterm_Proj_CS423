package com.example.midterm_proj;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.midterm_proj.ui.main.SizeConfig;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.midterm_proj.ui.main.SectionsPagerAdapter;
import com.example.midterm_proj.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OpenPopupHandler, ChangeTabHandler{

    private ImageViewModel mImageViewModel;
    private ActivityMainBinding binding;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final Context mContext = this;
    private final SinglePhotoView mPopupView = new SinglePhotoView();
    private PopupWindow mPopupWindow;
    private List<Image> mImageList = new ArrayList<Image>();
    private ViewPager mViewPager;
    private final StudioImageManager mStudioImageManager = new StudioImageManager();

    public void onRequestPermissionsResult (int requestCode,
                                    String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults != null && grantResults.length == 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initializeViewModel();
                } else {
                    Toast.makeText(MainActivity.this, "Cấp quyền thất bại", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                123);

        initializeSectionsPager();
        initializeViewModel();
        initializePopupView();
        initSize();
        BitmapFilter.getInstance(getBaseContext());
        ConvolutionMatrix.getInstance();
    }

    private void initializeSectionsPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), new ArrayList<Image>());
        mSectionsPagerAdapter.setOpenPopupHandler(this);
        mSectionsPagerAdapter.setChangeTabHandler(this);
        mSectionsPagerAdapter.setStudioImageManager(mStudioImageManager);

        mViewPager = binding.viewPager;
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(mViewPager);
    }

    private void initSize(){
        WindowManager w = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        w.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        SizeConfig.init(height, width);
    }

    void initializeViewModel() {
        //ViewModel - observer
        mImageViewModel = new ImageViewModel(this.getApplication());
        final Observer<List<Image>> imageObserver = new Observer<List<Image>> () {
            @Override
            public void onChanged(@Nullable List<Image> imageList) {
                mSectionsPagerAdapter.setImageList(imageList);
                mPopupView.initialize(mPopupWindow, imageList);
                mImageList = imageList;
            }
        };
        mImageViewModel.getAllImages().observe(this, imageObserver);
    }

    void initializePopupView() {
        mPopupView.setView(getLayoutInflater().inflate(R.layout.single_photo_view, null));
        mPopupWindow = new PopupWindow(mPopupView.getView(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        mPopupView.initialize(mPopupWindow, mImageList);
        mPopupView.setChangeTabHandler(this);
        mPopupView.setStudioImageManager(mStudioImageManager);
    }


    @Override
    public void openSinglePhoto (int position) {
        if (mImageList.size() > position) {
            mPopupView.setPosition(position);
        }
        mPopupWindow.showAsDropDown(binding.getRoot(), 0, 0);
    }

    @Override
    public void setTab(int tab) {
        mViewPager.setCurrentItem(tab);
    }
}