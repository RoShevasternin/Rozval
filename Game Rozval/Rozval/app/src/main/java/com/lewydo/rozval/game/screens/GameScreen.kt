package com.lewydo.rozval.game.screens

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.lewydo.rozval.game.actors.button.location.button.ALockStarKeyLevelButton
import com.lewydo.rozval.game.actors.main.APanelGame
import com.lewydo.rozval.game.box2d.WorldUtil
import com.lewydo.rozval.game.utils.*
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.actor.animShow
import com.lewydo.rozval.game.utils.actor.setBounds
import com.lewydo.rozval.game.utils.advanced.AdvancedBox2dScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedStage
import com.lewydo.rozval.game.utils.font.FontParameter

class GameScreen(): AdvancedBox2dScreen(WorldUtil()) {

    private val assetsAll = gdxGame.assetsAll

    private val panelMenu = APanelGame(this).apply { color.a = 0f }

    private val parameter = FontParameter()
    private val font29    = fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setCharacters(FontParameter.CharType.NUMBERS.chars + "or/").setSize(29))

    private val labelStyle29 = LabelStyle(font29, GameColor.black)

    override fun show() {
        stageBack.root.color.a = 0f
        setUIBackground(assetsAll.LVL_1.region)
        super.show()
        stageBack.root.animShow(TIME_ANIM_SCREEN)

        val abtn = ALockStarKeyLevelButton(this, 2, labelStyle29, 20, 0, 10)
        stageUI.addActor(abtn)
        abtn.setBounds(100f, 100f, 250f, 300f)
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
            mainPanel.menuBtnBlock = { hideScreen { gdxGame.navigationManager.back() } }
        }

    }

}