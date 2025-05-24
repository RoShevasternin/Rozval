package com.roshevasternin.rozval.game.actors.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.ScreenUtils
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.disposeAll
import com.roshevasternin.rozval.util.log

class ABlurGroup(
    override val screen: AdvancedScreen,
): AdvancedGroup() {

    companion object {
        private var vertexShader    = Gdx.files.internal("shader/blur/blurVS.glsl").readString()
        private var hFragmentShader = Gdx.files.internal("shader/blur/blurHFS.glsl").readString()
        private var vFragmentShader = Gdx.files.internal("shader/blur/blurVFS.glsl").readString()
    }

    private var hBlurShader: ShaderProgram? = null
    private var vBlurShader: ShaderProgram? = null

    private var fbo1: FrameBuffer? = null
    private var fbo2: FrameBuffer? = null

    private var frameTexture1: TextureRegion? = null
    private var frameTexture2: TextureRegion? = null

    private var camera = OrthographicCamera()

    private var screenXInPixels      = 0
    private var screenYInPixels      = 0
    private var screenWidthInPixels  = 0
    private var screenHeightInPixels = 0
    private var screenWidthInWorld   = 0f
    private var screenHeightInWorld  = 0f

    private var originalAlpha: Float = 1f

    var radiusBlur: Float = 0f

    override fun addActorsOnGroup() {
        createShaders()
        createFrameBuffer()

        val texturesChildren = screen.game.assetsAll.BUILDER
        addAndFillActor(Image(texturesChildren))
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch       == null ||
            hBlurShader == null || vBlurShader == null ||
            fbo1        == null || fbo2        == null
        ) return

        batch.end()

        // 1
        fbo1!!.begin()
        ScreenUtils.clear(Color.CLEAR)
        batch.begin()
        batch.projectionMatrix = camera.combined

        originalAlpha = color.a
        color.a       = 1f

        super.draw(batch, 1f)
        color.a = originalAlpha

        batch.end()
        fbo1!!.end(screenXInPixels, screenYInPixels, screenWidthInPixels, screenHeightInPixels)

        // 2
        fbo2!!.begin()
        ScreenUtils.clear(Color.CLEAR)
        batch.begin()

        batch.shader = vBlurShader

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        frameTexture1!!.texture.bind(0)

        vBlurShader!!.setUniformi("u_texture", 0)
        vBlurShader!!.setUniformf("u_resolution", screenHeightInWorld)
        vBlurShader!!.setUniformf("u_radius", radiusBlur)

        batch.draw(frameTexture1, 0f, 0f, screenWidthInWorld, screenHeightInWorld)
        batch.end()
        fbo2!!.end(screenXInPixels, screenYInPixels, screenWidthInPixels, screenHeightInPixels)

        batch.begin()
        batch.projectionMatrix = stage.camera.combined
        batch.shader = hBlurShader

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        frameTexture2!!.texture.bind(0)
        hBlurShader!!.setUniformi("u_texture", 0)

        hBlurShader!!.setUniformf("u_resolution", screenWidthInWorld)
        hBlurShader!!.setUniformf("u_radius", radiusBlur)
        hBlurShader!!.setUniformf("u_alpha", parentAlpha * originalAlpha)

        batch.draw(frameTexture2, 0f, 0f, screenWidthInWorld, screenHeightInWorld)
        batch.shader = null
    }

    override fun dispose() {
        super.dispose()
        disposeAll(
            hBlurShader,
            vBlurShader,
            fbo1,
            fbo2,
        )
    }

    // Logic ------------------------------------------------------------------------

    private fun createShaders() {
        ShaderProgram.pedantic = true
        hBlurShader = ShaderProgram(vertexShader, hFragmentShader)
        vBlurShader = ShaderProgram(vertexShader, vFragmentShader)

        if (hBlurShader?.isCompiled == false) {
            throw IllegalStateException("shader compilation failed:\n" + hBlurShader?.log)
        }
        if (vBlurShader?.isCompiled == false) {
            throw IllegalStateException("shader compilation failed:\n" + vBlurShader?.log)
        }
    }

    private fun createFrameBuffer() {
        parent.stage.viewport.apply {
            screenXInPixels      = screenX
            screenYInPixels      = screenY
            screenWidthInPixels  = screenWidth
            screenHeightInPixels = screenHeight
            screenWidthInWorld   = worldWidth
            screenHeightInWorld  = worldHeight

            log("""
                name: ${this@ABlurGroup.name}
                screenXInPixels = $screenXInPixels
                screenYInPixels = $screenYInPixels
                screenWidthInPixels = $screenWidthInPixels
                screenHeightInPixels = $screenHeightInPixels
                screenWidthInWorld = $screenWidthInWorld
                screenHeightInWorld = $screenHeightInWorld
            """.trimIndent())
        }

        camera = OrthographicCamera(screenWidthInWorld, screenHeightInWorld)
        camera.setToOrtho(false, screenWidthInWorld, screenHeightInWorld)

        fbo1 = FrameBuffer(Pixmap.Format.RGBA8888, screenWidthInPixels, screenHeightInPixels, false)
        fbo2 = FrameBuffer(Pixmap.Format.RGBA8888, screenWidthInPixels, screenHeightInPixels, false)


        frameTexture1 = TextureRegion(fbo1!!.colorBufferTexture)
        frameTexture1!!.flip(false, true)
        frameTexture2 = TextureRegion(fbo2!!.colorBufferTexture)
        frameTexture2!!.flip(false, true)
    }

}