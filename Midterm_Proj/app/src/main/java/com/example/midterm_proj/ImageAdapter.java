package com.example.midterm_proj;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final View mContainer;
    private List<Image> mImageList;
    private LayoutInflater mInflater;
    private OnShowHideToolbar showHideListener;
    private ShowStudioHandler mShowStudioHandler;
    private ImageViewModel mImageViewModel;

    public ImageAdapter (Context context, List<Image> imageList, View container) {
        mInflater = LayoutInflater.from(context);
        mImageList = imageList;
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
        Image image = mImageList.get(position);
        try {
            holder.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = mContainer.getWidth();
        int height = mContainer.getHeight();
        if (width > 0 && height > 0) {
            holder.setSize(width, height);
        }
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public void setShowHideListener(SinglePhotoView singlePhotoView) {
        showHideListener = (OnShowHideToolbar) singlePhotoView;
    }

    public void setShowStudioHandler(ShowStudioHandler handler) {
        mShowStudioHandler = handler;
    }

    public void setImageViewModel(ImageViewModel imageViewModel) {
        mImageViewModel = imageViewModel;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public View mContainer;

        private ImageView mImageView;
        private ImageAdapter mAdapter;
        private Bitmap mImageBitmap;
        private int mScrollY;
        private boolean verticalScroll;
        private boolean touch;
        private int SNAP_THRESHOLD = 300;
        private boolean isSnapping = false;
        private int scrollYDiff;
        private OnShowHideToolbar showHideListener;
        private int detailAnimation = 0;
        private int showHideDuration = 100;
        private Image mImage;
        private ConfirmAction mConfirmAction;

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
                    if (scrollY >= 0 && !isSnapping) {
                        showHideDetail(true);
                    }
                    else if (scrollY == 0 && !touch) {
                        showHideDetail(false);
                        isSnapping = false;
                    }
                    snapScroll();
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

        public void showHideDetail(boolean show) {
            LinearLayout details = mContainer.findViewById(R.id.detailsContainer);
            LinearLayout background = mContainer.findViewById(R.id.imageBackground);
            if (detailAnimation == -1) {
                return;
            }

            showHideListener.showHideToolbar(!show);

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

        public void setImage(Image image) throws IOException {
            mImage = image;
            mImageBitmap = MediaStore.Images.Media.getBitmap(mContainer.getContext().getContentResolver(), image.getUri());
            mImageView.setImageBitmap(mImageBitmap);

//            Write image details

            TextView imageName = mContainer.findViewById(R.id.imageName);
            TextView lastModified = mContainer.findViewById(R.id.lastModified);
            TextView imagePath = mContainer.findViewById(R.id.imagePath);
            TextView imageSize = mContainer.findViewById(R.id.imageSize);
            TextView imageResolution = mContainer.findViewById(R.id.imageResolution);
            Button shareBtn = mContainer.findViewById(R.id.shareButtonImage);
            Button studioBtn = mContainer.findViewById(R.id.studioButtonImage);
            Button deleteBtn = mContainer.findViewById(R.id.deleteButtonImage);

            File imageFile = new File(image.getUri().getPath());
            SimpleDateFormat format = new SimpleDateFormat("E, d MMM yyyy - hh:mm");
            String datetime = format.format(image.getDateAdded());

            Float size = 1f * image.getSize();
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
            sizeString = String.format("%.2f", size) + sizeString;

            String resolution = mImageBitmap.getWidth() + " x " + mImageBitmap.getHeight();

            imageName.setText(image.getName());
            lastModified.setText(datetime);
            imagePath.setText(imageFile.getAbsolutePath());
            imageSize.setText(sizeString);
            imageResolution.setText(resolution);

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleShareImage();
                }
            });

            studioBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        handleOpenStudio();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDeleteImage();
                }
            });
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

        private void handleShareImage () {
            Intent shareIntent = new Intent();

            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, mImage.getUri());
            shareIntent.setType("image/*");
            startActivity(mContainer.getContext(), Intent.createChooser(shareIntent, "Share Image"), null);
        }

        private void handleOpenStudio() throws IOException {
            mShowStudioHandler.handleShowStudio();
        }

        private void handleDeleteImage () {
            mConfirmAction = new ConfirmDeleteAction(mImage.getUri(), mContainer.getContext(), mImageViewModel);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContainer.getContext());
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
}
