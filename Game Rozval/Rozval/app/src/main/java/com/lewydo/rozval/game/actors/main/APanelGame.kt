package com.lewydo.rozval.game.actors.main

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.lewydo.rozval.game.actors.mainPanel.GameMainPanel
import com.lewydo.rozval.game.utils.GameColor
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen
import com.lewydo.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class APanelGame(override val screen: AdvancedScreen): AdvancedGroup() {

    val mainPanel = GameMainPanel(screen)

    // Field


    override fun addActorsOnGroup() {
        coroutine?.launch {
            runGDX {
                addMainPanel()
                //addDialog()
            }
        }
    }

    // Actors ------------------------------------------------------------------------

    private fun addMainPanel() {
        addActor(mainPanel)
        mainPanel.setBounds(1765f, 0f, 155f, 1080f)
    }

    private fun addDialog() {
        addAndFillActor(Image(screen.drawerUtil.getRegion(GameColor.background.cpy().apply { a = 0.65f })))
    }
    
}