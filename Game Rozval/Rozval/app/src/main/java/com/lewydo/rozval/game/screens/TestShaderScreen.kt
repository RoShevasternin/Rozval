package com.lewydo.rozval.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.utils.Align
import com.lewydo.rozval.game.GDXGame
import com.lewydo.rozval.game.actors.progress.AProgressDefault
import com.lewydo.rozval.game.actors.shader.ATestShader
import com.lewydo.rozval.game.utils.*
import com.lewydo.rozval.game.utils.actor.animHide
import com.lewydo.rozval.game.utils.advanced.AdvancedScreen
import com.lewydo.rozval.game.utils.advanced.AdvancedStage
import com.lewydo.rozval.game.utils.font.FontParameter
import kotlinx.coroutines.launch

class TestShaderScreen(override val game: GDXGame): AdvancedScreen() {

    private val parameter = FontParameter().setCharacters(FontParameter.CharType.ALL)
    private val font60    = fontGenerator_LondrinaSolid_Regular.generateFont(parameter.setSize(60))

    private val progress = AProgressDefault(this)
    private val lblFPS   = Label("", LabelStyle(font60, Color.BLACK))

    override fun show() {
        setBackBackground(drawerUtil.getRegion(Color.GRAY))
        //setUIBackground(game.assetsAll.LVL_1.region)
        super.show()
    }

    override fun AdvancedStage.addActorsOnStageUI() {
        addActor(progress)
        progress.setBounds(420f, 946f, 1080f, 80f)

        addActor(lblFPS)
        lblFPS.apply {
            setBounds(29f, 960f, 278f, 79f)
            setAlignment(Align.center)
        }

        val person1 = Image(game.assetsLoader.builderList[1])
        person1.debug()
        person1.setBounds(57f, 179f, 200f, 315f)

        val person2 = Image(game.assetsLoader.builderList[2])
        person2.debug()
        person2.setBounds(57f, 566f, 200f, 315f)



        val test = ATestShader(this@TestShaderScreen)
        test.debug()
        //test.setBounds(635f, 480f, 200f, 315f)

        val test2 = ATestShader(this@TestShaderScreen)
        test2.debug()
        test2.setBounds(635f, 480f, 200f, 315f)

        addActors(person1, /*test,*/ person2, test2)

        person1.color.a = 1f
        person2.color.a = 1f

test2.addActor(test)


        val p1 = Image(game.assetsLoader.builderList[1])
        p1.debug()
        p1.setSize(200f, 315f)

        val p2 = Image(game.assetsLoader.builderList[2])
        p2.debug()
        p2.setSize(200f, 315f)

        test.addActor(p1)
        test2.addActor(p2)




        coroutine?.launch {
            progress.progressPercentFlow.collect {
                p1.x = it * 3
                p2.x = -it * 3
            }
        }
    }

    override fun render(delta: Float) {
        super.render(delta)
        lblFPS.setText("FPS: " + Gdx.graphics.framesPerSecond)
    }

    override fun hideScreen(block: Block) {
                stageBack.root.animHide(TIME_ANIM_SCREEN) { block.invoke() }
    }

}