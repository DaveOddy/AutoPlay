package com.davidoddy.autoplay.audio

import android.animation.ValueAnimator
import android.media.AudioManager
import android.media.AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
import android.media.AudioManager.STREAM_MUSIC

/**
 * Created by doddy on 12/8/17.
 */
class VolumeAdjuster(val audioManager: AudioManager) : IVolumeAdjuster {

    companion object {
        private const val RAMP_UP = 3000L
    }

    override fun setVolume(percentage: Int) {

        val maxVolume = this.audioManager.getStreamMaxVolume(STREAM_MUSIC)
        val currentVolume = this.audioManager.getStreamVolume(STREAM_MUSIC)
        val targetVolume = percentage * maxVolume / 100

        val animator: ValueAnimator = ValueAnimator.ofInt(currentVolume, targetVolume)

        animator.setDuration(RAMP_UP)

                .addUpdateListener { animation: ValueAnimator? -> run {
                    val currentValue = animation?.animatedValue as Int
                    this.audioManager.setStreamVolume(STREAM_MUSIC, currentValue, FLAG_REMOVE_SOUND_AND_VIBRATE)
                }}

        animator.start()
    }
}