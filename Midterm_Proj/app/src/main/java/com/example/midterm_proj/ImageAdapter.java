package com.example.midterm_proj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.ui.main.SinglePhotoView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    public interface OnShowHideToolbar {
        void showHideToolbar(boolean show);
    }

    private final View mContainer;
    private LinkedList<String> mImages;
    private LayoutInflater mInflater;
    private OnShowHideToolbar showHideListener;

    public ImageAdapter (Context context, LinkedList<String> images, View container) {
        mInflater = LayoutInflater.from(context);
        mImages = images;
        mContainer = container;
    }

    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View imageContainerView = mInflater.inflate(R.layout.single_photo_item, parent, false);
        ImageViewHolder viewHolder = new ImageViewHolder(imageContainerView, this);
        viewHolder.setShowHideListener(showHideListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {
        String image = mImages.get(position);
        holder.setImage(image);

        int width = mContainer.getWidth();
        int height = mContainer.getHeight();
        if (width > 0 && height > 0) {
            holder.setSize(width, height);
        }
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public void setShowHideListener(SinglePhotoView singlePhotoView) {
        showHideListener = (OnShowHideToolbar) singlePhotoView;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private ImageAdapter mAdapter;
        private View mContainer;
        private Bitmap mImageBitmap;
        private File mImageFile;
        private int mScrollY;
        private boolean verticalScroll;
        private boolean touch;
        private int SNAP_THRESHOLD = 300;
        private boolean isSnapping = false;
        private int scrollYDiff;
        private OnShowHideToolbar showHideListener;
        private int detailAnimation = 0;
        private int showHideDuration = 100;

        public ImageViewHolder(View imageContainerView, ImageAdapter imageAdapter) {
            super(imageContainerView);
            mImageView = imageContainerView.findViewById(R.id.imageView);
            mContainer = imageContainerView;
            mAdapter = imageAdapter;

            mContainer.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    mScrollY = scrollY;
                    scrollYDiff = scrollY - oldScrollY;
                    verticalScroll = scrollY != oldScrollY;
                    if (verticalScroll && scrollY >= 0 && !isSnapping) {
                        showHideListener.showHideToolbar(false);
                        showHideDetail(true);
                    }
                    if (scrollY == 0 && !touch) {
                        showHideListener.showHideToolbar(true);
                        showHideDetail(false);
                        isSnapping = false;
                    }
                    snapScroll();
                }
            });

            mContainer.findViewById(R.id.imageDescription).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText text = (EditText) v;
                    if (!hasFocus) {
//                        Save description here
                        Toast.makeText(mContainer.getContext(), text.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(mContainer.getContext(), "focused", Toast.LENGTH_SHORT).show();
                        text.requestFocus();
                        InputMethodManager imm = (InputMethodManager) mContainer.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
            });

            mContainer.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP: {
                            touch = false;
                            isSnapping = false;
                            snapScroll();
                            if (scrollYDiff <= 1 && mScrollY <= SNAP_THRESHOLD) {
                                showHideListener.showHideToolbar(true);
                                showHideDetail(false);
                            }
                            else {
                                showHideListener.showHideToolbar(false);
                                showHideDetail(true);
                            }
                            break;
                        }
                        case MotionEvent.ACTION_DOWN: {
                            touch = true;
                            break;
                        }
                    }

                    return false;
                }
            });
        }

        private void showHideDetail(boolean show) {
            LinearLayout details = mContainer.findViewById(R.id.detailsContainer);
            LinearLayout background = mContainer.findViewById(R.id.imageBackground);
            if (detailAnimation == -1) {
                return;
            }

            if (show && detailAnimation == 0 && details.getAlpha() == 0f) {
//            show
                details.setAlpha(0f);
                details.setVisibility(View.VISIBLE);
                details.animate()
                        .alpha(1f)
                        .setDuration(showHideDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                detailAnimation = 1;
                            }
                        });
                background.setAlpha(0f);
                background.setVisibility(View.VISIBLE);
                background.animate()
                        .alpha(1f)
                        .setDuration(showHideDuration)
                        .setListener(null);
                detailAnimation = -1;
            }
            if (!show && detailAnimation == 1 && details.getAlpha() == 1f) {
//            hide
                details.setAlpha(1f);
                details.animate()
                        .alpha(0f)
                        .setDuration(showHideDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                details.setVisibility(View.INVISIBLE);
                                detailAnimation = 0;
                            }
                        });
                background.setAlpha(1f);
                background.animate()
                        .alpha(0f)
                        .setDuration(showHideDuration)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                background.setVisibility(View.INVISIBLE);
                            }
                        });
                detailAnimation = -1;
            }
        }

        private void snapScroll() {
            if (scrollYDiff <= 1 && mScrollY <= SNAP_THRESHOLD && !touch && !isSnapping) {
                isSnapping = true;
                ObjectAnimator.ofInt(mContainer, "scrollY",  0).setDuration(100).start();
            }
        }

        public void setImage(String image) {
            mImageBitmap = BitmapFactory.decodeFile(image);
            mImageView.setImageBitmap(mImageBitmap);

//            Write image details

            TextView lastModified = mContainer.findViewById(R.id.lastModified);
            TextView imagePath = mContainer.findViewById(R.id.imagePath);
            TextView imageSize = mContainer.findViewById(R.id.imageSize);
            TextView imageResolution = mContainer.findViewById(R.id.imageResolution);

            mImageFile = new File(image);

            SimpleDateFormat format = new SimpleDateFormat("E, d MMM yyyy - hh:mm");
            String datetime = format.format(new Date(mImageFile.lastModified()));

            Float size = 1f * mImageFile.length();
            int unit = 0;
            while (size > 100) {
                size /= 1024;
                unit++;
            }
            String sizeString;
            switch (unit) {
                case 1: sizeString = "KB";
                    break;
                case 2: sizeString = "MB";
                    break;
                case 3: sizeString = "GB";
                    break;
                default: sizeString = "B";
            }
            sizeString = String.valueOf((size.intValue())) + sizeString;

            String resolution = mImageBitmap.getWidth() + " x " + mImageBitmap.getHeight();

            lastModified.setText(datetime);
            imagePath.setText(image);
            imageSize.setText(sizeString);
            imageResolution.setText(resolution);
        }

        public void setSize(int width, int height) {
            mContainer.setLayoutParams(new ViewGroup.LayoutParams(width, height));

            int a = mImageBitmap.getWidth(), b = mImageBitmap.getHeight();

            Float scaledWidth = 1f * width;
            Float scaledHeight = 1f * mImageBitmap.getHeight() * width / mImageBitmap.getWidth();
            int paddingTop = 0;
            if (scaledHeight > height) {
                scaledWidth *= height / scaledHeight;
                scaledHeight = 1f * height;
            }
            else {
                paddingTop = (int)((height - scaledHeight) / 2);
            }
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(scaledWidth.intValue(), scaledHeight.intValue());
            mImageView.setLayoutParams(imageParams);
            mContainer.findViewById(R.id.paddingTop).setLayoutParams(new LinearLayout.LayoutParams(width, paddingTop));

            LinearLayout.LayoutParams detailsParams = new LinearLayout.LayoutParams(width, height);
            detailsParams.setMargins(0, -20, 0, 0);
            mContainer.findViewById(R.id.detailsContainer).setLayoutParams(detailsParams);
        }

        public void setShowHideListener(OnShowHideToolbar showHideListener) {
            this.showHideListener = showHideListener;
        }
    }
}
