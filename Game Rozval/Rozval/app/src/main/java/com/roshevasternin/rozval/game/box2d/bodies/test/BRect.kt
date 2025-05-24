package com.roshevasternin.rozval.game.box2d.bodies.test

import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.roshevasternin.rozval.game.actors.AImage
import com.roshevasternin.rozval.game.box2d.AbstractBody
import com.roshevasternin.rozval.game.utils.advanced.AdvancedBox2dScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup

class BRect(override val screenBox2d: AdvancedBox2dScreen): AbstractBody() {
    override val name       = "rect"
    override val bodyDef    = BodyDef().apply {
        type = BodyDef.BodyType.StaticBody
    }
    override val fixtureDef = FixtureDef()

    override var actor: AdvancedGroup? = AImage(screenBox2d, screenBox2d.game.assetsAll.list_PreviewLvl.first())

}