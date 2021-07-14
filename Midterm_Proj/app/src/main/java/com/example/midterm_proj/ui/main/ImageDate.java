package com.example.midterm_proj.ui.main;

import com.example.midterm_proj.Image;

import java.util.Date;
import java.util.List;

public class ImageDate {
    private Date date;
    List<Image> mImageList;

    public ImageDate(Date date, List<Image> imageList) {
        this.date = date;
        mImageList = imageList;
    }

    public Date getDate() {
        return date;
    }

    public List<Image> getImageList() {
        return mImageList;
    }
}
