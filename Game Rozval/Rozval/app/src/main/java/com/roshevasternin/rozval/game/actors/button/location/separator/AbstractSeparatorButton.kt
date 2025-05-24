package com.roshevasternin.rozval.game.actors.button.location.separator

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup

abstract class AbstractSeparatorButton: AdvancedGroup() {

    abstract val title     : String
    abstract val labelStyle: Label.LabelStyle

}