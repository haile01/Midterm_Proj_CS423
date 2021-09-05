package com.example.midterm_proj.StudioTool.BrushToolHelper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Brush {
    public Rect bound;
    public Paint paint;

    public Brush() {
        paint = new Paint();
        paint.setStrokeWidth(5f);
    }

    public abstract void setPaintColor (int color);

    public abstract void draw(Canvas canvas);

    public abstract boolean processDrag(float x, float y);

    public abstract void endDrag(float x, float y);

    public abstract void clear();
}
