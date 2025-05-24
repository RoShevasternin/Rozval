package com.roshevasternin.rozval.game.box2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Joint
import com.badlogic.gdx.physics.box2d.JointDef
import com.badlogic.gdx.utils.Disposable
import com.roshevasternin.rozval.game.utils.GameColor
import com.roshevasternin.rozval.game.utils.SizeScaler
import com.roshevasternin.rozval.game.utils.actor.setBounds
import com.roshevasternin.rozval.game.utils.advanced.AdvancedBox2dScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedGroup
import com.roshevasternin.rozval.game.utils.disposeAll
import com.roshevasternin.rozval.game.utils.scaledToB2
import com.roshevasternin.rozval.game.utils.scaledToUI
import com.roshevasternin.rozval.util.cancelCoroutinesAll
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class AbstractBodyGroup: Destroyable {
    abstract val screenBox2d: AdvancedBox2dScreen
    abstract val sizeScaler: SizeScaler

    private val _bodyList  = mutableSetOf<AbstractBody>()
    private val _actorList = mutableSetOf<AdvancedGroup>()
    val disposableSet      = mutableSetOf<Disposable>()
    val destroyableSet     = mutableSetOf<Destroyable>()

    val bodyList get()  = _bodyList.toList()
    val actorList get() = _actorList.toList()

    val Vector2.scaled        get() = sizeScaler.scaled(this)
    val Vector2.scaledInverse get() = sizeScaler.scaledInverse(this)
    val Float.scaled          get() = sizeScaler.scaled(this)
    val Float.scaledInverse   get() = sizeScaler.scaledInverse(this)

    var coroutine: CoroutineScope? = null
        private set

    val position = Vector2()
    val size     = Vector2()

    private val tmpPositionA = Vector2()
    private val tmpPositionB = Vector2()
    private val tmpAnchorA   = Vector2()
    private val tmpAnchorB   = Vector2()

    val colorJoint = GameColor.background//joint.cpy()

    open fun create(x: Float, y: Float, w: Float, h: Float) {
        position.set(x,y)
        size.set(w,h)

        coroutine = CoroutineScope(Dispatchers.Default)
        sizeScaler.calculateScale(Vector2(w, h))
    }

    override fun destroy() {
        log("destroy: ${this::class.java.name.substringAfterLast('.')}")
        cancelCoroutinesAll(coroutine)
        disposableSet.disposeAll()
        destroyableSet.destroyAll()
        _bodyList.destroyAll()
        _actorList.disposeAll()
    }

    fun createBody(body: AbstractBody, pos: Vector2, size: Vector2) {
        body.create(position.cpy().add(pos.scaled), size.scaled)
        _bodyList.add(body)
        body.actor?.let { _actorList.add(it) }
    }

    fun createBody(body: AbstractBody, x: Float, y: Float, w: Float, h: Float) {
        createBody(body, Vector2(x, y), Vector2(w, h))
    }

    fun <T : JointDef> createJoint(joint: AbstractJoint<out Joint, T>, jointDef: T) {
        joint.create(jointDef)
    }

    fun createBodyGroup(bodyGroup: AbstractBodyGroup, pos: Vector2, size: Vector2) {
        val resultPosition = position.cpy().add(pos.scaled)
        val resultSize     = size.scaled

        bodyGroup.create(resultPosition.x, resultPosition.y, resultSize.x, resultSize.y)
        destroyableSet.add(bodyGroup)
    }

    fun createBodyGroup(bodyGroup: AbstractBodyGroup, x: Float, y: Float, w: Float, h: Float) {
        createBodyGroup(bodyGroup, Vector2(x, y), Vector2(w, h))
    }

    protected fun Vector2.subCenter(body: Body): Vector2 = this.scaled.scaledToB2.sub((body.userData as AbstractBody).center)

    protected fun AdvancedGroup.setBoundsStandartBG(x: Float, y: Float, width: Float, height: Float) {
        setBounds(position.cpy().add(Vector2(x,y).scaled), Vector2(width, height).scaled)
        _actorList.add(this)
    }

    protected fun drawJoint(
        alpha: Float,
        bodyA: AbstractBody,
        bodyB: AbstractBody,
        anchorA: Vector2,
        anchorB: Vector2,
    ) {
        screenBox2d.drawerUtil.drawer.line(
            tmpPositionA.set(bodyA.body?.position).add(tmpAnchorA.set(anchorA).subCenter(bodyA.body!!)).scaledToUI,
            tmpPositionB.set(bodyB.body?.position).add(tmpAnchorB.set(anchorB).subCenter(bodyB.body!!)).scaledToUI,
            colorJoint.apply { a = alpha }, 1f
        )
    }

    fun setNoneId() {
        bodyList.onEach { it.setNoneId() }
    }

    fun setOriginalId() {
        bodyList.onEach { it.setOriginalId() }
    }

}