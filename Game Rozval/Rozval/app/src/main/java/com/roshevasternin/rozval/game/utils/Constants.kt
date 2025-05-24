package com.roshevasternin.rozval.game.utils

import com.badlogic.gdx.math.Vector2
import kotlin.math.PI

const val WIDTH_UI     = 1920f //2000f
const val HEIGHT_UI    = 1080f //2000f
const val WIDTH_BOX2D  = 20f
const val HEIGHT_BOX2D = 11.25f

const val METER_UI = WIDTH_UI / WIDTH_BOX2D

val Vector2.scaledToB2 get() = this.divOr0(METER_UI) // convert UI to Box2d
val Vector2.scaledToUI get() = this.scl(METER_UI) // convert Box2d to UI
val Float.scaledToB2 get() = this.divOr0(METER_UI) // convert UI to Box2d
val Float.scaledToUI get() = this * METER_UI // convert Box2d to UI

const val DEGTORAD = (PI / 180f).toFloat()
const val RADTODEG = (180f / PI).toFloat()

const val JOINT_WIDTH = 1f

const val TIME_ANIM_SCREEN_ALPHA = 0.4f