package com.example.midterm_proj.StudioTool;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

public class SharpenTool extends StudioTool {

    private boolean value = true;
    private final SharpenHandler mSharpenHandler;

    @Override
    public void choose() {
        super.choose();
        value = true;
        updateBitmap();
    }

    @Override
    public void cancel() {
        updateBitmap();
        super.cancel();
    }

    public interface SharpenHandler {
        void sharpenFilter(boolean value);
        Bitmap getBitmap();
    }

    public SharpenTool (StudioToolManager toolManager, SharpenHandler SharpenHandler) {
        super(toolManager, "Sharpen", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.sharpen));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.sharpen_tool_options, null);
        mSharpenHandler = SharpenHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        ImageButton cancelBtn = mToolOptions.findViewById(R.id.sharpenCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = false;
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.sharpenCommit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = true;
                commit();
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mSharpenHandler.sharpenFilter(value);
        mChangeBitmapHandler.changeBitmap(mSharpenHandler.getBitmap(), false);
    }
}
