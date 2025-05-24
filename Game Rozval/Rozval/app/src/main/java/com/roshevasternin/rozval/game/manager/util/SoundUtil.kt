package com.roshevasternin.rozval.game.manager.util

import com.badlogic.gdx.audio.Sound
import com.roshevasternin.rozval.game.manager.AudioManager
import com.roshevasternin.rozval.game.utils.runGDX
import com.roshevasternin.rozval.game.manager.SoundManager

class SoundUtil {

    val click = SoundManager.EnumSound.click.data.sound
    val touch = SoundManager.EnumSound.touch.data.sound

    private val boom1 = SoundManager.EnumSound.boom1.data.sound
    private val boom2 = SoundManager.EnumSound.boom2.data.sound
    private val boom3 = SoundManager.EnumSound.boom3.data.sound
    private val boom4 = SoundManager.EnumSound.boom4.data.sound
    private val boom5 = SoundManager.EnumSound.boom5.data.sound
    private val boom6 = SoundManager.EnumSound.boom6.data.sound
    private val boom7 = SoundManager.EnumSound.boom7.data.sound

    val boomList = listOf(boom1, boom2, boom3, boom4, boom5, boom6, boom7)

    // 0..100
    var volumeLevel = AudioManager.volumeLevelPercent

    var isPause = (volumeLevel <= 0f)

    fun play(sound: Sound, volume: Float = volumeLevel) = runGDX { if (isPause.not()) sound.play(volume / 100f) }
}