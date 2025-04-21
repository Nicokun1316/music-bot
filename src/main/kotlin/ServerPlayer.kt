package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class ServerPlayer : AudioEventAdapter {
    constructor(audioManager: AudioPlayerManager) {
        player = audioManager.createPlayer()
        player.addListener(this)
    }

    fun enqueue(tracks: List<AudioTrack>) {
        logger.debug { "Enqueuing tracks: $tracks" }
        mutableQueue.addAll(tracks)
        if (index == queue.size - tracks.size) { play() }
    }

    fun enqueue(track: AudioTrack) {
        logger.debug { "Enqueuing track: $track" }
        mutableQueue.add(track)
        if (index == queue.size - 1) { play() }
    }

    fun enqueueFirst(tracks: List<AudioTrack>) {
        logger.debug { "Prepending tracks: $tracks" }
        mutableQueue.addAll(0, tracks)
        play()
    }

    fun enqueueFirst(track: AudioTrack) {
        logger.debug { "Prepending track: $track" }
        mutableQueue.addFirst(track)
        play()
    }

    fun play() {
        if (index < queue.size) {
            logger.debug { "Playing ${queue[index]}" }
            player.playTrack(queue[index])
        } else {
            logger.warn { "No tracks available" }
        }
    }

    override fun onTrackEnd(_player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?
    ) {
        logger.debug {"Track end $endReason" }
        if (endReason?.mayStartNext == true) {
            ++index
            if (wrapAround) index = index % queue.size
            play()
        }
    }

    override fun onTrackException(_player: AudioPlayer?, track: AudioTrack?, exception: FriendlyException?
    ) {
        logger.error(exception) { "Track exception" }
    }

    override fun onTrackStuck(_player: AudioPlayer?, track: AudioTrack?, thresholdMs: Long
    ) {
        logger.warn { "Track stuck for $thresholdMs: $track" }
    }

    fun pause() {
        player.isPaused = true
    }

    fun resume() {
        player.isPaused = false
    }

    fun getData() = player.provide()?.data

    val queue: List<AudioTrack>
        get() = mutableQueue

    val currentIndex: Int
        get() = index

    private var wrapAround = true
    private var index = 0
    private val player: AudioPlayer
    private val mutableQueue: MutableList<AudioTrack> = mutableListOf()
    override fun toString(): String {
        return "ServerPlayer(${queue.joinToString(", ") { it.info.title }})"
    }
}