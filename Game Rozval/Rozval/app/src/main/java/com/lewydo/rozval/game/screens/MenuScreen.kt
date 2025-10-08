package com.lewydo.rozval.game.screens

import com.lewydo.rozval.game.GDXGame
import com.lewydo.rozval.game.actors.main.AMainMenu
import com.lewydo.rozval.game.utils.Block
import com.lewydo.rozval.game.utils.advanced.AdvancedMainScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedStage

class MenuScreen(override val game: GDXGame): AdvancedMainScreen() {

    companion object {
        var LVL_CLICK = 0
            private set
    }

    override val aMain = AMainMenu(this)

    override fun AdvancedStage.addActorsOnStageUI() {
        addMain()
    }

    override fun hideScreen(block: Block) {
        aMain.animHideMain { block.invoke() }
    }

    // Actors UI------------------------------------------------------------------------

    override fun AdvancedStage.addMain() {
        addAndFillActor(aMain)

        aMain.apply {
            availableLevelBtnBlock = {
                LVL_CLICK = it
                hideScreen { game.navigationManager.navigate(GameScreen::class.java.name, MenuScreen::class.java.name) }
            }

            mainPanel.exitBtnBlock      = { hideScreen { game.navigationManager.exit() } }
            mainPanel.bonusGameBtnBlock = { /*hideScreen { game.navigationManager.exit() }*/ }
        }
    }

}