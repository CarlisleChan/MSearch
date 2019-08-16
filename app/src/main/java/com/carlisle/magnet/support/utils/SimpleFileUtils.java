package com.carlisle.magnet.support.utils;

import android.content.Context;

import java.io.InputStream;

public class SimpleFileUtils {
    private static final String TAG = "SimpleFileUtils";

    public static String readStringFromAssets(Context context, String file) {
        byte[] data = readBytesFromAssets(context, file);
        String ret = null;

        try {
            ret = new String(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public static byte[] readBytesFromAssets(Context context, String file) {
        byte[] buffer = null;
        try {
            InputStream fin = context.getAssets().open(file);
            int length = fin.available();
            buffer = new byte[length];
            fin.read(buffer);
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer;
        }
    }
}
