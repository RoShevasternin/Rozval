package com.roshevasternin.rozval.game.screens

import com.roshevasternin.rozval.game.GDXGame
import com.roshevasternin.rozval.game.actors.panel.APanelGame
import com.roshevasternin.rozval.game.box2d.WorldUtil
import com.roshevasternin.rozval.game.utils.Block
import com.roshevasternin.rozval.game.utils.TIME_ANIM_SCREEN_ALPHA
import com.roshevasternin.rozval.game.utils.actor.animHide
import com.roshevasternin.rozval.game.utils.actor.animHideSuspend
import com.roshevasternin.rozval.game.utils.actor.animShow
import com.roshevasternin.rozval.game.utils.actor.animShowSuspend
import com.roshevasternin.rozval.game.utils.advanced.AdvancedBox2dScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedStage
import com.roshevasternin.rozval.game.utils.region
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class GameScreen(override val game: GDXGame): AdvancedBox2dScreen(WorldUtil()) {

    private val assetsAll = game.assetsAll

    private val panelMenu = APanelGame(this).apply { color.a = 0f }

    override fun show() {
        stageBack.root.color.a = 0f
        setUIBackground(assetsAll.LVL_1.region)
        super.show()
        stageBack.root.animShow(TIME_ANIM_SCREEN_ALPHA)
    }

    override fun AdvancedStage.addActorsOnStageBox2d() {
        coroutine?.launch {
            runGDX {

            }

//            panelMenu.animShowSuspend(TIME_ANIM_SCREEN_ALPHA)
        }
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        coroutine?.launch {
            panelMenu.animShowSuspend(TIME_ANIM_SCREEN_ALPHA)
        }
    }

    override fun hideScreen(block: Block) {
        coroutine?.launch {
            runGDX {
                stageBack.root.animHide(TIME_ANIM_SCREEN_ALPHA)
            }
            panelMenu.animHideSuspend(TIME_ANIM_SCREEN_ALPHA) { block.invoke() }

        }
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedGroup.addPanel() {
        addAndFillActor(panelMenu)

        panelMenu.apply {
            mainPanel.menuBtnBlock = { hideScreen { game.navigationManager.back() } }
        }

    }

}