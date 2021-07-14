package com.example.midterm_proj;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.midterm_proj.ui.main.SizeConfig;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.midterm_proj.ui.main.SectionsPagerAdapter;
import com.example.midterm_proj.databinding.ActivityMainBinding;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageViewModel mImageViewModel;
    private ActivityMainBinding binding;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private final Context mContext = this;

    protected void onActivityResult(int requestCode,
                                    String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults != null && grantResults.length == 2
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Cấp quyền thất bại", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), new ArrayList<Image>());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                123);

        //ViewModel - observer
        mImageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        final Observer<List<Image>> imageObserver = new Observer<List<Image>> () {
            @Override
            public void onChanged(@Nullable List<Image> imageList) {
                mSectionsPagerAdapter = new SectionsPagerAdapter(mContext, getSupportFragmentManager(), imageList);
                binding.viewPager.setAdapter(mSectionsPagerAdapter);
            }
        };
        mImageViewModel.getAllImages().observe(this, imageObserver);
        initSize();

    }

    private void initSize(){
        WindowManager w = getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        w.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        SizeConfig.init(height, width);

        // test
        SizeConfig size = new SizeConfig();
        //Toast.makeText(getApplicationContext(), Integer.toString(size.getWidth()), Toast.LENGTH_LONG).show();
    }
}