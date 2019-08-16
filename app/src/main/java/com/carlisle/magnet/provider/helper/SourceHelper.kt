package com.carlisle.magnet.provider.helper

import android.content.Context
import com.carlisle.magnet.provider.http.MagnetRule
import com.carlisle.magnet.support.utils.SharedPreferencesUtils
import com.carlisle.magnet.support.utils.SimpleFileUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SourceHelper {
    private val DEFAULT_CONFIG = "rules.json"
    private val KEY_SELECTED = "selected"

    fun getSelected(context: Context): List<MagnetRule> {
        var json = SharedPreferencesUtils.getData(context, KEY_SELECTED, "") as String
        if (json.isNullOrEmpty()) {
            json = SimpleFileUtils.readStringFromAssets(context, DEFAULT_CONFIG)
        }
        return Gson().fromJson(json, object : TypeToken<List<MagnetRule>>() {}.type)
    }

    fun saveSelected(context: Context, sources: List<MagnetRule>) {
        val json = Gson().toJson(sources)
        SharedPreferencesUtils.saveData(context, KEY_SELECTED, json)
    }
}