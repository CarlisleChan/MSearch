package com.carlisle.magnet.support.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by carlisle on 11/8/16.
 */

public class SharedPreferencesUtils {
    public static final String DEFAULT_FILE_NAME = "cached_data";

    public static void saveData(Context context, String key, Object data) {
        saveData(context, DEFAULT_FILE_NAME, key, data);
    }

    public static void saveData(Context context, String fileName, String key, Object data) {
        String type = data.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.commit();
    }

    public static void deleteData(Context context, String key) {
        deleteData(context, DEFAULT_FILE_NAME, key);
    }

    public static void deleteData(Context context, String fileName, String key) {

        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);

        editor.commit();
    }

    public static Object getData(Context context, String key, Object defValue) {
        return getData(context, DEFAULT_FILE_NAME, key, defValue);
    }

    public static Object getData(Context context, String fileName, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (DEFAULT_FILE_NAME, Context.MODE_PRIVATE);

        if ("Integer".equals(type)) {
            return sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sharedPreferences.getLong(key, (Long) defValue);
        }

        return null;
    }

    public static void clearData(Context context) {
        clearData(context, DEFAULT_FILE_NAME);
    }

    public static void clearData(Context context, String fileName) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
