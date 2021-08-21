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

public class ContrastTool extends StudioTool {

    private ContrastHandler mContrastHander;
    private double value = 0;

    public interface ContrastHandler {
        void contrastFilter(double value);
        Bitmap getBitmap();
    }

    public ContrastTool (StudioToolManager toolManager, ContrastHandler ContrastHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Contrast", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.contrast));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.contrast_tool_options, null);
        mContrastHander = ContrastHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mContrastHander.contrastFilter(value);
        mChangeBitmapHandler.changeBitmap(mContrastHander.getBitmap(), false);
    }
}
