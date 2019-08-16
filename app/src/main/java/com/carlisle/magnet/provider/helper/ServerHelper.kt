package com.carlisle.magnet.provider.helper

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.carlisle.magnet.R
import com.carlisle.magnet.provider.http.HttpCenter
import com.carlisle.magnet.support.utils.SharedPreferencesUtils
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.toast

object ServerHelper {

    val DEFAULT_SERVER = "http://34.92.179.23:8080"
    private val KEY_SERVER = "server"

    fun showServerChangeDialog(context: Context) {
        val dialogLayout = context.layoutInflater.inflate(R.layout.dialog_server_change, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.et_server_address)
        editText.setText(getSelected(context))
        editText.setSelection(getSelected(context).length)

        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.DialogStyle))
        builder.setTitle("更换服务器地址")
        builder.setView(dialogLayout)
        builder.setPositiveButton("确定") { _, _ ->
            val server = editText.text.toString()
            if (Patterns.WEB_URL.matcher(server).matches()) {
                saveSelected(context, server)
                HttpCenter.setup(context)
            } else {
                context.toast("地址不合法!")
            }
        }
        builder.setNegativeButton("重置") { _, _ ->
            saveSelected(context, DEFAULT_SERVER)
        }
        builder.show()
    }

    fun getSelected(context: Context): String {
        var server = SharedPreferencesUtils.getData(context, KEY_SERVER, "") as String
        if (server.isNullOrEmpty()) {
            server = DEFAULT_SERVER
        }
        return server
    }

    fun saveSelected(context: Context, server: String) {
        SharedPreferencesUtils.saveData(context, KEY_SERVER, server)
    }
}