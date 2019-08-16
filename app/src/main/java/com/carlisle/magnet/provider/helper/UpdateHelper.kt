package com.carlisle.magnet.provider.helper

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.carlisle.magnet.BuildConfig
import com.carlisle.magnet.R
import com.carlisle.magnet.provider.http.HttpCenter
import org.jetbrains.anko.browse
import org.jetbrains.anko.runOnUiThread

object UpdateHelper {
    fun checkUpdate(context: Context) {
        HttpCenter.checkUpdate(success = { update ->
            if (update.latestVersionCode > BuildConfig.VERSION_CODE) {
                context.runOnUiThread {
                    val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.DialogStyle))
                    builder.setTitle(update.title)
                    builder.setMessage(update.releaseNotes.joinToString("\n"))
                    builder.setPositiveButton("确定") { _, _ -> browse(update.download) }
                    builder.setNegativeButton("取消") { _, _ -> }
                    builder.show()
                }
            }
        }, failure = {

        })
    }
}