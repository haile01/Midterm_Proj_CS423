package com.example.midterm_proj.StudioTool.BrushToolHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.example.midterm_proj.R;

import javax.security.auth.callback.Callback;

public class ColorPicker {
    private final int[] colors = new int[]{
            Color.WHITE, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
            Color.CYAN, Color.DKGRAY, Color.LTGRAY, Color.GRAY, Color.MAGENTA,
            Color.YELLOW
    };
    private LinearLayout container;
    private View currentColor;
    private int colorInd = 0;

    public int getCurrentColor () {
        return colors[colorInd];
    }

    public ColorPicker(LinearLayout view, Context context) {
//            Inflate color picker buttons
        container = view.findViewById(R.id.colorPickerContainer);
        currentColor = view.findViewById(R.id.colorPickerCurrent);
        currentColor.setBackgroundResource(R.drawable.color_picker_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70, 70);
        layoutParams.setMargins(10, 0, 0, 10);

        for (int i = 0; i < colors.length; i++) {
            Button btn = new Button(context);
            btn.setLayoutParams(layoutParams);
            btn.setBackgroundResource(R.drawable.color_picker_background);
            ((GradientDrawable) btn.getBackground()).setColor(colors[i]);
            int index = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleChooseColor(index);
                }
            });

            container.addView(btn);
        }

        colorInd = 0;
        updateCurrentColor();
    }

    private void updateCurrentColor() {
//            Set current color View
        ((GradientDrawable) currentColor.getBackground()).setColor(colors[colorInd]);
//            Set all buttons to default

        for (int i = 0; i < container.getChildCount(); i++) {
            Button btn = (Button) container.getChildAt(i);
            btn.setBackgroundResource(R.drawable.color_picker_background);
            ((GradientDrawable) btn.getBackground()).setColor(colors[i]);
        }

//            Set select button to chosen
        Button btn = (Button) container.getChildAt(colorInd);
        btn.setBackgroundResource(R.drawable.color_picker_background_chosen);
        ((GradientDrawable) btn.getBackground()).setColor(colors[colorInd]);
    }

    public void handleChooseColor(int index) {
        colorInd = index;
        updateCurrentColor();
    }
}
