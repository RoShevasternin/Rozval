package com.roshevasternin.rozval.game.screens

import com.roshevasternin.rozval.game.GDXGame
import com.roshevasternin.rozval.game.actors.panel.APanelLoader
import com.roshevasternin.rozval.game.manager.MusicManager
import com.roshevasternin.rozval.game.manager.ParticleEffectManager
import com.roshevasternin.rozval.game.manager.SoundManager
import com.roshevasternin.rozval.game.manager.SpriteManager
import com.roshevasternin.rozval.game.utils.Block
import com.roshevasternin.rozval.game.utils.TIME_ANIM_SCREEN_ALPHA
import com.roshevasternin.rozval.game.utils.actor.animHide
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedStage
import com.roshevasternin.rozval.game.utils.runGDX
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoaderScreen(override val game: GDXGame) : AdvancedScreen() {

    private val progressFlow     = MutableStateFlow(0f)
    private var isFinishLoading  = false
    private var isFinishProgress = false

    private val panelLoader by lazy { APanelLoader(this) }

    override fun show() {
        loadSplashAssets()
        super.show()
        loadAssets()
        collectProgress()
    }

    override fun render(delta: Float) {
        super.render(delta)
        loadingAssets()
        isFinish()
    }

    override fun hideScreen(block: Block) {}

    override fun AdvancedStage.addActorsOnStageUI() {
        addPanelLoader()
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedStage.addPanelLoader() {
        addAndFillActor(panelLoader)
    }

    // Logic ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(game.spriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.LOADER.data)
            loadAtlas()
        }
        game.assetManager.finishLoading()
        game.spriteManager.initAtlas()
    }

    private fun loadAssets() {
        with(game.spriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.entries.map { it.data }.toMutableList()
            loadAtlas()
            loadableTexturesList = SpriteManager.EnumTexture.entries.map { it.data }.toMutableList()
            loadTexture()
        }
        with(game.musicManager) {
            loadableMusicList = MusicManager.EnumMusic.entries.map { it.data }.toMutableList()
            load()
        }
        with(game.soundManager) {
            loadableSoundList = SoundManager.EnumSound.entries.map { it.data }.toMutableList()
            load()
        }
        with(game.particleEffectManager) {
            loadableParticleEffectList = ParticleEffectManager.EnumParticleEffect.entries.map { it.data }.toMutableList()
            load()
        }
    }

    private fun initAssets() {
        game.spriteManager.initAtlasAndTexture()
        game.musicManager.init()
        game.soundManager.init()
        game.particleEffectManager.init()
    }

    private fun loadingAssets() {
        if (isFinishLoading.not()) {
            if (game.assetManager.update(16)) {
                isFinishLoading = true
                initAssets()
            }
            progressFlow.value = game.assetManager.progress
        }
    }

    private fun collectProgress() {
        coroutine?.launch {
            var progress = 0
            progressFlow.collect { p ->
                while (progress < (p * 100)) {
                    progress += 1
                    runGDX { panelLoader.progressLbl.setText("$progress%") }
                    if (progress % 50 == 0) log("progress = $progress%")
                    if (progress == 100) isFinishProgress = true

//                    delay((20..65).shuffled().first().toLong())
                    delay((5..10).shuffled().first().toLong())
                }
            }
        }
    }

    private fun isFinish() {
        if (isFinishProgress) {
            isFinishProgress = false

//            game.musicUtil.apply { music = main.apply {
//                isLooping = true
//                coff      = 0.15f
//            } }

            stageUI.root.animHide(TIME_ANIM_SCREEN_ALPHA) {
                game.navigationManager.navigate(TestShaderScreen::class.java.name) //  MenuScreen::class.java.name)
            }
        }
    }


}