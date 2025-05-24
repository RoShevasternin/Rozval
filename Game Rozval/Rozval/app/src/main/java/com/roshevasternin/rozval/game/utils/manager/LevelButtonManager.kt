package com.roshevasternin.rozval.game.utils.manager

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.roshevasternin.rozval.game.actors.button.location.button.AAvailableLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.ALockStarKeyLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.ALockStarLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.AbstractLevelButton
import com.roshevasternin.rozval.game.data.Level
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.font.FontParameter
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class LevelButtonManager(val screen: AdvancedScreen) {

    companion object {
        const val NECESSARY_KEY = 2
    }

    private val parameter = FontParameter()
    private val font29    = screen.fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setCharacters(FontParameter.CharType.NUMBERS.chars + "or/").setSize(29))

    private val labelStyle29 = LabelStyle(font29, GameColor.black)

    private val starCount = screen.game.starUtil.starFlow.value
    private val keyCount  = screen.game.keyUtil.keyFlow.value
    private val locations = screen.game.locationsUtil.locationsFlow.value

    private val buttonsList     = mutableListOf<AbstractLevelButton>()
    private val isButtonOpenKey = AtomicBoolean(false)

    private lateinit var tmpButton        : AbstractLevelButton
    private lateinit var tmpLocationLevels: List<Level>

    private var tmpLevel = 0

    val mapLocationLevelButton = mutableMapOf<LocationManager.LocationType, List<AbstractLevelButton>>()
    val listAllLevelButton     = mutableListOf<AbstractLevelButton>()

    init {
        collectKey()
    }

    fun getButtons(locationType: LocationManager.LocationType): List<AbstractLevelButton> {
        buttonsList.clear()
        isButtonOpenKey.set(false)

        tmpLocationLevels = locations[locationType.ordinal].levels

        locationType.listLevelNecessaryStars.onEachIndexed { index, necessaryStars ->
            tmpLevel = (locationType.firstLevel + index)

            tmpButton = if (starCount >= necessaryStars) {
                isButtonOpenKey.set(true)

                AAvailableLevelButton(screen, tmpLevel, labelStyle29).also { btn ->
                    btn.starsLvL.fill(tmpLocationLevels[index].star)
                }
            } else when {
                tmpLocationLevels[index].isOpen -> {
                    isButtonOpenKey.set(true)

                    AAvailableLevelButton(screen, tmpLevel, labelStyle29).also { btn ->
                        btn.starsLvL.fill(tmpLocationLevels[index].star)
                    }
                }

                isButtonOpenKey.getAndSet(false) -> {
                    ALockStarKeyLevelButton(screen, tmpLevel, labelStyle29, necessaryStars, keyCount, NECESSARY_KEY)
                }

                else -> {
                    ALockStarLevelButton(screen, tmpLevel, labelStyle29, necessaryStars)
                }
            }

            tmpButton.setSize(250f, 300f)
            buttonsList.add(tmpButton)
        }

        listAllLevelButton.addAll(buttonsList)
        mapLocationLevelButton[locationType] = buttonsList.toList()

        return buttonsList
    }

    private fun collectKey() {
        screen.coroutine?.launch {
            screen.game.keyUtil.keyFlow.collect { keyCount ->
                listAllLevelButton.onEach { btn ->
                    if(btn is ALockStarKeyLevelButton) {
                        if (keyCount < btn.necessaryKeys) btn.disableOfKey()
                    }
                }
            }
        }
    }

}