package com.roshevasternin.rozval.game.actors.counter

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class ALvLCounter(override val screen: AdvancedScreen, labelStyle: Label.LabelStyle, lvl: Int): AdvancedGroup() {

    private val lvlImg = Image(screen.game.assetsAll.lvl)
    private val lvlLbl = Label(lvl.toString(), labelStyle)

    override fun addActorsOnGroup() {
        addActors(lvlImg, lvlLbl)
        lvlImg.setBounds(0f, 3f, 47f, 47f)
        lvlLbl.apply {
            setBounds(50f, 0f, 90f, 47f)
            setAlignment(Align.center)
        }
    }

}