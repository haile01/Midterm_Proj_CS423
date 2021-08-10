package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class StudioToolManager {

    private Context mContext;
    private LinearLayout mToolBtnView;
    private LayoutInflater mInflater;
    private LinearLayout mRootView;
    private String currentTool = "";

//    Tools
    private CropTool mCropTool;

    public StudioToolManager (LayoutInflater inflater, LinearLayout rootView, LinearLayout toolBtnView, Context context) {
        mInflater = inflater;
        mRootView = rootView;
        mToolBtnView = toolBtnView;
        mContext = context;
        initialize();
    }

    private void initialize() {
        currentTool = "";
        mCropTool = new CropTool(mInflater, mRootView, mContext);
//        Add more tools here

//        Attach to toolBtnView
        mToolBtnView.removeAllViews();
        mToolBtnView.addView(((StudioTool) mCropTool).inflateButton());
    }

    public void chooseTool (String toolName) {
        switch (toolName) {
            case "crop": {
                currentTool = toolName;
                mCropTool.choose();
            }
            default: {
//                Do nothing
            }
        }
    }

    public StudioTool getCurrentTool () {
        switch (currentTool) {
            case "crop": {
                return mCropTool;
            }
            default: {
//                Some error
                return null;
            }
        }
    }
}
