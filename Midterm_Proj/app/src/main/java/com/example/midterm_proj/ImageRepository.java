package com.example.midterm_proj;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ImageRepository {
    private MutableLiveData<List<Image>> mImagesList;

    public ImageRepository(Application application) {
        ImageMediaFileReader reader = new ImageMediaFileReader(application);
        mImagesList = new MutableLiveData<List<Image>>();

        mImagesList = reader.getMutableImagesData();
    }

    public LiveData<List<Image>> getAllImages() {
        return mImagesList;
    }

//    private static class deleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
//        private WordDao mAsyncTaskDao;
//
//        deleteWordAsyncTask(WordDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Word... params) {
//            mAsyncTaskDao.deleteWord(params[0]);
//            return null;
//        }
//    }
}
