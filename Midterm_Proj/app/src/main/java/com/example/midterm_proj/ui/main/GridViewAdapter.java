package com.example.midterm_proj.ui.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.R;

import java.util.List;

class GridViewAdapter extends BaseAdapter {
    private final List<Image> mImageList;
    private Context mContext;
//    private List<Photo> mData;
    OpenPopupHandler mOpenPopupHandler;

    // Constructor
    public GridViewAdapter(Context c, List<Image> imageList) {
        mContext = c;
        mImageList = imageList;
    }

    public int getCount() {
        return mImageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(5, 5, 5, 5);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageURI(mImageList.get(position).getUri());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOpenPopupHandler != null) {
                    mOpenPopupHandler.openSinglePhoto(position);
                }
            }
        });
        return imageView;
    }

    private Bitmap resizeMapIcons(Bitmap imageBitmap, int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }
}