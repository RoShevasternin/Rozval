package com.roshevasternin.rozval.game.actors.counter

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class AStarCounter(override val screen: AdvancedScreen, labelStyle: Label.LabelStyle): AdvancedGroup() {

    private val starImg = Image(screen.game.assetsAll.star_fill)
    private val starLbl = Label(screen.game.starUtil.starFlow.value.toString(), labelStyle)

    override fun addActorsOnGroup() {
        addActors(starImg, starLbl)
        starImg.setBounds(0f, 3f, 40f, 40f)
        starLbl.apply {
            setBounds(50f, 0f, 90f, 47f)
            setAlignment(Align.center)
        }

        collectStars()
    }

    // Logic ------------------------------------------------------------------------

    private fun collectStars() {
        coroutine?.launch {
            screen.game.starUtil.starFlow.collect { star ->
                runGDX { starLbl.setText(star) }
            }
        }
    }

}