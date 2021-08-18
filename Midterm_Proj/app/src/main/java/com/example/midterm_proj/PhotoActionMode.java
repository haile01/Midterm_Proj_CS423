package com.example.midterm_proj;

import android.app.Notification;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.ActionMode;

import com.example.midterm_proj.ui.main.GridRecyclerAdapter;

public class PhotoActionMode {

    private PhotoActionHander mActionHandler;
    public ActionMode actionMode;

    public interface PhotoActionHander {
        void handleShare ();
        void handleDelete ();
        void handleCancel ();
    }

    public void setActionHandler(PhotoActionHander actionHandler) {
        mActionHandler = actionHandler;
    }
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_share:
                    mActionHandler.handleShare();
                    mode.finish();
                    return true;
                case R.id.menu_delete:
                    mActionHandler.handleDelete();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionHandler.handleCancel();
            actionMode = null;
        }
    };

    public ActionMode.Callback getActionModeCallback() {
        return actionModeCallback;
    }
}
