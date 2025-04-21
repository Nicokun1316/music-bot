package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val logger = KotlinLogging.logger {}

suspend fun DefaultAudioPlayerManager.findTracks(query: String, search: Boolean = true): List<AudioTrack> = suspendCoroutine {
    this.loadItem("${if (search) "ytsearch: " else ""}$query", object : AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack) {
            logger.info {
                "Single track loaded for $query, ${track.info.uri}"
            }
            it.resume(listOf(track))
        }

        override fun playlistLoaded(playlist: AudioPlaylist) {
            logger.info {
                "Playlist loaded for query $query, ${playlist.tracks.size} tracks"
            }
            it.resume(playlist.tracks)
        }

        override fun noMatches() {
            logger.warn { "No tracks found for query $query" }
            it.resumeWithException(RuntimeException("No tracks loaded"))
        }

        override fun loadFailed(exception: FriendlyException?) {
            logger.error(exception) { "Failed to load tracks for query $query"  }
            it.resumeWithException(exception ?:  RuntimeException("Unknown exception"))
        }
    })
}
