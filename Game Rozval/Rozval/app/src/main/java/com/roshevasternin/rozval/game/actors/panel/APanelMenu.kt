package com.roshevasternin.rozval.game.actors.panel

import com.roshevasternin.rozval.game.actors.autoLayout.ATableGroup
import com.roshevasternin.rozval.game.actors.autoLayout.AutoLayout
import com.roshevasternin.rozval.game.actors.button.location.button.AAvailableLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.ALockStarKeyLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.ALockStarLevelButton
import com.roshevasternin.rozval.game.actors.button.location.button.AbstractLevelButton
import com.roshevasternin.rozval.game.actors.button.location.separator.ALockSeparatorButton
import com.roshevasternin.rozval.game.actors.button.location.separator.AbstractSeparatorButton
import com.roshevasternin.rozval.game.actors.mainPanel.MenuMainPanel
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.manager.LevelButtonManager
import com.roshevasternin.rozval.game.utils.manager.LocationManager
import com.roshevasternin.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class APanelMenu(override val screen: AdvancedScreen): AdvancedGroup() {

    val mainPanel          = MenuMainPanel(screen)
    private val tableGroup = getTableGroup()

    // Field
    private val locationManager = LocationManager(screen)
    private val locationsUtil   = screen.game.locationsUtil
    private val keyUtil         = screen.game.keyUtil

    var availableLevelBtnBlock: (Int) -> Unit = {}

    override fun addActorsOnGroup() {
        coroutine?.launch {
            runGDX {
                addMainPanel()
                addTableGroup()
            }

            addLevels()
        }
    }

    // Init ------------------------------------------------------------------------

    private fun getTableGroup(): ATableGroup {
        return ATableGroup(screen, startGapV = 50f, endGapV = 50f, gapV = 50f, gapH = 85f,
            alignmentVH = AutoLayout.AlignmentHorizontal.CENTER,
            alignmentVV = AutoLayout.AlignmentVertical.TOP,
            alignmentHH = AutoLayout.AlignmentHorizontal.CENTER,
            alignmentHV = AutoLayout.AlignmentVertical.CENTER,
        )
    }

    // Actors ------------------------------------------------------------------------

    private fun addMainPanel() {
        addActor(mainPanel)
        mainPanel.setBounds(1765f, 0f, 155f, 1080f)
    }

    private fun addTableGroup() {
        addActor(tableGroup)
        tableGroup.setBounds(0f, 0f, 1765f, 1080f)
    }

    private suspend fun addLevels() {
        LocationManager.LocationType.entries.onEach { locationType ->
            locationManager.locationType = locationType
            locationManager.getSeparatorButton().also { separatorBtn ->
                separatorBtn.setOnClickBlock(locationType)
                tableGroup.addToTable(separatorBtn)
            }
            locationManager.getButtonList().onEachIndexed { index, btn ->
                btn.setOnClickBlock(locationType, index)
                tableGroup.addToTable(btn)
            }
        }
    }

    // Logic ------------------------------------------------------------------------

    private fun AbstractSeparatorButton.setOnClickBlock(locationType: LocationManager.LocationType) {
        if (this is ALockSeparatorButton) {
            onClickBlock = {
                changeToAvailableSeparatorButton()
                (locationManager.levelButtonManager.mapLocationLevelButton[locationType]?.first() as? ALockStarLevelButton)?.changeToAvailableLevelButton()

                keyUtil.update { it - locationType.separatorNecessaryKeys }
                locationsUtil.update { locations ->
                    locations.apply { get(locationType.ordinal).apply {
                        isOpen = true
                        levels.first().isOpen = true
                    } }
                }

            }
        }
    }

    private fun AbstractLevelButton.setOnClickBlock(locationType: LocationManager.LocationType, indexBtn: Int) {
        when(this) {
            is AAvailableLevelButton -> {
                setOnClickBlock {
                    availableLevelBtnBlock.invoke(locationType.firstLevel+indexBtn)
                }
            }
            is ALockStarKeyLevelButton -> {
                onClickBlock = {
                    changeToAvailableLevelButton()

                    keyUtil.update { it - LevelButtonManager.NECESSARY_KEY }
                    locationsUtil.update { locations ->
                        locations.apply { get(locationType.ordinal).apply {
                            levels[indexBtn].isOpen = true
                        } }
                    }

                }
            }
        }
    }
    
}