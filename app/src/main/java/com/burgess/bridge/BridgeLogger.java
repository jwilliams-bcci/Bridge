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
    public static void createLogFile(File logFile) {
        try {
            File newLogFile = new File(logFile.getAbsolutePath());
            newLogFile.createNewFile();
            Log.i("BRIDGE_LOGGER", "New file created.");
        } catch (IOException e) {
            Log.e("BRIDGE_LOGGER", e.getMessage());
        }
    }

    public static void log(String flag, File logFile, String tag, String message) {
        switch(flag) {
            case "E":
                Log.e(tag, message);
                break;
            case "I":
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
                writer.append(LocalDateTime.now().toString() + "-" + flag + "-" + tag + "-" + message + "\n");
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            Log.e("BRIDGE_LOGGER", e.getMessage());
        }
    }
}
