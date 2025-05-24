package com.roshevasternin.rozval.game.actors.button

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.runGDX
import com.roshevasternin.rozval.util.log

class AButtonWithText(
    override val screen: AdvancedScreen,
    text         : String,
    labelStyleLvL: LabelStyle,
) : AButton(screen, Type.Exit) {

    private val lvlLbl = Label(text, labelStyleLvL)

    override fun addActorsOnGroup() {
        super.addActorsOnGroup()
        addLvlLbl()

        lock()
    }

    // Actors ------------------------------------------------------------------------
    private fun addLvlLbl() {
        addActor(lvlLbl)
        lvlLbl.apply {
            setBounds(416f, 22f, 174f, 59f)
            setAlignment(Align.center)
        }
    }

    // Logic ------------------------------------------------------------------------

    fun lock() {
        disable()
        //lvlLbl.color.set(GameColor.lock)
    }

    fun unlock() {
        runGDX {
            enable()
            //lvlLbl.color.set(GameColor.unlock)
        }
    }

}