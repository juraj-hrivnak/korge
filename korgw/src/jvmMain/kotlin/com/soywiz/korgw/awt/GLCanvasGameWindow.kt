package com.soywiz.korgw.awt

import com.soywiz.korag.gl.*
import com.soywiz.korgw.GameWindowCreationConfig
import com.soywiz.korgw.platform.*
import java.awt.*

open class GLCanvasGameWindow(val canvas: GLCanvas, config: GameWindowCreationConfig = GameWindowCreationConfig()) : BaseAwtGameWindow(config) {
    init {
        exitProcessOnExit = false
        canvas.defaultRenderer = { gl, g ->
            framePaint(g)
        }
    }

    override val ctx: BaseOpenglContext get() = canvas.ctx!!
    override val ag: AGOpengl get() = canvas.ag
    override val component: Component get() = canvas
    override val contentComponent: Component get() = canvas
}
