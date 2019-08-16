package com.carlisle.magnet.support.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Creator      : carlisle
 * Date         : 08/05/2017
 * Description  :
 */

object ClipboardUtils {
    fun copy(context: Context, content: String) {
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cmb.primaryClip = ClipData.newPlainText(null, content)
    }

    fun paste(context: Context): String {
        val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return cmb.primaryClip?.getItemAt(0)?.text.toString()
    }
}
