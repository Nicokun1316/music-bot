package io.github.nicokun1316

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlin.math.min

interface TrackQueueInterface {
    fun tableRep(): String

    val currentTrack: AudioTrack?
    val currentIndex: Int
    val size: Int
    val songsLeft: Boolean
    var wrapAround: Boolean
}

class TrackQueue: TrackQueueInterface {

    public fun enqueue(tracks: Collection<AudioTrack>) {
        queue.addAll(tracks)
    }
    public fun enqueueFirst(tracks: Collection<AudioTrack>) {
        index = -1
        queue.addAll(0, tracks)
    }

    public fun nextTrack(): AudioTrack? {
        ++index
        if (wrapAround) {
            index %= queue.size
        }
        if (songsLeft) {
            return queue[index]
        }
        return null
    }

    override fun tableRep(): String = queue.joinToString("\n") { it.info.title }


    public override val currentTrack
        get() = queue[currentIndex]

    override fun toString(): String {
        return "TrackQueue(${queue.joinToString(", ") { it.info.title }})"
    }

    override val size: Int
        get() = queue.size
    public override val currentIndex
        get() = min(index, queue.size)
    public override val songsLeft
        get() = index < queue.size

    public override var wrapAround = true

    private val queue = mutableListOf<AudioTrack>()
    private var index = -1
}