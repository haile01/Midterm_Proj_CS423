package com.example.midterm_proj.ui.main;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import com.example.midterm_proj.BitmapFilter;

public class StudioFragmentViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private Bitmap bitmapToProcess = null;

    public Bitmap exposureFilter(double value) {
        bitmapToProcess = BitmapFilter.exposure(bitmapToProcess, value);
        return getBitmap();
    }

    public void contrastFilter() {
    }

    public void handleText() {
    }

    public void handleCrop() {
    }

    public void handleBrush() {
    }

    public void sharpenFilter() {
    }

    public void saturationFilter() {
    }

    public void brightFilter() {
    }


    public void setImageBitmap(Bitmap mImageBitmap) {
        bitmapToProcess.recycle();
        bitmapToProcess = null;
        bitmapToProcess = mImageBitmap;
    }

    public Bitmap getBitmap() {
        return bitmapToProcess;
    }
}