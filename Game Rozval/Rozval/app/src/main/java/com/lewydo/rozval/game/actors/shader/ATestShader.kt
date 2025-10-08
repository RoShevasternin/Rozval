package com.lewydo.rozval.game.actors.shader

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.ScreenUtils
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen
import com.lewydo.rozval.game.utils.runGDX
import com.lewydo.rozval.util.log
import java.util.Stack
import kotlin.apply
import kotlin.collections.get
import kotlin.div
import kotlin.text.toInt

class ATestShader(override val screen: AdvancedScreen): AdvancedGroup() {

    private var fboGroup     : FrameBuffer?   = null
    private var textureGroup : TextureRegion? = null

    private val identityMatrix: Matrix4 = Matrix4().idt()
    private var camera = OrthographicCamera()

    override fun addActorsOnGroup() {
        createFrameBuffer()
    }

    private inline fun Batch.withMatrix(newProjectionMatrix: Matrix4, newTransformMatrix: Matrix4, block: () -> Unit) {
        val oldProj  = projectionMatrix
        val oldTrans = transformMatrix
        projectionMatrix = newProjectionMatrix
        transformMatrix  = newTransformMatrix
        block()
        projectionMatrix = oldProj
        transformMatrix  = oldTrans
    }

    private fun drawChildrenSimple(batch: Batch, parentAlpha: Float) {
        children.begin()
        for (i in 0 until children.size) {
            val child = children[i]
            if (child.isVisible) child.draw(batch, parentAlpha)
        }
        children.end()
    }

    private fun FrameBuffer.endAdvanced(batch: Batch) {
        end()
        stage.viewport.apply()

        batch.projectionMatrix = stage.camera.combined
        batch.transformMatrix  = identityMatrix

        batch.color  = Color.WHITE
        batch.shader = null
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (batch == null) return

        batch.end()

        fboGroup!!.begin()
        ScreenUtils.clear(Color.CLEAR, true)
        batch.begin()

        batch.withMatrix(camera.combined, identityMatrix) {
            drawChildrenSimple(batch, parentAlpha * color.a)
            //super.draw(batch, parentAlpha)
        }
        
        batch.end()
        fboGroup!!.endAdvanced(batch)

        batch.begin()
        //batch.draw(textureGroup, x, y, width, height)

        batch.draw(
            textureGroup,
            x, y,
            originX, originY,
            width, height,
            scaleX, scaleY,
            rotation,
        )
        batch.end()

        batch.begin()


    }

    private fun createFrameBuffer() {
        camera = OrthographicCamera(width, height)
        camera.position.set(width / 2f, height / 2f, 0f)
        camera.update()

        fboGroup     = FrameBuffer(Pixmap.Format.RGBA8888, (width).toInt(), (height).toInt(), false)
        textureGroup = TextureRegion(fboGroup!!.colorBufferTexture)
        textureGroup!!.flip(false, true)
    }

}