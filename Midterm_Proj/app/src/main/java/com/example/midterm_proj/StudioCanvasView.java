package com.example.midterm_proj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.midterm_proj.StudioTool.StudioTool;

import java.util.LinkedList;
import java.util.List;

public class StudioCanvasView extends View {
    LinkedList<Bitmap> bitmapHistory = new LinkedList<Bitmap>();
    LinkedList<StudioTool> toolHistory = new LinkedList<StudioTool>();
    private final Rect bitmapRect = new Rect();
    private final Rect canvasRect = new Rect();

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
            bitmapRect.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvasRect.set(0, 0, getWidth(), getHeight());

            canvas.drawBitmap(bitmap, bitmapRect, canvasRect, null);
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
}
