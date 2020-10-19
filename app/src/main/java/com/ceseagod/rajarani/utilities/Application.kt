package com.ceseagod.rajarani.utilities

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex


class Application : Application() {
    var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        SessionMaintainence.init(this)
        context = applicationContext

    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
