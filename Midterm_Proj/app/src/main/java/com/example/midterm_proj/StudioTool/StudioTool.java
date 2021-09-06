package com.example.midterm_proj.StudioTool;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;

public class StudioTool {
    private LinearLayout mToolOptionsContainer;
    private StudioToolManager mToolManager;
    public LayoutInflater mInflater;
    public LinearLayout mToolOptions;
    public Drawable btnIcon;
    public String btnText;
    public ChangeBitmapHandler mChangeBitmapHandler;

    public StudioTool (StudioToolManager toolManager, String text, Drawable icon) {
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mInflater = toolManager.mInflater;
        mToolOptionsContainer = toolManager.mToolOptionsView;
        mToolManager = toolManager;
        btnText = text;
        btnIcon = icon;
    }

    public void renderToolOptions () {
        if (mToolOptionsContainer != null) {
            mToolOptionsContainer.removeAllViews();
            mToolOptionsContainer.addView(mToolOptions);
        }
    }

    public void choose() {
//        Do sth here
        if (mToolManager.currentTool != null) {
            mToolManager.currentTool.cancel();
        }
        mToolManager.currentTool = this;
        renderToolOptions();
    }

    public View inflateButton() {
        View btnView = mInflater.inflate(R.layout.single_tool_button, null);
        Button btn = (Button) btnView.findViewById(R.id.toolBtn);
        btn.setText(btnText);
        btn.setCompoundDrawablesWithIntrinsicBounds(null, btnIcon, null, null);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose();
            }
        });
        return btnView;
    }

//    Draws additional thingy onto the canvas (i.e. grid lines in Crop)
//    Override these or they'll do nothing
    public void drawCanvas(Canvas canvas, Matrix matrix) {
    }

    public void beginDrag(float x, float y) {
    }

    public void processDrag(float x, float y) {
    }

    public void endDrag(float x, float y) {
    }

//    Pressing X button
    public void cancel () {
        mToolManager.unChoose();
        if (mToolOptionsContainer != null) {
            mToolOptionsContainer.removeAllViews();
        }
    }

//    Pressing V button
    public void commit () {
        mToolManager.mViewModel.commit();
        mToolManager.unChoose();
        if (mToolOptionsContainer != null) {
            mToolOptionsContainer.removeAllViews();
        }
    }
}
