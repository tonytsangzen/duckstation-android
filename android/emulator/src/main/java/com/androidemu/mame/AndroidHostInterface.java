package com.androidemu.mame;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.io.InputStream;

public class AndroidHostInterface {
    public final static int DISPLAY_ALIGNMENT_TOP_OR_LEFT = 0;
    public final static int DISPLAY_ALIGNMENT_CENTER = 1;
    public final static int DISPLAY_ALIGNMENT_RIGHT_OR_BOTTOM = 2;

    private long mNativePointer;
    private Context mContext;
    static {
        System.loadLibrary("duckstation-native");
    }

    static private AndroidHostInterface mInstance;

    public AndroidHostInterface(Context context) {
        this.mContext = context;
    }


    static public boolean createInstance(Context context, String baseDir) {
        // Set user path.
        if(baseDir==null)
            baseDir = context.getExternalCacheDir() + "/";

        mInstance = create(context, baseDir);
        return mInstance != null;
    }

    static public boolean hasInstance() {
        return mInstance != null;
    }

    static public AndroidHostInterface getInstance() {
        return mInstance;
    }

    static public boolean hasInstanceAndEmulationThreadIsRunning() {
        return hasInstance() && getInstance().isEmulationThreadRunning();
    }

    //native call back
    public void reportError(String message) {
        Log.e("HostInterface", message);
        Object lock = new Object();
        ((Activity)mContext).runOnUiThread (() -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, button) -> {
                        dialog.dismiss();
                        synchronized (lock) {
                            lock.notify();
                        }
                    })
                    .create()
                    .show();
        });

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public void reportMessage(String message) {
        Log.e("HostInterface", message);
        Object lock = new Object();
        ((Activity)mContext).runOnUiThread (() -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, button) -> {
                        dialog.dismiss();
                        synchronized (lock) {
                            lock.notify();
                        }
                    })
                    .create()
                    .show();
        });

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public InputStream openAssetStream(String path) {
        try {
            return mContext.getAssets().open(path, AssetManager.ACCESS_STREAMING);
        } catch (IOException e) {
            return null;
        }
    }

    public void onEmulationStarted() {
        Log.e("HostInterface", "onEmulationStarted");

    }

    public void onEmulationStopped() {
        Log.e("HostInterface", "onEmulationStopped");
        stopEmulationThread();
        ((Activity)mContext).finish();
    }

    public void setVibration(boolean enabled) {

    }

    //native function
    static public native String getScmVersion();
    static public native String getFullScmVersion();
    static public native AndroidHostInterface create(Context context, String userDirectory);
    public native boolean isEmulationThreadRunning();
    public native boolean startEmulationThread(Surface surface, String filename, boolean resumeState, String state_filename);
    public native boolean isEmulationThreadPaused();
    public native void pauseEmulationThread(boolean paused);
    public native void stopEmulationThread();
    public native void shutdown();
    public native boolean hasSurface();
    public native void surfaceChanged(Surface surface, int format, int width, int height);
    // TODO: Find a better place for this.
    public native void setControllerType(int index, String typeName);
    public native void setControllerButtonState(int index, int buttonCode, boolean pressed);
    public native void setControllerAxisState(int index, int axisCode, float value);
    public static native int getControllerButtonCode(String controllerType, String buttonName);
    public static native int getControllerAxisCode(String controllerType, String axisName);
    public native void refreshGameList(boolean invalidateCache, boolean invalidateDatabase, AndroidProgressCallback progressCallback);
    public native void resetSystem();
    public native void loadState(boolean global, int slot);
    public native void saveState(boolean global, int slot);
    public native void saveResumeState(boolean waitForCompletion);
    public native void applySettings();
    public native void setDisplayAlignment(int alignment);
    public native PatchCode[] getPatchCodeList();
    public native void setPatchCodeEnabled(int index, boolean enabled);
    public native boolean importPatchCodesFromString(String str);
    public native void addOSDMessage(String message, float duration);
    public native boolean hasAnyBIOSImages();
    public native String importBIOSImage(byte[] data);
    public native boolean isFastForwardEnabled();
    public native void setFastForwardEnabled(boolean enabled);
    public native String[] getMediaPlaylistPaths();
    public native int getMediaPlaylistIndex();
    public native boolean setMediaPlaylistIndex(int index);

}
