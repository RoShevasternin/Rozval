package com.testios.first

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class Main : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var image: Texture? = null

    override fun create() {
        batch = SpriteBatch()
        image = Texture("libgdx.png")
    }

    override fun render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch!!.begin()
        batch!!.draw(image, 140f, 210f)
        batch!!.end()
    }

    override fun dispose() {
        batch!!.dispose()
        image!!.dispose()
    }
}
