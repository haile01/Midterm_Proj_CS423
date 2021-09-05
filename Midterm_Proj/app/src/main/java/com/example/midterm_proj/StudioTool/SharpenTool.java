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

public class SharpenTool extends StudioTool {

    private SharpenHandler mSharpenHander;
    private int value = 0;

    public interface SharpenHandler {
        void sharpenFilter(int i);
        Bitmap getBitmap();
    }

    public SharpenTool (StudioToolManager toolManager, SharpenHandler SharpenHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Sharpen", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.sharpen));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.sharpen_tool_options, null);
        mSharpenHander = SharpenHandler;
    }

    public void updateBitmap () {
//        Do sth, then
<<<<<<< HEAD
        mSharpenHander.sharpenFilter(value);
        mChangeBitmapHandler.changeBitmap(mSharpenHander.getBitmap(), false);
=======
        mSharpenHander.sharpenFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mSharpenHander.getBitmap());
>>>>>>> parent of f858d33 (Added some additional handers for tools (draw canvas, touch hander))
    }
}
