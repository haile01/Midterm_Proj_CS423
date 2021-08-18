package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.ConfirmDeleteAction;
import com.example.midterm_proj.Image;
import com.example.midterm_proj.OpenPopupHandler;
import com.example.midterm_proj.PhotoActionMode;
import com.example.midterm_proj.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.GridRecyclerHolder>
    implements PhotoActionMode.PhotoActionHander {

    private final List<Image> mImageList;
    private Context mContext;
    OpenPopupHandler mOpenPopupHandler;
    SizeConfig size;
    private PhotoActionMode mPhotoActionMode = new PhotoActionMode();
    private int selectedPos = -1;

    public GridRecyclerAdapter(Context c, List<Image> imageList) {
        this.mContext = c;

        this.mImageList = imageList;
        size = new SizeConfig();
        mPhotoActionMode.setActionHandler(this);
    }

    @NonNull
    @Override
    public GridRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view, parent, false);
        return new GridRecyclerHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull GridRecyclerHolder holder, int position) {
        holder.photo.setLayoutParams(new RelativeLayout.LayoutParams((size.getWidth() -30 ) / size.getNumOfImagesRow(), (size.getWidth() - 30 ) / size.getNumOfImagesRow()));
        holder.photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        SizeConfig size = new SizeConfig();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                holder.photo.setImageBitmap(mContext.getContentResolver().loadThumbnail(mImageList.get(position).getUri(), new Size(size.getWidth(), size.getHeight()), null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.photo.setOnClickListener(v -> {
            if (mOpenPopupHandler != null) {
                mOpenPopupHandler.openSinglePhoto(mImageList.get(position).getId());
            }
        });
        holder.photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mPhotoActionMode.actionMode != null) {
                    return false;
                }

                // Start the CAB using the ActionMode.Callback defined above
                selectedPos = position;
//                ((RelativeLayout) holder.itemView).addView(getTickIconView());
                mPhotoActionMode.actionMode = ((Activity) mContext).startActionMode(mPhotoActionMode.getActionModeCallback());
                return true;
            }
        });
    }

    private View getTickIconView() {
        ImageView res = new ImageView(mContext);
//        res.setImageResource(R.drawable.ic_baseline_check_circle_25);
        res.setTag(1);
        return res;
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public void setPhotoActionMode(PhotoActionMode photoActionMode) {
        mPhotoActionMode = photoActionMode;
        mPhotoActionMode.setActionHandler(this);
    }

    @Override
    public void handleShare() {
        Log.d("HandleShare", "selectedPos = " + selectedPos);

        if (selectedPos == -1)
            return;
        Intent shareIntent = new Intent();

        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, mImageList.get(selectedPos).getUri());
        shareIntent.setType("image/*");
        startActivity(mContext, Intent.createChooser(shareIntent, "Share Image"), null);
    }

    @Override
    public void handleDelete() {
        if (selectedPos == -1)
            return;
        int index = selectedPos;
        ConfirmDeleteAction action = new ConfirmDeleteAction(mImageList.get(index).getUri(), mContext, mContext.getContentResolver());
//            ConfirmDialog dialog = new ConfirmDialog("Bạn có chắc muốn xóa ảnh này?");
//            dialog.show(((FragmentActivity) mView.getContext()).getSupportFragmentManager(), "Xác nhận xóa");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Bạn có chắc muốn xóa ảnh này?")
                .setCancelable(false)
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.execute();
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

    @Override
    public void handleCancel () {
        selectedPos = -1;
    }

    public static class GridRecyclerHolder extends RecyclerView.ViewHolder{
        public ImageView photo;
        public GridRecyclerHolder(@NonNull View itemView)
        {
            super(itemView);
            photo = itemView.findViewById(R.id.view_image);

        }
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }
}
