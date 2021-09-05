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

public class BrushTool extends StudioTool {

    private BrushHandler mBrushHander;

    public interface BrushHandler {
        void handleBrush();
        Bitmap getBitmap();
    }

    public BrushTool (StudioToolManager toolManager, BrushHandler BrushHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Brush", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.brush));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.brush_tool_options, null);
        mBrushHander = BrushHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mBrushHander.handleBrush();
        mChangeBitmapHandler.changeBitmap(mBrushHander.getBitmap(), false);
    }
}
