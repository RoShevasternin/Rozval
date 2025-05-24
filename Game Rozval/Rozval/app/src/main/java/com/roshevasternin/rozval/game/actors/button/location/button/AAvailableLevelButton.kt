package com.roshevasternin.rozval.game.actors.button.location.button

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.roshevasternin.rozval.game.actors.button.location.actors.AStars3
import com.roshevasternin.rozval.game.actors.button.AButton
import com.roshevasternin.rozval.game.utils.Block
import com.roshevasternin.rozval.game.utils.actor.disable
import com.roshevasternin.rozval.game.utils.actor.setOnClickListener
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen

class AAvailableLevelButton(
    override val screen    : AdvancedScreen,
    override val level     : Int,
    override val labelStyle: Label.LabelStyle,
) : AbstractLevelButton() {

    private val assetsAll = screen.game.assetsAll

    private val levelBtnStyle = AButton.AButtonStyle(
        default  = TextureRegionDrawable(assetsAll.list_LvlBtnDef[level-1]),
        pressed  = NinePatchDrawable(assetsAll.lvl_btn_press),
        disabled = NinePatchDrawable(assetsAll.lvl_btn_dis),
    )

    // Actors
    private val button = AButton(screen, AButton.Type.None).apply { setStyle(levelBtnStyle) }

    val starsLvL = AStars3(screen)

    override fun addActorsOnGroup() {
        setSaturationPreview(1f)

        addAndFillActor(button)
        super.addActorsOnGroup()

        addStarsLvL()
    }

    // Actors ------------------------------------------------------------------------

    private fun addStarsLvL() {
        addActor(starsLvL)
        starsLvL.disable()
        starsLvL.setBounds(73f, 17f, 104f, 30f)
    }

    // Logic ------------------------------------------------------------------------

    fun setOnClickBlock(block: () -> Unit) {
        button.setOnClickListener {
            block()
        }
    }

}