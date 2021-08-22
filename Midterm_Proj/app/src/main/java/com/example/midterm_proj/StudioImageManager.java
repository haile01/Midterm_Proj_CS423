package com.example.midterm_proj;

import android.graphics.Bitmap;

public class StudioImageManager {

    private Bitmap mBitmap;
    private OnChangeBitmapHandler mOnChangeBitmapHandler;

    public interface OnChangeBitmapHandler {
        void changeBitmap (Bitmap bitmap);
    }

    public StudioImageManager () {
//        Do sth
    }

    public void setBitmap (Bitmap bitmap) {
        mBitmap = bitmap;
        mOnChangeBitmapHandler.changeBitmap(bitmap);
    }

    public void setOnChangeBitmapHandler (OnChangeBitmapHandler handler) {
        mOnChangeBitmapHandler = handler;
    }
}
