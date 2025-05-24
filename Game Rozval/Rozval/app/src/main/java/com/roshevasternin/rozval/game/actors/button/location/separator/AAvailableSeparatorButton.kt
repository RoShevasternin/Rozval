package com.roshevasternin.rozval.game.actors.button.location.separator

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen

class AAvailableSeparatorButton(
    override val screen    : AdvancedScreen,
    override val title     : String,
    override val labelStyle: LabelStyle,
) : AbstractSeparatorButton() {

    private val assetsAll = screen.game.assetsAll

    private val newLabelStyle = LabelStyle(labelStyle).also { it.fontColor = GameColor.white }

    // Actors
    private val separatorImg = Image(assetsAll.LEVEL_SEPARATOR)
    private val titleLbl     = Label(title, newLabelStyle)

    override fun addActorsOnGroup() {
        addAndFillActors(separatorImg, titleLbl)
        titleLbl.setAlignment(Align.center)
    }

}