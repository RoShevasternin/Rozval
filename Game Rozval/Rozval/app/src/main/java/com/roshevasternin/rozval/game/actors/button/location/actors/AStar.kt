package com.roshevasternin.rozval.game.actors.button.location.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen

class AStar(override val screen: AdvancedScreen): AdvancedGroup() {

    private val assetsAll = screen.game.assetsAll

    private val image = Image(assetsAll.star_empty)

    override fun addActorsOnGroup() {
        addAndFillActor(image)
    }

    fun fill() {
        image.drawable = TextureRegionDrawable(assetsAll.star_fill)
    }

}