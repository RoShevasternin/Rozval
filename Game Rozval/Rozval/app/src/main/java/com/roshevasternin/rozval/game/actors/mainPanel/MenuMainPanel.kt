package com.roshevasternin.rozval.game.actors.mainPanel

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.roshevasternin.rozval.game.actors.button.AButton
import com.roshevasternin.rozval.game.actors.counter.AKeyCounter
import com.roshevasternin.rozval.game.actors.counter.AStarCounter
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.font.FontParameter

class MenuMainPanel(_screen: AdvancedScreen): AbstractMainPanel(_screen) {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font40    = screen.fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setSize(40))

    private val labelStyle40 = LabelStyle(font40, GameColor.white)

    private val exitBtn      = AButton(screen, AButton.Type.Exit)
    private val bonusGameBtn = AButton(screen, AButton.Type.BonusGame)
    private val starCounter  = AStarCounter(screen, labelStyle40)
    private val keyCounter   = AKeyCounter(screen, labelStyle40)

    var exitBtnBlock      = {}
    var bonusGameBtnBlock = {}

    override fun AdvancedGroup.addActorsOnParentGroup() {
        addExitBtn()
        addBonusGameBtn()
        addStar()
        addKey()
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedGroup.addExitBtn() {
        addActor(exitBtn)
        exitBtn.apply {
            setBounds(18f, 35f, 124f, 51f)
            setOnClickListener { exitBtnBlock() }
        }
    }

    private fun AdvancedGroup.addBonusGameBtn() {
        addActor(bonusGameBtn)
        bonusGameBtn.apply {
            setBounds(5f, 825f, 150f, 96f)
            setOnClickListener { bonusGameBtnBlock() }
        }
    }

    private fun AdvancedGroup.addStar() {
        addActor(starCounter)
        starCounter.setBounds(15f, 1008f, 140f, 47f)
    }

    private fun AdvancedGroup.addKey() {
        addActor(keyCounter)
        keyCounter.setBounds(15f, 941f, 140f, 47f)
    }

}