package com.example.midterm_proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.midterm_proj.StudioTool.StudioTool;

import java.util.LinkedList;
import java.util.List;

public class StudioCanvasView extends View {
    LinkedList<Bitmap> bitmapHistory = new LinkedList<Bitmap>();
    LinkedList<StudioTool> toolHistory = new LinkedList<StudioTool>();
    private final Rect bitmapRect = new Rect();
    private final Rect canvasRect = new Rect();
    private final Matrix matrix = new Matrix();

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
        if (toolHistory != null && toolHistory.size() > 0) {
//Do sth later
        }
        if (bitmapHistory != null && bitmapHistory.size() > 0) {
            Bitmap bitmap = bitmapHistory.getLast();
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
    }

    public void setBitmap(Bitmap bitmap) {
        bitmapHistory.clear();
        bitmapHistory.add(bitmap);
        invalidate();
    }

    public void cancel() {
        toolHistory.clear();
        bitmapHistory.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

//        return super.onTouchEvent(event);

        int maskedAction = event.getActionMasked();

        float x = event.getX();
        float y = event.getY();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
                beginDrag(x, y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // TODO use data
                processDrag(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                endDrag(x, y);
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
    }
    private void processDrag(float x, float y) {
    }
    private void endDrag(float x, float y) {
    }
}
