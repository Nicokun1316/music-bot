package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

@OptIn(KordVoice::class)
class ServerPlayer : AudioEventAdapter {
    constructor(audioManager: AudioPlayerManager) {
        player = audioManager.createPlayer()
        player.addListener(this)
    }

    fun enqueue(tracks: List<AudioTrack>) {
        logger.debug { "Enqueuing tracks: $tracks" }
        trackQueue.enqueue(tracks)
        if (!playing) {
            trackQueue.nextTrack()?.let { play(it) }
        }
    }

    fun enqueue(track: AudioTrack) {
        logger.debug { "Enqueuing track: $track" }
        trackQueue.enqueue(listOf(track))
        if (!playing) {
            trackQueue.nextTrack()?.let { play(it) }
        }
    }

    fun enqueueFirst(tracks: List<AudioTrack>) {
        logger.debug { "Prepending tracks: $tracks" }
        trackQueue.enqueueFirst(tracks)
        trackQueue.nextTrack()?.let { play(it) }
    }

    fun enqueueFirst(track: AudioTrack) {
        logger.debug { "Prepending track: $track" }
        trackQueue.enqueueFirst(listOf(track))

        trackQueue.nextTrack()?.let { play(it) }
    }

    fun play(track: AudioTrack) {
        logger.debug { "Playing ${track.info.title}" }
        playing = true
        player.playTrack(track.makeClone())
    }

    override fun onTrackEnd(_player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        logger.debug {"Track end $endReason" }
        if (endReason?.mayStartNext == true) {
            val nextTrack = trackQueue.nextTrack()
            if (nextTrack == null) {
                playing = false
            } else {
                play(nextTrack)
            }
        }
    }

    override fun onTrackException(_player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?) {
        logger.error(exception) { "Track exception" }
    }

    override fun onTrackStuck(_player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long) {
        logger.warn { "Track stuck for $thresholdMs: $track" }
    }

    fun pause() {
        player.isPaused = true
    }

    fun resume() {
        player.isPaused = false
    }

    fun getData() = player.provide()?.data

    val progress get() = trackQueue.currentTrack.position / trackQueue.currentTrack.duration

    var volume
        get() = player.volume
        set(value) {
            player.volume = 50
        }

    fun skip() {
        val next = trackQueue.nextTrack()
        if (next != null) {
            play(next)
        } else {
            playing = false
        }
    }

    public val tracks: TrackQueueInterface
        get() = trackQueue

    suspend fun connect(channel: BaseVoiceChannelBehavior) {
        connection = channel.connect {
            audioProvider {
                AudioFrame.fromData(getData())
            }
        }
    }

    suspend fun disconnect() {
        connection?.disconnect()
        connection = null
    }


    private var wrapAround = true
    private val player: AudioPlayer
    private val trackQueue = TrackQueue()
    private var playing = false
    private var connection: VoiceConnection? = null
    override fun toString(): String {
        return "ServerPlayer(playing = $playing, wrapAround = $wrapAround, trackQueue = $trackQueue)"
    }
}