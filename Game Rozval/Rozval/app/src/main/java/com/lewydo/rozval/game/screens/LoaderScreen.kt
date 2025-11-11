package com.lewydo.rozval.game.screens

import com.lewydo.rozval.game.actors.main.AMainLoader
import com.lewydo.rozval.game.manager.MusicManager
import com.lewydo.rozval.game.manager.ParticleEffectManager
import com.lewydo.rozval.game.manager.SoundManager
import com.lewydo.rozval.game.manager.SpriteManager
import com.lewydo.rozval.game.utils.Block
import com.lewydo.rozval.game.utils.advanced.AdvancedMainScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedStage
import com.lewydo.rozval.game.utils.gdxGame
import com.lewydo.rozval.game.utils.runGDX
import com.lewydo.rozval.util.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoaderScreen : AdvancedMainScreen() {

    private val progressFlow     = MutableStateFlow(0f)
    private var isFinishLoading  = false
    private var isFinishProgress = false

    override val aMain by lazy { AMainLoader(this) }

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

    override fun AdvancedStage.addActorsOnStageUI() {
        addMain()
    }

    override fun hideScreen(block: Block) {
        aMain.animHideMain { block.invoke() }
    }

    // Actors UI ------------------------------------------------------------------------

    override fun AdvancedStage.addMain() {
        addAndFillActor(aMain)

    }

    // Logic ------------------------------------------------------------------------

    private fun loadSplashAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = mutableListOf(SpriteManager.EnumAtlas.LOADER.data)
            loadAtlas()
        }
        gdxGame.assetManager.finishLoading()
        gdxGame.spriteManager.initAtlas()
    }

    private fun loadAssets() {
        with(gdxGame.spriteManager) {
            loadableAtlasList = SpriteManager.EnumAtlas.entries.map { it.data }.toMutableList()
            loadAtlas()
            loadableTexturesList = SpriteManager.EnumTexture.entries.map { it.data }.toMutableList()
            loadTexture()
        }
        with(gdxGame.musicManager) {
            loadableMusicList = MusicManager.EnumMusic.entries.map { it.data }.toMutableList()
            load()
        }
        with(gdxGame.soundManager) {
            loadableSoundList = SoundManager.EnumSound.entries.map { it.data }.toMutableList()
            load()
        }
        with(gdxGame.particleEffectManager) {
            loadableParticleEffectList = ParticleEffectManager.EnumParticleEffect.entries.map { it.data }.toMutableList()
            load()
        }
    }

    private fun initAssets() {
        gdxGame.spriteManager.initAtlasAndTexture()
        gdxGame.musicManager.init()
        gdxGame.soundManager.init()
        gdxGame.particleEffectManager.init()
    }

    private fun loadingAssets() {
        if (isFinishLoading.not()) {
            if (gdxGame.assetManager.update(16)) {
                isFinishLoading = true
                initAssets()
            }
            progressFlow.value = gdxGame.assetManager.progress
        }
    }

    private fun collectProgress() {
        coroutine?.launch {
            var progress = 0
            progressFlow.collect { p ->
                while (progress < (p * 100)) {
                    progress += 1
                    runGDX { aMain.progressLbl.setText("$progress%") }
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

            hideScreen {
                gdxGame.navigationManager.navigate(TestShaderScreen::class.java.name)//   MenuScreen::class.java.name)
            }
        }
    }


}