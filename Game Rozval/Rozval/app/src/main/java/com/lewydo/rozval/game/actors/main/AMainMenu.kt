package com.lewydo.rozval.game.actors.main

import com.lewydo.rozval.game.actors.autoLayout.ATableGroup
import com.lewydo.rozval.game.actors.autoLayout.AutoLayout
import com.lewydo.rozval.game.actors.button.location.button.AAvailableLevelButton
import com.lewydo.rozval.game.actors.button.location.button.ALockStarKeyLevelButton
import com.lewydo.rozval.game.actors.button.location.button.ALockStarLevelButton
import com.lewydo.rozval.game.actors.button.location.button.AbstractLevelButton
import com.lewydo.rozval.game.actors.button.location.separator.ALockSeparatorButton
import com.lewydo.rozval.game.actors.button.location.separator.AbstractSeparatorButton
import com.lewydo.rozval.game.actors.mainPanel.MenuMainPanel
import com.lewydo.rozval.game.screens.MenuScreen
import com.lewydo.rozval.game.utils.TIME_ANIM_SCREEN
import com.lewydo.rozval.game.utils.actor.animDelay
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.actor.animShow
import com.lewydo.rozval.game.utils.advanced.AdvancedMainGroup
import com.lewydo.rozval.game.utils.manager.LevelButtonManager
import com.lewydo.rozval.game.utils.manager.LocationManager
import kotlinx.coroutines.launch

class AMainMenu(override val screen: MenuScreen): AdvancedMainGroup() {

    val mainPanel          = MenuMainPanel(screen)
    private val tableGroup = getTableGroup()

    // Field
    private val locationManager = LocationManager(screen)
    private val locationsUtil   = screen.game.ds_Location
    private val keyUtil         = screen.game.ds_key

    var availableLevelBtnBlock: (Int) -> Unit = {}

    override fun addActorsOnGroup() {
        color.a = 0f

        addMainPanel()
        addTableGroup()

        animShowMain {
            coroutine?.launch { addLevels() }
        }

    }

    override fun animShowMain(blockEnd: Runnable) {
        children.onEach { it.clearActions() }

        this.animShow(TIME_ANIM_SCREEN)
        this.animDelay(TIME_ANIM_SCREEN) { blockEnd.run() }
    }

    override fun animHideMain(blockEnd: Runnable) {
        children.onEach { it.clearActions() }

        this.animHide(TIME_ANIM_SCREEN)
        this.animDelay(TIME_ANIM_SCREEN) { blockEnd.run() }
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
                    availableLevelBtnBlock.invoke(locationType.startLevel+indexBtn)
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