package com.example.temp.process

import android.content.Context
import androidx.room.Room
import com.example.temp.VoiceApp
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import net.folivo.trixnity.client.MatrixClient
import net.folivo.trixnity.client.login
import net.folivo.trixnity.client.media.okio.OkioMediaStore
import net.folivo.trixnity.client.room
import net.folivo.trixnity.client.store.repository.room.TrixnityRoomDatabase
import net.folivo.trixnity.client.store.repository.room.TrixnityRoomDatabase_Impl
import net.folivo.trixnity.client.store.repository.room.createRoomRepositoriesModule
import net.folivo.trixnity.clientserverapi.model.authentication.IdentifierType
import net.folivo.trixnity.core.model.RoomId
import okio.Path
import okio.Path.Companion.toPath

fun getCacheDirectoryPath(): Path {
    return VoiceApp.INSTANCE.cacheDir.absolutePath.toPath().resolve("cache").resolve("media");
}

class TrixnityEventProcessor {

    companion object {
        private lateinit var client : MatrixClient
        suspend fun start(applicationContext: Context) = runBlocking {
            var repositoriesModule = createRoomRepositoriesModule(Room.databaseBuilder(applicationContext, TrixnityRoomDatabase::class.java,  "trixnityRoomDb"))
            var mediaPath = getCacheDirectoryPath().resolve("media")
            var mediaStore = OkioMediaStore(mediaPath)
            client = MatrixClient.login(
                baseUrl = Url("https://matrix.org"),
                mediaStore = mediaStore ,
                repositoriesModule = repositoriesModule,
                identifier = IdentifierType.User("icebottle4"),
                password = "5H5w&CSI7ym7IP"
            ).getOrThrow()
            client.startSync()
            println("Starting sync")
        }
        suspend fun stop() {
            client.stopSync()
            println("Stopping sync")
        }
    }

}