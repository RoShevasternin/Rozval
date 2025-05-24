package com.roshevasternin.rozval.game.actors.counter

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AKeyCounter(override val screen: AdvancedScreen, labelStyle: Label.LabelStyle): AdvancedGroup() {

    private val keyImg = Image(screen.game.assetsAll.key_fill)
    private val keyLbl = Label(screen.game.keyUtil.keyFlow.value.toString(), labelStyle)

    override fun addActorsOnGroup() {
        addActors(keyImg, keyLbl)
        keyImg.setBounds(0f, 3f, 40f, 40f)
        keyLbl.apply {
            setBounds(50f, 0f, 90f, 47f)
            setAlignment(Align.center)
        }

        collectKeys()
    }

    // Logic ------------------------------------------------------------------------

    private fun collectKeys() {
        coroutine?.launch {
            screen.game.keyUtil.keyFlow.collect { key ->
                runGDX { keyLbl.setText(key) }
            }
        }
    }

}