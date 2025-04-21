package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener
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
        val empty = queue.isEmpty()
        queue.addAll(tracks)
        if (empty) { play() }
    }

    fun enqueue(track: AudioTrack) {
        val empty = queue.isEmpty()
        queue.add(track)
        if (empty) { play() }
    }

    fun enqueueFirst(tracks: List<AudioTrack>) {
        val empty = queue.isEmpty()
        queue.addAll(0, tracks)
        if (empty) { play() }
    }

    fun enqueueFirst(track: AudioTrack) {
        val empty = queue.isEmpty()
        queue.addFirst(track)
        if (empty) { play() }
    }

    fun play() {
        if (index < queue.size) {
            player.playTrack(queue[index])
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

    private var wrapAround = true
    private var index = 0
    private val player: AudioPlayer
    private val queue: MutableList<AudioTrack> = mutableListOf()
}