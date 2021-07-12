package com.example.midterm_proj;

import android.content.Context;
import android.widget.Toast;

import androidx.documentfile.provider.DocumentFile;

import com.example.midterm_proj.ConfirmAction;

import java.io.File;

public class ConfirmDeleteAction extends ConfirmAction {

    private File mImageFile;
    private Context mContext;

    public ConfirmDeleteAction (File imageFile, Context context) {
        super();
        mImageFile = imageFile;
        mContext = context;
    }

    @Override
    public void execute() {
        String message;

        try {
            if (mImageFile.delete()) {
                message = "Xóa thành công";
            }
            else {
                message = "Không thể xóa file của ứng dụng khác ¯\\_(ツ)_/¯";
            }
        }
        catch (SecurityException ex) {
            message = "Lỗi " + ex.getMessage();
        }

        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
