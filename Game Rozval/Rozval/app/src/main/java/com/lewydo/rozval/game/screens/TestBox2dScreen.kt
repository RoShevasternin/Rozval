package com.lewydo.rozval.game.screens

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.lewydo.rozval.game.GDXGame
import com.lewydo.rozval.game.actors.progress.AProgressDefault
import com.lewydo.rozval.game.box2d.bodies.standart.BDynamic
import com.lewydo.rozval.game.box2d.bodies.test.BRect
import com.lewydo.rozval.game.utils.Block
import com.lewydo.rozval.game.utils.TIME_ANIM_SCREEN
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.advanced.AdvancedBox2dUserScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedGroup
import com.lewydo.rozval.game.utils.advanced.AdvancedStage
import com.lewydo.rozval.game.utils.gdxGame
import com.lewydo.rozval.game.utils.region
import com.lewydo.rozval.game.utils.runGDX
import kotlinx.coroutines.launch

class TestBox2dScreen(): AdvancedBox2dUserScreen() {

    private val bDynamic = BDynamic(this)
    private val bRect    = BRect(this)

    private val progress = AProgressDefault(this)

    override fun show() {
        setBackBackground(gdxGame.assetsAll.LVL_1.region)
        super.show()
    }

    override fun AdvancedStage.addActorsOnStageBox2d() {
        coroutine?.launch {
            runGDX {
                //createB_Dynamic()
                //createB_Rect()
            }

//            panelMenu.animShowSuspend(TIME_ANIM_SCREEN_ALPHA)
        }
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        coroutine?.launch {
            runGDX {
                addActor(progress)
                progress.setBounds(420f, 946f, 1080f, 80f)

                val img0 = Image(gdxGame.assetsLoader.builderList.first())
                addActor(img0)
                img0.debug()
                img0.setBounds(57f, 179f, 200f, 315f)

//                val test = ATestShaderGroup(this@TestScreen)
//                addActor(test)
//                test.debug()
//                test.setBounds(859f, 304f, 200f, 315f)

                coroutine?.launch {
                    progress.progressPercentFlow.collect {
//                        test.blurRadius = it
                    }
                }

                val img = Image(gdxGame.assetsLoader.builderList[2])
                addActor(img)
                img.debug()
                img.setBounds(57f, 566f, 200f, 315f)
            }

//            panelMenu.animShowSuspend(TIME_ANIM_SCREEN_ALPHA)
        }
    }

    override fun hideScreen(block: Block) {
        coroutine?.launch {
            runGDX {
                stageBack.root.animHide(TIME_ANIM_SCREEN) { block.invoke() }
            }
        }
    }

    // Actors ------------------------------------------------------------------------

    private fun AdvancedGroup.addPanel() {

    }

    // Body ------------------------------------------------------------------------

    /*private fun createB_Dynamic() {
        bDynamic.apply {
            id = BodyId.DYNAMIC
            collisionList.addAll(arrayOf(BodyId.RECT))
        }
        bDynamic.create(339f, 473f, 135f, 135f)
    }

    private fun createB_Rect() {
        bRect.apply {
            id = BodyId.RECT
            collisionList.addAll(arrayOf(BodyId.DYNAMIC))
        }
        bRect.create(849f, 428f, 223f, 223f)

        bRect.beginContactBlockArray.add(AbstractBody.ContactBlock { body, contact ->
            when(body.id) {
                BodyId.DYNAMIC -> {
                    runGDX {
                        log("""
                            normal: ${contact.worldManifold.normal},
                            points: ${contact.worldManifold.points.joinToString()},
                            separations: ${contact.worldManifold.separations.joinToString()},
                            numberOfContactPoints: ${contact.worldManifold.numberOfContactPoints},
                            """
                        )
                    }
                }
            }
        })
    }*/

}