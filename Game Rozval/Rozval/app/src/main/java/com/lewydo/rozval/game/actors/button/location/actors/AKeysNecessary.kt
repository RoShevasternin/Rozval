package com.lewydo.rozval.game.actors.button.location.actors

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.lewydo.rozval.game.actors.autoLayout.AHorizontalGroup
import com.lewydo.rozval.game.actors.autoLayout.AutoLayout
import com.lewydo.rozval.game.utils.GameColor
import com.lewydo.rozval.game.utils.SizeScaler
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen
import com.lewydo.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class AKeysNecessary(
    override val screen: AdvancedScreen,
    keyCount : Int,
    private val necessaryKeys: Int,
    labelStyle: LabelStyle,
): AHorizontalGroup(screen,
    alignmentH = AutoLayout.AlignmentHorizontal.CENTER,
    alignmentV = AutoLayout.AlignmentVertical.CENTER,
    isWrapH = true,
) {

    override val sizeScaler = SizeScaler(SizeScaler.Axis.Y,30f)

    private val assetsAll = screen.game.assetsAll


    private val keyImg      = Image(assetsAll.key_grey)
    private val keyCountLbl = Label("${getNecessaryKeyCount(keyCount)}/$necessaryKeys", labelStyle)

    override fun addActorsOnGroup() {
        gap = 7f.scaled
        addKeys()
    }

    // Actors ------------------------------------------------------------------------

    private fun addKeys() {
        keyImg.setSizeScaled(30f, 30f)

        keyCountLbl.height = 30f.scaled
        keyCountLbl.setAlignment(Align.center)

        addActors(keyImg, keyCountLbl)

        collectKeys()
    }

    fun enableKey() {
        keyImg.drawable = TextureRegionDrawable(assetsAll.key_fill)
        keyCountLbl.apply { style = LabelStyle(style).also { it.fontColor = GameColor.gold } }
    }

    fun disableKey() {
        keyImg.drawable = TextureRegionDrawable(assetsAll.key_grey)
        keyCountLbl.apply { style = LabelStyle(style).also { it.fontColor = GameColor.black } }
    }

    // Logic ------------------------------------------------------------------------

    private fun getNecessaryKeyCount(currentKeyCount: Int) = if (currentKeyCount >= necessaryKeys) necessaryKeys else currentKeyCount

    private fun collectKeys() {
        coroutine?.launch {
            screen.game.ds_key.flow.collect { key ->
                runGDX { keyCountLbl.setText("${getNecessaryKeyCount(key)}/$necessaryKeys") }
            }
        }
    }

}