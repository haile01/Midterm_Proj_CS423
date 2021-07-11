package com.example.midterm_proj.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;

import com.example.midterm_proj.R;

public class SinglePhotoView {

    View mView;

    public SinglePhotoView () {

    }

    void setView (View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public void initialize(PopupWindow window) {

        if (window != null) {
            Button closeButton = mView.findViewById(R.id.closePhoto);
            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick (View v) {
                    window.dismiss();
                }
            });
        }
    }
}
