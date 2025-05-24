package com.roshevasternin.rozval.game.actors.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
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

class ATestShaderGroup(
    override val screen: AdvancedScreen,
    val _textureMask: Texture = screen.game.assetsAll.MASK_CIRCLE,
): AdvancedGroup() {

    companion object {
        private var vertexShader   = Gdx.files.internal("shader/test/testVS.glsl").readString()
        private var fragmentShader = Gdx.files.internal("shader/test/testFS.glsl").readString()
    }

    private var shaderProgram: ShaderProgram? = null

    private var fboGroup: FrameBuffer? = null
    private var fboMask : FrameBuffer? = null

    private var textureGroup: TextureRegion? = null
    private var textureMask : TextureRegion? = null

    private var camera = OrthographicCamera()

    private var screenXInPixels      = 0
    private var screenYInPixels      = 0
    private var screenWidthInPixels  = 0
    private var screenHeightInPixels = 0

    private var originalAlpha: Float    = 1f

    override fun addActorsOnGroup() {
        createShaders()
        createFrameBuffer()

        val texturesChildren = screen.game.assetsAll.BUILDER
        addActor(Image(texturesChildren).apply {
            setBounds(-2f, -60f, 200f, 315f)
        })
    }

    init {
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                touchDragged(event, x, y, pointer)
                return true
            }

            override fun touchDragged(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                super.touchDragged(event, x, y, pointer)
                children.last().also {
                    it.x = x
                    it.y = y
                }
            }
        })
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch         == null ||
            shaderProgram == null ||
            fboGroup      == null || fboMask      == null ||
            textureGroup  == null  || textureMask == null
        ) return

        batch.end()

        fboMask!!.begin()
        ScreenUtils.clear(Color.CLEAR)
        batch.begin()
        batch.projectionMatrix = camera.combined

        batch.draw(
            _textureMask,
            x, y,
            originX, originY,
            width, height,
            scaleX, scaleY,
            rotation,
            0, 0,
            _textureMask.width, _textureMask.height,
            false, false
        )

        batch.end()
        fboGroup!!.end(screenXInPixels, screenYInPixels, screenWidthInPixels, screenHeightInPixels)

        fboGroup!!.begin()
        ScreenUtils.clear(Color.CLEAR)
        batch.begin()
        batch.projectionMatrix = camera.combined

        originalAlpha = color.a
        color.a       = 1f

        super.draw(batch, 1f)
        color.a = originalAlpha

        batch.end()
        fboGroup!!.end(screenXInPixels, screenYInPixels, screenWidthInPixels, screenHeightInPixels)

        batch.begin()
        batch.projectionMatrix = stage.camera.combined

        batch.shader = shaderProgram

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1)
        textureMask!!.texture.bind(1)
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0)
        textureGroup!!.texture.bind(0)

        shaderProgram!!.setUniformi("u_mask", 1)
        shaderProgram!!.setUniformi("u_texture", 0)
        shaderProgram!!.setUniformf("u_alpha", parentAlpha * originalAlpha)

        batch.draw(textureGroup, x, y, width, height)

        batch.shader = null
    }

    override fun dispose() {
        super.dispose()
        disposeAll(
            shaderProgram,
            fboGroup,
            fboMask,
        )
    }

    // Logic ------------------------------------------------------------------------

    private fun createShaders() {
        ShaderProgram.pedantic = false
        shaderProgram = ShaderProgram(vertexShader, fragmentShader)

        if (shaderProgram?.isCompiled == false) {
            throw IllegalStateException("shader compilation failed:\n" + shaderProgram?.log)
        }
    }

    private fun createFrameBuffer() {
        parent.stage.viewport.apply {
            screenXInPixels      = screenX
            screenYInPixels      = screenY
            screenWidthInPixels  = screenWidth
            screenHeightInPixels = screenHeight
        }

        camera = OrthographicCamera(width, height)
        camera.position.set(x + (width / 2f), y + (height / 2f), 0f)
        camera.update()

        fboGroup = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)
        fboMask  = FrameBuffer(Pixmap.Format.RGBA8888, width.toInt(), height.toInt(), false)

        textureGroup = TextureRegion(fboGroup!!.colorBufferTexture)
        textureGroup!!.flip(false, true)
        textureMask = TextureRegion(fboMask!!.colorBufferTexture)
        textureMask!!.flip(false, true)
    }

}