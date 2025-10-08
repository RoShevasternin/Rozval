package com.lewydo.rozval.game.utils.advanced

import com.badlogic.gdx.utils.viewport.FitViewport
import com.lewydo.rozval.game.box2d.WorldUtil
import com.lewydo.rozval.game.utils.HEIGHT_BOX2D
import com.lewydo.rozval.game.utils.HEIGHT_UI
import com.lewydo.rozval.game.utils.WIDTH_BOX2D
import com.lewydo.rozval.game.utils.WIDTH_UI
import com.lewydo.rozval.util.log

abstract class AdvancedBox2dScreen(
    val worldUtil: WorldUtil,
    val uiW  : Float = WIDTH_UI,
    val uiH  : Float = HEIGHT_UI,
    val boxW : Float = WIDTH_BOX2D,
    val boxH : Float = HEIGHT_BOX2D,
): AdvancedScreen(uiW, uiH) {

    private val viewportDebug by lazy { FitViewport(boxW, boxH) }

    private val viewportBox2d by lazy { FitViewport(uiW, uiH) }
    val stageBox2d            by lazy { AdvancedStage(viewportBox2d) }

    override fun show() {
        stageBox2d.addActorsOnStageBox2d()
        super.show()

        inputMultiplexer.addProcessor(stageBox2d)
    }

    override fun resize(width: Int, height: Int) {
        viewportDebug.update(width, height, true)
        viewportBox2d.update(width, height, true)
        super.resize(width, height)
    }

    override fun render(delta: Float) {
        worldUtil.update(delta)

        stageBack.render()
        stageBox2d.render()
        stageUI.render()

        drawerUtil.update()

        worldUtil.debug(viewportDebug.camera.combined)
    }

    override fun dispose() {
        log("dispose AdvancedBox2dScreen: ${this::class.java.name.substringAfterLast('.')}")
        worldUtil.dispose()
        super.dispose()
    }

    abstract fun AdvancedStage.addActorsOnStageBox2d()

}