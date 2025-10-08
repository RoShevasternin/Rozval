package com.lewydo.rozval.game.screens

import com.lewydo.rozval.game.GDXGame
import com.lewydo.rozval.game.actors.main.APanelGame
import com.lewydo.rozval.game.box2d.WorldUtil
import com.lewydo.rozval.game.utils.Block
import com.lewydo.rozval.game.utils.TIME_ANIM_SCREEN
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.actor.animShow
import com.lewydo.rozval.game.utils.advanced.AdvancedBox2dScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedStage
import com.lewydo.rozval.game.utils.region
import com.lewydo.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class GameScreen(override val game: GDXGame): AdvancedBox2dScreen(WorldUtil()) {

    private val assetsAll = game.assetsAll

    private val panelMenu = APanelGame(this).apply { color.a = 0f }

    override fun show() {
        stageBack.root.color.a = 0f
        setUIBackground(assetsAll.LVL_1.region)
        super.show()
        stageBack.root.animShow(TIME_ANIM_SCREEN)
    }

    override fun AdvancedStage.addActorsOnStageBox2d() {

    }

    override fun AdvancedStage.addActorsOnStageUI() {
        panelMenu.animShow(TIME_ANIM_SCREEN)
    }

    override fun hideScreen(block: Block) {
        runGDX {
            stageBack.root.animHide(TIME_ANIM_SCREEN)
        }
        panelMenu.animHide(TIME_ANIM_SCREEN) { block.invoke() }
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedGroup.addPanel() {
        addAndFillActor(panelMenu)

        panelMenu.apply {
            mainPanel.menuBtnBlock = { hideScreen { game.navigationManager.back() } }
        }

    }

}