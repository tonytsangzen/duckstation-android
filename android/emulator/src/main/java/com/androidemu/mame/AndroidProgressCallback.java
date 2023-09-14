package com.androidemu.mame;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.appcompat.app.AlertDialog;

public class AndroidProgressCallback {

    public AndroidProgressCallback(Activity context) {

    }

    public void dismiss() {

    }

    public void setTitle(String text) {
    }

    public void setStatusText(String text) {
    }

    public void setProgressRange(int range) {
    }

    public void setProgressValue(int value) {
    }

    public void modalError(String message) {
    }

    public void modalInformation(String message) {
    }

    private class ConfirmationResult {
    }

    public boolean modalConfirmation(String message) {
        return false;
    }
}
