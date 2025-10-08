package com.lewydo.rozval.game.manager

import com.badlogic.gdx.Gdx
import com.lewydo.rozval.game.GDXGame
import com.lewydo.rozval.game.screens.*
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen
import com.lewydo.rozval.game.utils.runGDX

class NavigationManager(val game: GDXGame) {

    private val backStack = mutableListOf<String>()
    var key: Int? = null
        private set

    fun navigate(toScreenName: String, fromScreenName: String? = null, key: Int? = null) = runGDX {
        this.key = key

        game.updateScreen(getScreenByName(toScreenName))
        backStack.filter { name -> name == toScreenName }.onEach { name -> backStack.remove(name) }
        fromScreenName?.let { fromName ->
            backStack.filter { name -> name == fromName }.onEach { name -> backStack.remove(name) }
            backStack.add(fromName)
        }
    }

    fun back(key: Int? = null) = runGDX {
        this.key = key

        if (isBackStackEmpty()) exit() else game.updateScreen(getScreenByName(backStack.removeAt(backStack.lastIndex)))
    }


    fun exit() = runGDX { Gdx.app.exit() }


    fun isBackStackEmpty() = backStack.isEmpty()

    private fun getScreenByName(name: String): AdvancedScreen = when(name) {
        LoaderScreen::class.java.name -> LoaderScreen(game)
        MenuScreen  ::class.java.name -> MenuScreen(game)
        GameScreen  ::class.java.name -> GameScreen(game)

        TestBox2dScreen::class.java.name -> TestBox2dScreen(game)
        TestShaderScreen::class.java.name -> TestShaderScreen(game)

        else -> MenuScreen(game)
    }

}