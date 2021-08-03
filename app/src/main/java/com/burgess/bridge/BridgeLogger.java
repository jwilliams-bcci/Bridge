package com.burgess.bridge;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class BridgeLogger {
    private static BridgeLogger instance;
    private static Context ctx;
    private static File logFile;

    public static final String TAG = "BRIDGE_LOGGER";

    private BridgeLogger(Context context) {
        ctx = context;
        logFile = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "BridgeLogFile.txt");
    }

    public static synchronized BridgeLogger getInstance(Context context) {
        if (instance == null) {
            instance = new BridgeLogger(context);
        }
        return instance;
    }
    public static synchronized  BridgeLogger getInstance() {
        if (instance == null) {
            throw new IllegalStateException(BridgeLogger.class.getSimpleName() + " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public static void createLogFile(File logFile) {
        try {
            File newLogFile = new File(logFile.getAbsolutePath());
            newLogFile.createNewFile();
            Log.i(TAG, "New file created.");
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void log(char flag, String tag, String message) {
        switch(flag) {
            case 'E':
                Log.e(tag, message);
                break;
            case 'I':
                Log.i(tag, message);
                break;
            default:
                Log.d(tag, message);
                break;
        }
        try {
            if(!logFile.exists()) {
                createLogFile(logFile);
            } else {
                FileWriter writer = new FileWriter(logFile, true);
                writer.append(String.format("%s - %s - %s - %s\n", LocalDateTime.now().toString(), flag, tag, message));
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static Intent sendLogFile(String inspectorId, String versionName) {
        Uri logFileUri = FileProvider.getUriForFile(ctx, "com.burgess.bridge", logFile);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        String to[] = {"jwilliams@burgess-inc.com", "rsandlin@burgess-inc.com"};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(String.valueOf(logFileUri)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Activity log from Bridge for Inspector " + inspectorId);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Bridge version: " + versionName);
        return emailIntent;
    }
}
