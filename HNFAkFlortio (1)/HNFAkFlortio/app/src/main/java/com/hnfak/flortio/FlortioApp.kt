package com.hnfak.flortio

import android.app.Application
import com.hnfak.flortio.di.AppContainer

class FlortioApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
