package com.example.midterm_proj.StudioTool.BrushToolHelper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;

import java.util.HashMap;

public class BrushPicker implements ChangeBrushTypeHandler {
    private String brushType = "pen";
    private HashMap <String, Button> brushPickBtn;

    public BrushPicker (LinearLayout toolOptions, Context context) {
        LinearLayout brushTypeContainer = toolOptions.findViewById(R.id.brushTypeContainer);
        brushPickBtn = new HashMap<>();
        brushPickBtn.put("pen", Pen.renderButton(context, this));
        brushPickBtn.put("line", Line.renderButton(context, this));
//        brushPickBtn.put("polygon", Polygon.renderButton(context, this));

        brushTypeContainer.addView(brushPickBtn.get("pen"));
        brushTypeContainer.addView(brushPickBtn.get("line"));
//        brushTypeContainer.addView(brushPickBtn.get("polygon"));

        changeBrushType(brushType);
    }

    @Override
    public void changeBrushType(String type) {
        brushType = type;

        for (String brush : brushPickBtn.keySet()) {
            if (brush.equals(type)) {
                brushPickBtn.get(brush).setTextColor(Color.BLACK);
                brushPickBtn.get(brush).setBackgroundResource(R.drawable.brush_select_background_chosen);
            }
            else {
                brushPickBtn.get(brush).setTextColor(Color.WHITE);
                brushPickBtn.get(brush).setBackgroundResource(R.drawable.brush_select_background);
            }
        }
    }

    public String getBrushType () {
        return brushType;
    }
}
