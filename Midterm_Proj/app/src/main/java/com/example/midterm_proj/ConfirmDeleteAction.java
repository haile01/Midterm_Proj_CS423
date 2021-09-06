package com.example.midterm_proj;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.example.midterm_proj.ConfirmAction;

import java.io.File;

public class ConfirmDeleteAction extends ConfirmAction {

    private ContentResolver mContentResolver;
    private Uri mImageUri;
    private Context mContext;

    public ConfirmDeleteAction (Uri imageUri, Context context, ContentResolver contentResolver) {
        super();
        mImageUri = imageUri;
        mContext = context;
        mContentResolver = contentResolver;
    }

    @Override
    public void execute() {
        String message;

        try {
            ImageRepository.getInstance(mContentResolver).deleteImage(mImageUri);
            message = "Xóa ảnh thành công";
        }
        catch (SecurityException ex) {
            message = "Lỗi " + ex.getMessage();
        }

        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
