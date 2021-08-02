package com.burgess.bridge;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

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
        logFile = new File(ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "BridgeLogFile.txt");
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
}
