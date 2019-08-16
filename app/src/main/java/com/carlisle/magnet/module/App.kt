package com.carlisle.magnet.module

import android.app.Application
import com.carlisle.magnet.provider.http.HttpCenter

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        HttpCenter.setup(this)
    }

}