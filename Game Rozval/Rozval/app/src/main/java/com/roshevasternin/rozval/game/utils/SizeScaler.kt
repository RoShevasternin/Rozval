package com.roshevasternin.rozval.game.utils

import com.badlogic.gdx.math.Vector2

class SizeScaler(val axis: Axis, val originalSize: Float) {

    private var scale       = 1f
    private var newAxisSize = 0f

    fun calculateScale(newSize: Vector2) {
        newAxisSize = when(axis) {
            Axis.X -> newSize.x
            Axis.Y -> newSize.y
        }
        scale = originalSize.divOr0(newAxisSize)
    }

    fun scaled(size: Vector2): Vector2 {
        return size.divOr0(scale)
    }

    fun scaledInverse(size: Vector2): Vector2 {
        return size.scl(scale)
    }

    fun scaled(size: Float): Float {
        return size.divOr0(scale)
    }
    fun scaledInverse(size: Float): Float {
        return (size * scale)
    }

    enum class Axis {
        X, Y
    }

}