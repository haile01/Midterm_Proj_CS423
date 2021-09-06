package com.example.midterm_proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.midterm_proj.StudioTool.StudioTool;
import com.example.midterm_proj.StudioTool.StudioToolManager;

import java.util.LinkedList;
import java.util.List;

public class StudioCanvasView extends View {
    private Bitmap currentBitmap;
    private final Rect bitmapRect = new Rect();
    private final Rect canvasRect = new Rect();
    private final Matrix matrix = new Matrix();
    private StudioToolManager mStudioToolManager;

    public StudioCanvasView(Context context) {
        super(context);
    }

    public StudioCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StudioCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StudioCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (currentBitmap != null) {
            Bitmap bitmap = currentBitmap;
            float bw = bitmap.getWidth(), bh = bitmap.getHeight();
            float cw = getWidth(), ch = getHeight();

            float hRatio = ch / bh;
            float wRatio = cw / bw;
            float ratio = 0;
            float xOffset = 0;
            float yOffset = 0;

            if (bw * hRatio > cw) {
//                Fit to width
                yOffset = (ch - bh * wRatio) / 2;
                ch = bh * wRatio;
                ratio = wRatio;
            }
            else {
//                Fit to height
                xOffset = (cw - bw * hRatio) / 2;
                cw = bw * hRatio;
                ratio = hRatio;
            }

            matrix.reset();
            matrix.postScale(ratio, ratio);
            matrix.postTranslate((int) Math.round(xOffset), (int) Math.round(yOffset));

            canvas.drawBitmap(bitmap, matrix, null);
        }
        if (mStudioToolManager.currentTool != null) {
//            Handle specific tools (crop, text, ...)
            mStudioToolManager.currentTool.drawCanvas(canvas, matrix);
        }
    }

    public void updateBitmap (Bitmap bitmap) {
        currentBitmap = bitmap;
        invalidate();
    }

    public void cancel() {
        currentBitmap = null;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        return super.onTouchEvent(event);

        int maskedAction = event.getActionMasked();

        float x = event.getX();
        float y = event.getY();

        Matrix inverse = new Matrix();
        matrix.invert(inverse);
        float[] inverted = new float[] {x, y};
        inverse.mapPoints(inverted);

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
                beginDrag(inverted[0], inverted[1]);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // TODO use data
                processDrag(inverted[0], inverted[1]);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                endDrag(inverted[0], inverted[1]);
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }

        return true;

    }

    private void beginDrag(float x, float y) {
        if (mStudioToolManager.currentTool != null) {
            mStudioToolManager.currentTool.beginDrag(x, y);
            postInvalidate();
        }
    }
    private void processDrag(float x, float y) {
        if (mStudioToolManager.currentTool != null) {
            mStudioToolManager.currentTool.processDrag(x, y);
            postInvalidate();
        }
    }
    private void endDrag(float x, float y) {
        if (mStudioToolManager.currentTool != null) {
            mStudioToolManager.currentTool.endDrag(x, y);
            postInvalidate();
        }
    }

    public void setStudioToolManager(StudioToolManager studioToolManager) {
        mStudioToolManager = studioToolManager;


    }
}
