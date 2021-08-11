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

public class BrightTool extends StudioTool {

    private BrightHandler mBrightHander;
    private int value = 0;

    public interface BrightHandler {
        void brightFilter(int value);
        Bitmap getBitmap();
    }

    public BrightTool (StudioToolManager toolManager, BrightHandler BrightHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Bright", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.brighten));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.bright_tool_options, null);
        mBrightHander = BrightHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mBrightHander.brightFilter(value);
        mChangeBitmapHandler.changeBitmap(mBrightHander.getBitmap());
    }
}
