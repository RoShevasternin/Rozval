package com.roshevasternin.rozval.game.screens

import com.roshevasternin.rozval.game.GDXGame
import com.roshevasternin.rozval.game.actors.panel.APanelMenu
import com.roshevasternin.rozval.game.utils.Block
import com.roshevasternin.rozval.game.utils.TIME_ANIM_SCREEN_ALPHA
import com.roshevasternin.rozval.game.utils.actor.animHideSuspend
import com.roshevasternin.rozval.game.utils.actor.animShowSuspend
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedStage
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class MenuScreen(override val game: GDXGame): AdvancedScreen() {

    companion object {
        var LVL_CLICK = 0
            private set
    }

    private val panelMenu = APanelMenu(this).apply { color.a = 0f }

    override fun AdvancedStage.addActorsOnStageUI() {
        coroutine?.launch {
            runGDX {
                addPanel()
            }

            panelMenu.animShowSuspend(TIME_ANIM_SCREEN_ALPHA)
        }
    }

    override fun hideScreen(block: Block) {
        coroutine?.launch {
            panelMenu.animHideSuspend(TIME_ANIM_SCREEN_ALPHA) { block.invoke() }
        }
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedStage.addPanel() {
        addAndFillActor(panelMenu)

        panelMenu.apply {
            availableLevelBtnBlock = {
                LVL_CLICK = it
                hideScreen { game.navigationManager.navigate(GameScreen::class.java.name, MenuScreen::class.java.name) }
            }

            mainPanel.exitBtnBlock      = { hideScreen { game.navigationManager.exit() } }
            mainPanel.bonusGameBtnBlock = { /*hideScreen { game.navigationManager.exit() }*/ }
        }

    }

}