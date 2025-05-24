package com.roshevasternin.rozval.game.actors

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen

class ALabel(override val screen: AdvancedScreen, text: CharSequence, labelStyle: LabelStyle): AdvancedGroup() {

    val label = Label(text, labelStyle)

    override fun addActorsOnGroup() {
        addActor(label)
        label.setSize(width, height)
    }

}