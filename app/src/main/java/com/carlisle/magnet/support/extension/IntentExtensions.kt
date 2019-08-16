package com.carlisle.seed.support.extension

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.market(): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}

fun Context.magnet(magnet: String): Boolean {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(magnet))
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        return true
    }
    return false
}