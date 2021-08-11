package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;

public class SaturationTool extends StudioTool {

    private SaturationHandler mSaturationHander;
    private int value = 0;

    public interface SaturationHandler {
        void saturationFilter(int value);
        Bitmap getBitmap();
    }

    public SaturationTool (StudioToolManager toolManager, SaturationHandler SaturationHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Saturation", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.saturation));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.saturation_tool_options, null);
        mSaturationHander = SaturationHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mSaturationHander.saturationFilter(value);
        mChangeBitmapHandler.changeBitmap(mSaturationHander.getBitmap());
    }
}
