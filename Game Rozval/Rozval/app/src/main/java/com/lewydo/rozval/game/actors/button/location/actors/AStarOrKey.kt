package com.lewydo.rozval.game.actors.button.location.actors

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.lewydo.rozval.game.actors.autoLayout.AHorizontalGroup
import com.lewydo.rozval.game.actors.autoLayout.AutoLayout
import com.lewydo.rozval.game.utils.SizeScaler
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen

class AStarOrKey(
    override val screen: AdvancedScreen,
    labelStyle   : LabelStyle,
    starCount    : Int,
    keyCount     : Int,
    necessaryKeys: Int
): AdvancedGroup() {

    override val sizeScaler = SizeScaler(SizeScaler.Axis.Y,30f)

    private val starNecessary = AStarsNecessary(screen, starCount, labelStyle)
    private val orLbl         = Label("or", labelStyle)
    private val keyNecessary  = AKeysNecessary(screen, keyCount, necessaryKeys, labelStyle)


    private val horizontalGroup = AHorizontalGroup(screen,
        alignmentH = AutoLayout.AlignmentHorizontal.AUTO,
        alignmentV = AutoLayout.AlignmentVertical.CENTER,
    )

    override fun addActorsOnGroup() {
        addAndFillActor(horizontalGroup)
        horizontalGroup.apply {
            addStarHorizontalGroup()
            addOrLabel()
            addKeyHorizontalGroup()
        }
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedGroup.addStarHorizontalGroup() {
        starNecessary.height = height
        addActor(starNecessary)
    }

    private fun AdvancedGroup.addOrLabel() {
        orLbl.apply {
            height = 30f.scaled
            setAlignment(Align.center)
        }
        addActor(orLbl)
    }

    private fun AdvancedGroup.addKeyHorizontalGroup() {
        keyNecessary.height = height
        addActor(keyNecessary)
    }

    // Logic ------------------------------------------------------------------------

    fun enableKey() {
        keyNecessary.enableKey()
    }

    fun disableKey() {
        keyNecessary.disableKey()
    }

}