package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;
import com.google.android.material.slider.RangeSlider;

import org.jetbrains.annotations.NotNull;

public class BrightTool extends StudioTool {

    private BrightHandler mBrightHander;
    private int mValue = 0; // 0 to 255

    public interface BrightHandler {
        void brightFilter(int value);
        Bitmap getBitmap();
    }

    private TextView debug;

    public BrightTool (StudioToolManager toolManager, BrightHandler BrightHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Bright", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.brighten));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mBrightHander = BrightHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.bright_tool_options, null);
        debug = mToolOptions.findViewById(R.id.brightValueDebug);
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        RangeSlider slider = mToolOptions.findViewById(R.id.brightValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
            if (mBrightHander.getBitmap() != null){
                if (fromUser) {
//                    Fucking lag :/
                    mValue = Float.valueOf(value).intValue();
                    debug.setText("" + mValue);
                    Log.d("BRIGHT", "" + mValue);
                    updateBitmap();
                }
            }
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mBrightHander.brightFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mBrightHander.getBitmap());
    }
}