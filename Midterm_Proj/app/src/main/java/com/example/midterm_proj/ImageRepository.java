package com.example.midterm_proj;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ImageRepository {
    private static MutableLiveData<List<Image>> mImagesList;
    private ImageMediaFileReader reader;
    private final ContentResolver mContentResolver;

    public ImageRepository(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
        if (mImagesList == null || mImagesList.getValue() == null) {
            mImagesList = new MutableLiveData<List<Image>>();
            new readImageMediaFileTask(mContentResolver).execute();
        }
    }

    public LiveData<List<Image>> getAllImages() {
        return mImagesList;
    }

    public void deleteImage(Uri imageUri) {
        //new deleteImageAsyncTask(mContentResolver, imageUri).execute();
        try {
            if (mContentResolver.delete(imageUri, null, null) > 0)
                for (Image i : Objects.requireNonNull(mImagesList.getValue()))
                    if (i.getUri() == imageUri) {
                        mImagesList.getValue().remove(i);
                        mImagesList.setValue(mImagesList.getValue());
                        break;
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
            mImagesList.setValue(data);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            data = reader.getMutableAllImagesData();
            return null;
        }
    }

//    private static class deleteImageAsyncTask extends AsyncTask<Void, Void, Boolean> {
//        private ContentResolver mContentResolver;
//        private Uri mImageUri;
//
//        public deleteImageAsyncTask(ContentResolver contentResolver, Uri imageUri) {
//            super();
//            mContentResolver = contentResolver;
//            mImageUri = imageUri;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean isDeleted) {
//            // invoked in UI thread so use setValue and not postValue
//            if (isDeleted)
//                mImagesList.setValue(mImagesList.getValue());
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
///*          // Check if file/image exists
//            String[] projection = { MediaStore.Images.Media._ID };
//
//            // Match on the file path
//            String selection = MediaStore.Images.Media.DATA + " = ?";
//            String[] selectionArgs = new String[] { file.getAbsolutePath() };
//
//            // Query for the ID of the media matching the file path
//            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            Cursor c = mContentResolver.query(queryUri, projection, selection, selectionArgs, null);
//            if (c.moveToFirst()) {
//                // We found the ID. Deleting the item via the content provider will also remove the file
//                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
//                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//                mContentResolver.delete(deleteUri, null, null);
//            } else {
//                // File not found in media store DB
//            }
//*/
//            mContentResolver.delete(mImageUri, null, null);
//            // there might be exception, use try-catch
//            // but idk how to do exception in background
//            // maybe this doesn't need to be in background :v
//
//            //mImagesList.getValue().removeIf(i -> (i.getUri() == mImageUri));
//            for (Image i : Objects.requireNonNull(mImagesList.getValue()))
//                if (i.getUri() == mImageUri) {
//                    mImagesList.getValue().remove(i);
//                    return true;
//                }
//            return false;
//        }
//    }

    private static class saveImageAsyncTask extends  AsyncTask<Void, Void, Void> {
        private ContentResolver mContentResolver;
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
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
//            try (InputStream fin = mContentResolver.openInputStream(imageUri)) {
//                fin.
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            String[] projection = new String[] {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DATE_ADDED
            };
            try (Cursor cursor = mContentResolver.query(
                    imageUri,
                    projection,
                    null,
                    null,
                    null
            )) {
                int idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                int dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
                int cnt = 0;

                if (cursor.moveToFirst()) {
                    long id = cursor.getLong(idCol);
                    String name = cursor.getString(nameCol);
                    int size = cursor.getInt(sizeCol);
                    Date dateAdded = new Date(Integer.valueOf(cursor.getString(dateAddedCol)) * 1000L);

                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    mImagesList.getValue().add(0, new Image(contentUri, name, size, dateAdded, cnt++));
                    mImagesList.setValue(mImagesList.getValue());
                }
            }
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
