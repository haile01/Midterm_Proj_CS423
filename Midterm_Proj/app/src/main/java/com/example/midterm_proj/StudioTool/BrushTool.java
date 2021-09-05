package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioTool.BrushToolHelper.Brush;
import com.example.midterm_proj.StudioTool.BrushToolHelper.BrushPicker;
import com.example.midterm_proj.StudioTool.BrushToolHelper.ColorPicker;
import com.example.midterm_proj.StudioTool.BrushToolHelper.Line;
import com.example.midterm_proj.StudioTool.BrushToolHelper.Pen;
import com.example.midterm_proj.StudioTool.BrushToolHelper.Polygon;

import java.util.ArrayList;
import java.util.LinkedList;


public class BrushTool extends StudioTool {

    private final BrushHandler mBrushHandler;
    private LinkedList<Brush> brushes;
    private ColorPicker mColorPicker;
    private BrushPicker mBrushPicker;

    public interface BrushHandler {
        void handleBrush(LinkedList<Brush> brushes);
        Bitmap getBitmap();
    }

    public BrushTool (StudioToolManager toolManager, BrushHandler BrushHandler) {
        super(toolManager, "Brush", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.brush));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.brush_tool_options, null);
        mBrushHandler = BrushHandler;
        brushes = new LinkedList<>();
        initializeToolOptionsUI(toolManager.mContext);
    }

    private void initializeToolOptionsUI(Context context) {
        mColorPicker = new ColorPicker(mToolOptions, context);
        mBrushPicker = new BrushPicker(mToolOptions, context);

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.brushCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.brushCommit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mBrushHandler.handleBrush(brushes);
        mChangeBitmapHandler.changeBitmap(mBrushHandler.getBitmap(), false);
    }
    private float curX;
    private float curY;

    @Override
    public void beginDrag(float x, float y) {
        super.beginDrag(x, y);
        switch (mBrushPicker.getBrushType()) {
            case "pen": brushes.add(new Pen(x, y));
            break;
            case "line": brushes.add(new Line());
            break;
            case "polygon": brushes.add(new Polygon());
            break;
            default: return;
        }
        brushes.getLast().setPaintColor(mColorPicker.getCurrentColor());
        curX = x;
        curY = y;
    }

    @Override
    public void processDrag(float x, float y) {
        super.processDrag(x, y);
        if (brushes.getLast() != null) {
            brushes.getLast().processDrag(x, y);
            updateBitmap();
        }
    }

    @Override
    public void endDrag(float x, float y) {
        super.endDrag(x, y);
        if (brushes.getLast() != null) {
            brushes.getLast().endDrag(x, y);
            Log.d("endDrag", "brushes.size() = " + brushes.size());
        }
    }

    @Override
    public void cancel() {
        for (Brush b : brushes) {
            b.clear();
        }
        updateBitmap();
        super.cancel();
    }

    @Override
    public void commit() {
        super.commit();
    }
}
