package com.example.midterm_proj;

import android.graphics.Bitmap;

import com.example.midterm_proj.ui.main.ChangeBitmapHandler;

public class StudioImageManager {

    private Bitmap mBitmap;
    private ChangeBitmapHandler mChangeBitmapHandler;

    public StudioImageManager () {
//        Do sth
    }

    public void setBitmap (Bitmap bitmap) {
        mBitmap = bitmap;
        mChangeBitmapHandler.changeBitmap(bitmap, true);
    }

    public void setChangeBitmapHandler (ChangeBitmapHandler handler) {
        mChangeBitmapHandler = handler;
    }
}
