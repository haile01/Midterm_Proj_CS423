package com.example.midterm_proj.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.midterm_proj.BuildConfig;
import com.example.midterm_proj.ConfirmAction;
import com.example.midterm_proj.ConfirmDeleteAction;
import com.example.midterm_proj.ImageAdapter;
import com.example.midterm_proj.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.startActivity;

public class SinglePhotoView implements ImageAdapter.OnShowHideToolbar {

    View mView;
//    private int curImage = 0;
    private LinkedList<String> images = new LinkedList<String>();
    private RecyclerView mImageRecyclerView;
    private ImageAdapter mImageAdapter;
    private int padding = 30;
    private LinearLayoutManager mLayoutManager;
    private ConfirmAction mConfirmAction;
    private int showHideDuration = 100;
    private int toolbarAnimation = 1;


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
            ImageButton closeButton = mView.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick (View v) {
                    window.dismiss();
                }
            });
        }

//        Handle click to show/hide toolbar
//            attachShowHideOnClick();

//        Handle share to Share button
        attachShareOnClick();
        attachDeleteOnClick();

//        Delete later
        fetchAllImages(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
//        Prepare image recycler view
        prepareImageRecyclerView();
    }

    private void attachDeleteOnClick() {
        Button deleteButton = mView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteImage();
            }
        });
    }

    private void attachShareOnClick() {
        Button shareButton = mView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleShareImage();
            }
        });
    }

    public void showHideToolbar(boolean show) {
        LinearLayout toolbar = mView.findViewById(R.id.bottomToolbarContainer);
        ImageButton close = mView.findViewById(R.id.closeButton);
        if (toolbarAnimation == -1) {
            return;
        }

        if (show && toolbarAnimation == 0 && toolbar.getAlpha() == 0f) {
//            show
            toolbar.setAlpha(0f);
            toolbar.setVisibility(View.VISIBLE);
            toolbar.animate()
                    .alpha(1f)
                    .setDuration(showHideDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            toolbarAnimation = 1;
                        }
                    });
            close.setBackgroundResource(R.color.transparent);
            close.setElevation(0f);
            close.setImageResource(R.drawable.ic_baseline_arrow_back_white);
            toolbarAnimation = -1;
        }
        if (!show && toolbarAnimation == 1 && toolbar.getAlpha() == 1f) {
//            hide
            toolbar.setAlpha(1f);
            toolbar.animate()
                    .alpha(0f)
                    .setDuration(showHideDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            toolbar.setVisibility(View.GONE);
                            toolbarAnimation = 0;
                        }
                    });
            close.setBackgroundResource(R.drawable.return_button_background);
            close.setElevation(8f);
            close.setImageResource(R.drawable.ic_baseline_arrow_back_black);
            toolbarAnimation = -1;
        }
    }

    private void prepareImageRecyclerView() {
        mImageRecyclerView = mView.findViewById(R.id.imageContainer);
        mImageAdapter = new ImageAdapter(mView.getContext(), images, mView);
        mImageAdapter.setShowHideListener(this);
        mLayoutManager = new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mImageRecyclerView.setAdapter(mImageAdapter);
        mImageRecyclerView.setLayoutManager(mLayoutManager);
        mImageRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = padding;
                outRect.right = padding;
            }
        });
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(mImageRecyclerView);
    }

    private void fetchAllImages(File directory) {
        File[] files;

        if (directory.exists() && directory.isDirectory()) {
            files = directory.listFiles();
        }
        else {
            files = new File[] {};
        }

        if (files == null)
            return;

        for (File f: files) {
            if (f.isDirectory()) {
                fetchAllImages(f);
            }
            else {
                if (f.isFile() && isImage(f))
                images.push(f.getAbsolutePath());
            }
        }
    }

    private String[] extensions = {"jpg", "png", "gif", "jpeg"};

    private boolean isImage(File f) {
        for (String ext: extensions) {
            if (f.getName().toLowerCase().endsWith(ext))
                return true;
        }
        return false;
    }

    private void handleShareImage () {
        int index = mLayoutManager.findFirstVisibleItemPosition();
        Intent shareIntent = new Intent();
        File imageFile = new File(images.get(index));
        Uri imageUri = FileProvider.getUriForFile(((Activity) mView.getContext()), BuildConfig.APPLICATION_ID + ".fileprovider", imageFile);

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        startActivity(mView.getContext(), Intent.createChooser(shareIntent, "Share Image"), null);
    }

    private void handleDeleteImage () {
        int index = mLayoutManager.findFirstVisibleItemPosition();
        File imageFile = new File(images.get(index));
        if (imageFile.exists()) {
            mConfirmAction = new ConfirmDeleteAction(imageFile, mView.getContext());
//            ConfirmDialog dialog = new ConfirmDialog("Bạn có chắc muốn xóa ảnh này?");
//            dialog.show(((FragmentActivity) mView.getContext()).getSupportFragmentManager(), "Xác nhận xóa");
            AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
            builder.setMessage("Bạn có chắc muốn xóa ảnh này?")
                    .setCancelable(false)
                    .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mConfirmAction.execute();
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Do sth
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void setPosition(int position) {
        mLayoutManager.scrollToPositionWithOffset(position, -padding);
    }
}
