package com.example.midterm_proj.ui.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.midterm_proj.ChangeTabHandler;
import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioCanvasView;
import com.example.midterm_proj.StudioImageManager;

public class StudioFragment extends Fragment implements StudioImageManager.OnChangeBitmapHandler {

    private ViewGroup mContainer;
    private ChangeTabHandler mChangeTabHander;
    private Bitmap mBitmap;
    private LayoutInflater mInflater;
    private ViewGroup mRootView;
    private StudioImageManager mStudioImageManager;
    private View mEmptyBitmapView;
    private LinearLayout mContentView;
    private StudioCanvasView mBitmapCanvasView;

    public StudioFragment () {
//        Empty constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_studio, container, false);
        mInflater = inflater;
        mContainer = container;
        mRootView = (ViewGroup) root;
        mContentView = mRootView.findViewById(R.id.studioContentContainer);

        initialize();
        return root;
    }

    private void initialize() {
        mEmptyBitmapView = mInflater.inflate(R.layout.empty_bitmap, mRootView, false);
        mBitmapCanvasView = new StudioCanvasView(mRootView.getContext());
        renderEmptyBitmap();
        attachCancelButton();
    }

    private void attachCancelButton () {
        Button cancelBtn = (Button) mRootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancel();
            }
        });
    }

    public void handleCancel () {
        mBitmapCanvasView.cancel();
        renderEmptyBitmap();
        mChangeTabHander.setTab(0);
    }

    private void renderEmptyBitmap () {
        mContentView.removeAllViews();
        mContentView.addView(mEmptyBitmapView);
    }
    
    private void renderBitmap () {
        mContentView.removeAllViews();
        mContentView.addView(mBitmapCanvasView);
    }

    public static StudioFragment newInstance() {
        StudioFragment fragment = new StudioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setChangeTabHandler(ChangeTabHandler handler) {
        mChangeTabHander = handler;
    }

    public void setStudioImageManager(StudioImageManager manager) {
        mStudioImageManager = manager;
        manager.setOnChangeBitmapHandler(this);
    }

    @Override
    public void changeBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mBitmapCanvasView.setBitmap(bitmap);
        renderBitmap();
    }
}
