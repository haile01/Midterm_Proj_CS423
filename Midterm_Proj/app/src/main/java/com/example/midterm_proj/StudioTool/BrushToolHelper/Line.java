package com.example.midterm_proj.StudioTool.BrushToolHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;

public class Line extends Brush {

    public Line() {
    }

    public static Button renderButton (Context context, ChangeBrushTypeHandler handler) {
        Button res = new Button(context);
        res.setText("Line");
        res.setBackgroundResource(R.drawable.brush_select_background);
//        ((GradientDrawable) res.getBackground()).setColor(Color.TRANSPARENT);
        res.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 0, 10, 0);
        res.setLayoutParams(layoutParams);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.changeBrushType("line");
            }
        });

        return res;
    }

    @Override
    public void setPaintColor(int color) {
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {

    }

    @Override
    public boolean processDrag(float x, float y) {
        return true;
    }

    @Override
    public void endDrag(float x, float y) {

    }

    @Override
    public void clear() {

    }
}
