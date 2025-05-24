package com.roshevasternin.rozval.game

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ScreenUtils
import com.roshevasternin.rozval.MainActivity
import com.roshevasternin.rozval.game.manager.*
import com.roshevasternin.rozval.game.manager.util.MusicUtil
import com.roshevasternin.rozval.game.manager.util.ParticleEffectUtil
import com.roshevasternin.rozval.game.manager.util.SoundUtil
import com.roshevasternin.rozval.game.manager.util.SpriteUtil
import com.roshevasternin.rozval.game.screens.LoaderScreen
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGame
import com.roshevasternin.rozval.game.utils.dataStore.IsTutorialsUtil
import com.roshevasternin.rozval.game.utils.dataStore.KeyUtil
import com.roshevasternin.rozval.game.utils.dataStore.LocationsUtil
import com.roshevasternin.rozval.game.utils.dataStore.StarUtil
import com.roshevasternin.rozval.game.utils.disposeAll
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class GDXGame(val activity: MainActivity) : AdvancedGame() {

    lateinit var assetManager     : AssetManager      private set
    lateinit var navigationManager: NavigationManager private set
    lateinit var spriteManager    : SpriteManager     private set
    lateinit var musicManager     : MusicManager      private set
    lateinit var soundManager     : SoundManager      private set
    lateinit var particleEffectManager: ParticleEffectManager private set

    val assetsLoader by lazy { SpriteUtil.Loader() }
    val assetsAll    by lazy { SpriteUtil.All() }

    val musicUtil by lazy { MusicUtil()    }
    val soundUtil by lazy { SoundUtil()    }

    val particleEffectUtil by lazy { ParticleEffectUtil() }

    var backgroundColor = GameColor.background
    val disposableSet   = mutableSetOf<Disposable>()

    val coroutine = CoroutineScope(Dispatchers.Default)

    val starUtil        = StarUtil(coroutine)
    val keyUtil         = KeyUtil(coroutine)
    val locationsUtil   = LocationsUtil(coroutine)
    val isTutorialsUtil = IsTutorialsUtil(coroutine)

    override fun create() {
        navigationManager = NavigationManager(this)
        assetManager      = AssetManager()
        spriteManager     = SpriteManager(assetManager)

        musicManager      = MusicManager(assetManager)
        soundManager      = SoundManager(assetManager)

        particleEffectManager = ParticleEffectManager(assetManager)

        navigationManager.navigate(LoaderScreen::class.java.name)
    }

    override fun render() {
        ScreenUtils.clear(backgroundColor)
        super.render()
    }

    override fun dispose() {
        try {
            log("dispose LibGDXGame")
            coroutine.cancel()
            disposableSet.disposeAll()
            disposeAll(assetManager, musicUtil)
            super.dispose()
        } catch (e: Exception) { log("exception: ${e.message}") }
    }

}