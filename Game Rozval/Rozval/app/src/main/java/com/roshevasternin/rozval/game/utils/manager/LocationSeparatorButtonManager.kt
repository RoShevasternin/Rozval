package com.roshevasternin.rozval.game.utils.manager

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.roshevasternin.rozval.game.actors.button.location.separator.AAvailableSeparatorButton
import com.roshevasternin.rozval.game.actors.button.location.separator.ALockSeparatorButton
import com.roshevasternin.rozval.game.actors.button.location.separator.AbstractSeparatorButton
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.font.FontParameter
import kotlinx.coroutines.launch

class LocationSeparatorButtonManager(val screen: AdvancedScreen) {

    private val parameter = FontParameter()
    private val font50    = screen.fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setCharacters(FontParameter.CharType.ALL).setSize(50))

    private val labelStyle50 = LabelStyle(font50, GameColor.black)

    private val starCount = screen.game.starUtil.starFlow.value
    private val keyCount  = screen.game.keyUtil.keyFlow.value
    private val locations = screen.game.locationsUtil.locationsFlow.value

    private lateinit var tmpButton: AbstractSeparatorButton

    val listAllSeparatorButton = mutableListOf<AbstractSeparatorButton>()

    init {
        collectKey()
    }

    fun getSeparatorButton(locationType: LocationManager.LocationType): AbstractSeparatorButton {
        tmpButton = if (starCount >= locationType.separatorNecessaryStars) {
            AAvailableSeparatorButton(screen, locationType.separatorName, labelStyle50)
        } else when {
            locations[locationType.ordinal].isOpen -> {
                AAvailableSeparatorButton(screen, locationType.separatorName, labelStyle50)
            }
            else -> {
                ALockSeparatorButton(screen, locationType.separatorName, labelStyle50, locationType.separatorNecessaryStars, keyCount, locationType.separatorNecessaryKeys)
            }
        }

        tmpButton.setSize(1765f, 110f)
        listAllSeparatorButton.add(tmpButton)

        return tmpButton
    }

    private fun collectKey() {
        screen.coroutine?.launch {
            screen.game.keyUtil.keyFlow.collect { keyCount ->
                listAllSeparatorButton.onEach { btn ->
                    if(btn is ALockSeparatorButton) {
                        if (keyCount < btn.necessaryKeys) btn.disableOfKey()
                    }
                }
            }
        }
    }

}