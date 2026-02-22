package org.noormahal.vp25.android

import android.app.Application
import org.noormahal.vp25.android.data.Graph

class VoiceApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}