package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;
import com.google.android.material.slider.RangeSlider;

import org.jetbrains.annotations.NotNull;

public class SharpenTool extends StudioTool {

    private SharpenHandler mSharpenHander;
    private int mValue = 0;

    public interface SharpenHandler {
        void sharpenFilter(int i);
        Bitmap getBitmap();
    }

    public SharpenTool (StudioToolManager toolManager, SharpenHandler SharpenHandler) {
        super(toolManager, "Sharpen", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.sharpen));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.sharpen_tool_options, null);
        mSharpenHander = SharpenHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        RangeSlider slider = mToolOptions.findViewById(R.id.sharpenValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                if (mSharpenHander.getBitmap() != null){
                    if (fromUser) {
                        mValue = Float.valueOf(value).intValue();
                        // debug.setText("" + value);
                        Log.d("SHARPEN", "" + value);
                        updateBitmap();
                    }
                }
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mSharpenHander.sharpenFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mSharpenHander.getBitmap(), false);
    }
}
