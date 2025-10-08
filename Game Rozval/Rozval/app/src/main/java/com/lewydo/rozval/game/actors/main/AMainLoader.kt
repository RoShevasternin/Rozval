package com.lewydo.rozval.game.actors.main

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import com.lewydo.rozval.game.screens.LoaderScreen
import com.lewydo.rozval.game.utils.GameColor
import com.lewydo.rozval.game.utils.TIME_ANIM_SCREEN
import com.lewydo.rozval.game.utils.actor.animDelay
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.actor.animShow
import com.lewydo.rozval.game.utils.actor.setBounds
import com.lewydo.rozval.game.utils.advanced.AdvancedMainGroup
import com.lewydo.rozval.game.utils.font.FontParameter
import com.lewydo.rozval.game.utils.Layout.Loader as LL

class AMainLoader(override val screen: LoaderScreen): AdvancedMainGroup() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL).setSize(66)
    private val font      = screen.fontGenerator_LondrinaSolid_Regular.generateFont(parameter)

    val loaderImg        = Image(screen.game.assetsLoader.loader)
    val loadingLbl       = Label("Loading", Label.LabelStyle(font, GameColor.white))
    val loadingPointsLbl = Label("", Label.LabelStyle(font, GameColor.white))
    val progressLbl      = Label("", Label.LabelStyle(font, GameColor.white))
    val builderImg       = Image(screen.game.assetsLoader.builderList.random())


    override fun addActorsOnGroup() {
        addLoader()
        addLoading()
        addProgress()
        addBuilder()
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

    // Actors ------------------------------------------------------------------------

    private fun addLoader() {
        addActor(loaderImg)
        loaderImg.apply {
            setBounds(LL.loader)
            setOrigin(Align.center)
            addAction(Actions.forever(Actions.rotateBy(-360f, 5f, Interpolation.linear)))
        }
    }

    private fun addLoading() {
        addActors(loadingLbl, loadingPointsLbl)
        loadingLbl.setBounds(LL.loading)

        loadingPointsLbl.apply {
            setBounds(LL.loadingPoints)

            val time = 0.23f
            addAction(Actions.forever(Actions.sequence(
                Actions.run { setText("") },
                Actions.delay(time),
                Actions.run { setText(".") },
                Actions.delay(time),
                Actions.run { setText("..") },
                Actions.delay(time),
                Actions.run { setText("...") },
                Actions.delay(time),
                Actions.run { setText("..") },
                Actions.delay(time),
                Actions.run { setText(".") },
                Actions.delay(time),
            )))
        }
    }

    private fun addProgress() {
        addActor(progressLbl)
        progressLbl.apply {
            setBounds(LL.progress)
            setAlignment(Align.center)
        }
    }

    private fun addBuilder() {
        addActor(builderImg)
        builderImg.setBounds(LL.builder)
    }

}