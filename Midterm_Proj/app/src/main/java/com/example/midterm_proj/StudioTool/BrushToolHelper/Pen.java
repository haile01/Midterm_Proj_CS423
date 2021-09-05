package com.example.midterm_proj.StudioTool.BrushToolHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;

import java.util.ArrayList;

public class Pen extends Brush {
    ArrayList<PointF> points;

    public static Button renderButton (Context context, ChangeBrushTypeHandler handler) {
        Button res = new Button(context);
        res.setText("Pen");
        res.setBackgroundResource(R.drawable.brush_select_background);
//        ((GradientDrawable) res.getBackground()).setColor(Color.TRANSPARENT);
        res.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 10, 0);
        res.setLayoutParams(layoutParams);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.changeBrushType("pen");
            }
        });

        return res;
    }

    public Pen(float x, float y) {
        points = new ArrayList<>();
        points.add(new PointF(x, y));
    }

    @Override
    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        Log.d("Pen::draw", "length = " + points.size());
        for (int i = 1; i < points.size(); i++) {
            PointF a = new PointF();
            canvas.drawLine(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y, paint);
        }
    }

    @Override
    public boolean processDrag(float x, float y) {
        points.add(new PointF(x, y));
        return true;
    }

    @Override
    public void endDrag(float x, float y) {
        points.add(new PointF(x, y));
    }

    @Override
    public void clear() {
        points.clear();
    }
}
