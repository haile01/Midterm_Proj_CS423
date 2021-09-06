package com.example.midterm_proj;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.example.midterm_proj.ConfirmAction;

import java.io.File;

public class ConfirmDeleteAction extends ConfirmAction {

    private final Uri mImageUri;
    private final ImageViewModel mViewModel;
    private final Context mContext;

    public ConfirmDeleteAction (Uri imageUri, Context context, ImageViewModel viewModel) {
        super();
        mImageUri = imageUri;
        mViewModel = viewModel;
        mContext = context;
    }

    @Override
    public void execute() {
        String message;

        try {
            mViewModel.deleteImage(mImageUri);
            message = "Xóa ảnh thành công";
        }
        catch (SecurityException ex) {
            message = "Lỗi " + ex.getMessage();
        }

        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
