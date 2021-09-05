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

public class ExposureTool extends StudioTool {

    private ExposureHandler mExposureHander;
    private int mValue = 0;

    public interface ExposureHandler {
        void exposureFilter(int value);
        Bitmap getBitmap();
    }

    public ExposureTool (StudioToolManager toolManager, ExposureHandler ExposureHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Exposure", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.exposure));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.exposure_tool_options, null);
        mExposureHander = ExposureHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        RangeSlider slider = mToolOptions.findViewById(R.id.exposureValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                if (fromUser) {
//                    Fucking lag :/
                    mValue = Float.valueOf(value).intValue();
                   // debug.setText("" + value);
                    Log.d("EXPOSURE", "" + value);
                    updateBitmap();
                }
            }
        });
    }
    public void updateBitmap () {
//        Do sth, then
        mExposureHander.exposureFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mExposureHander.getBitmap(), false);
    }
}
