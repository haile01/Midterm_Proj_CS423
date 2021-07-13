package com.example.midterm_proj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.midterm_proj.ui.main.PhotoDate;
import com.example.midterm_proj.ui.main.Photo;
import com.example.midterm_proj.ui.main.SizeConfig;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    private ActivityMainBinding binding;

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

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                123);

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
        Toast.makeText(getApplicationContext(), Integer.toString(size.getWidth()), Toast.LENGTH_LONG).show();
    }

//    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
//    List<PhotoDate> getPhotoDates() throws ParseException {
//        List<Photo> photos = new ArrayList<Photo>();
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//        photos.add(new Photo(R.drawable.dog1));
//
//        List<PhotoDate> photosDate = new ArrayList<PhotoDate>();
//        photosDate.add(new PhotoDate(ft.parse("2020-11-11"), photos));
//        photosDate.add(new PhotoDate(ft.parse("2021-11-11"), photos));
//        photosDate.add(new PhotoDate(ft.parse("2020-11-11"), photos));
//        photosDate.add(new PhotoDate(ft.parse("2019-11-11"), photos));
//
//        return photosDate;
//    }
}