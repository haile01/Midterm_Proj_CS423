package com.example.midterm_proj;

import android.os.Bundle;

import com.example.midterm_proj.ui.main.PhotoDate;
import com.example.midterm_proj.ui.main.Photo;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.midterm_proj.ui.main.SectionsPagerAdapter;
import com.example.midterm_proj.databinding.ActivityMainBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

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