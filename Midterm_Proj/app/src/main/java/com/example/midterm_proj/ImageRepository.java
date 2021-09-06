package com.example.midterm_proj;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;

public class ImageRepository {
    private static ImageRepository instance;

    public static ImageRepository getInstance(ContentResolver contentResolver) {
        if (instance == null) {
            instance = new ImageRepository(contentResolver);
        }
        return instance;
    }

    private MutableLiveData<List<Image>> mImagesList;
    private final ContentResolver mContentResolver;
    private String version;

    private ImageRepository(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        if (mImagesList == null || mImagesList.getValue() == null) {
            mImagesList = new MutableLiveData<List<Image>>();
            new readImageMediaFileTask(mContentResolver).execute();
        }
    }

    public LiveData<List<Image>> getAllImages() {
        return mImagesList;
    }

    public void initializeCheckForUpdTimer(Context context) {
        String newVersion;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri collection;
            collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            newVersion = MediaStore.getVersion(context, MediaStore.getVolumeName(collection));
        } else {
            newVersion = MediaStore.getVersion(context);
        }

        Handler handler = new Handler();
        Runnable checkForUpdTask = new Runnable() {
            @Override
            public void run() {
                if (version != null && !version.equals(newVersion)) {
                    new readImageMediaFileTask(mContentResolver);
                }
                version = newVersion;
                handler.postDelayed(this, 10000);
            }
        };

        handler.post(checkForUpdTask);
    }

    public void deleteImage(Uri imageUri) {
        try {
            if (mContentResolver.delete(imageUri, null, null) > 0) {
                Log.d("ImageRepo::deleteImage", "deleted " + imageUri);
                new readImageMediaFileTask(mContentResolver).execute();
            }
        } catch (Exception e) {
            Log.e("DeleteImage", "Cannot delete image");
        }
    }

    public void saveImage(Bitmap image) {
        new saveImageAsyncTask(mContentResolver, image).execute();
    }



    //---------------AsyncTask------------------------------

    private static class readImageMediaFileTask extends AsyncTask<Void, Void, Void> {
        private ImageMediaFileReader reader;
        private List<Image> data;

        public readImageMediaFileTask(ContentResolver contentResolver) {
            super();
            reader = new ImageMediaFileReader(contentResolver);
        }

        @Override
        protected void onPostExecute(Void voids) {
            instance.mImagesList.postValue(data);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            data = reader.getMutableAllImagesData();
            return null;
        }
    }

    private static class saveImageAsyncTask extends  AsyncTask<Void, Void, Void> {
        private final ContentResolver mContentResolver;
        private Bitmap mImage;
        private OutputStream fos;
        private Uri imageUri;
        //private File imageFile;

        public saveImageAsyncTask(ContentResolver contentResolver, Bitmap image) {
            super();
            mContentResolver = contentResolver;
            mImage = image;
        }

        @Override
        protected void onPreExecute() {
            String name = System.currentTimeMillis() + ".jpg";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, name + ".jpg");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                imageUri = mContentResolver.insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values);
                try {
                    fos = mContentResolver.openOutputStream(Objects.requireNonNull(imageUri));
                } catch (FileNotFoundException e) {
                    Log.e("SaveImg", "Cannot save file, version > Q");
                    cancel(true);
                }
            } else {
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File imageFile = new File(imagesDir, name + ".jpg");
                imageUri = Uri.fromFile(imageFile);
                try {
                    fos = new FileOutputStream(imageFile);
                } catch (FileNotFoundException e) {
                    Log.e("SaveImg", "Cannot save file, version < Q");
                    cancel(true);
                }
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new readImageMediaFileTask(mContentResolver).execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isCancelled())
                return null;
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return null;
        }
    }
}
