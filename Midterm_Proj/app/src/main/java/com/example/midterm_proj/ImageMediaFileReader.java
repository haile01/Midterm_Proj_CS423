package com.example.midterm_proj;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageMediaFileReader {
    private MutableLiveData<List<Image>> mImagesData;
    //private Application application;
    private ContentResolver mContentResolver;

    public ImageMediaFileReader(ContentResolver contentResolver) {
        //application = tempApp;
        mContentResolver = contentResolver;
        mImagesData = new MutableLiveData<List<Image>>();
    }

    public MutableLiveData<List<Image>> getMutableAllImagesData() {
        readAllImage();
        return mImagesData;
    }

    private void readAllImage() {
        List<Image> mImagesList = new ArrayList<Image>();

        Uri collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED
        };
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

        try (Cursor cursor = mContentResolver.query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            int idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
            //int cnt = 0;

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idCol);
                String name = cursor.getString(nameCol);
                int size = cursor.getInt(sizeCol);
                Date dateAdded = new Date(Integer.valueOf(cursor.getString(dateAddedCol)) * 1000L);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                mImagesList.add(new Image(contentUri, name, size, dateAdded/*, cnt++*/));
            }
        }
        mImagesData.setValue(mImagesList);
    }
}
