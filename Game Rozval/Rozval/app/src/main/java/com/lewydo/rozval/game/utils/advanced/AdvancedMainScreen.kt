package com.lewydo.rozval.game.utils.advanced

abstract class AdvancedMainScreen : AdvancedScreen() {

    abstract val aMain: AdvancedMainGroup

    abstract fun AdvancedStage.addMain()

}