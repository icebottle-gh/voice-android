package com.example.temp

import android.app.Application
import com.example.temp.data.Graph

class VoiceApp : Application() {
    companion object {
        lateinit var INSTANCE: VoiceApp
    }

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
        INSTANCE = this
    }
}