package com.roshevasternin.rozval.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.roshevasternin.rozval.game.GDXGame
import com.roshevasternin.rozval.game.actors.ATmpGroup
import com.roshevasternin.rozval.game.actors.button.AButton
import com.roshevasternin.rozval.game.actors.progress.AProgressDefault
import com.roshevasternin.rozval.game.actors.shader.ABlurGroup
import com.roshevasternin.rozval.game.actors.shader.AMaskGroup
import com.roshevasternin.rozval.game.actors.shader.ATestShaderGroup
import com.roshevasternin.rozval.game.utils.*
import com.roshevasternin.rozval.game.utils.actor.animHide
import com.roshevasternin.rozval.game.utils.advanced.AdvancedScreen
import com.roshevasternin.rozval.game.utils.advanced.AdvancedStage
import com.roshevasternin.rozval.game.utils.font.FontParameter
import com.roshevasternin.rozval.util.log
import kotlinx.coroutines.launch

class TestShaderScreen(override val game: GDXGame): AdvancedScreen() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font60    = fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setSize(60))

    private val progress = AProgressDefault(this)
    private val lblFPS   = Label("", LabelStyle(font60, Color.BLACK))

    override fun show() {
        setBackgrounds(drawerUtil.getRegion(Color.GRAY), game.assetsAll.LVL_1.region)
        super.show()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        coroutine?.launch {
            runGDX {
                addActor(progress)
                progress.setBounds(420f, 946f, 1080f, 80f)

                addActor(lblFPS)
                lblFPS.apply {
                    setBounds(29f, 960f, 278f, 79f)
                    setAlignment(Align.center)
                }

                val img1 = Image(game.assetsLoader.builderList.first())
                addActor(img1)
                img1.debug()
                img1.setBounds(57f, 179f, 200f, 315f)


                val tmpG = ATmpGroup(this@TestShaderScreen)
                addAndFillActor(tmpG)
                val btn = AButton(this@TestShaderScreen, AButton.Type.Exit)
                tmpG.addAndFillActor(btn)
                //btn.setSize(200f, 200f)

                val test = AMaskGroup(this@TestShaderScreen)
                addActor(test)
                test.debug()
                test.setBounds(862f, 442f, 195f, 195f)
                test.setOrigin(Align.center)



                coroutine?.launch {
                    progress.progressPercentFlow.collect {
                        //test.radiusBlur = it / 10f
                    }
                }

                val img3 = Image(game.assetsLoader.builderList[2])
                addActor(img3)
                img3.debug()
                img3.setBounds(635f, 480f, 200f, 315f)
                img3.color.a = 0.5f

            }

        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        lblFPS.setText("FPS: " + Gdx.graphics.framesPerSecond)
    }

    override fun hideScreen(block: Block) {
        coroutine?.launch {
            runGDX {
                stageBack.root.animHide(TIME_ANIM_SCREEN_ALPHA) { block.invoke() }
            }
        }
    }

}