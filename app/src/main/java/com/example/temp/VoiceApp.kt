package com.example.temp

import android.app.Application
import com.example.temp.data.Graph

class VoiceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}