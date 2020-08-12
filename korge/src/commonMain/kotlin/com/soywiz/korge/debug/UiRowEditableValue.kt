package com.soywiz.korge.debug

import com.soywiz.korui.*
import com.soywiz.korui.layout.*

class UiRowEditableValue(app: UiApplication, val labelText: String, val editor: UiEditableValue<*>) : UiContainer(app) {
    val leftPadding = UiLabel(app)
    val label = UiLabel(app).apply {
        text = labelText
        preferredWidth = 50.percent
    }
    init {
        layout = HorizontalUiLayout
        leftPadding.preferredSize(16.pt, 16.pt)
        label.preferredSize(50.percent - 16.pt, 16.pt)
        editor.preferredSize(50.percent, 16.pt)
        //backgroundColor = Colors.RED
        addChild(leftPadding)
        addChild(label)
        addChild(editor)
        label.onClick {
            editor.hideEditor()
        }
        //addChild(UiLabel(app).also { it.text = "text" }.also { it.bounds = RectangleInt(120, 0, 120, 32) })
    }
}
