package korlibs.korge.view

import korlibs.korge.render.RenderContext
import korlibs.korge.render.useLineBatcher
import korlibs.image.color.Colors
import korlibs.image.color.RGBA
import korlibs.math.geom.*

inline fun Container.line(a: Point, b: Point, color: RGBA = Colors.WHITE, callback: @ViewDslMarker Line.() -> Unit = {})
    = Line(a, b, color).addTo(this, callback)
inline fun Container.line(x0: Double, y0: Double, x1: Double, y1: Double, color: RGBA = Colors.WHITE, callback: @ViewDslMarker Line.() -> Unit = {})
    = Line(Point(x0, y0), Point(x1, y1), color).addTo(this, callback)

class Line(
    p1: Point,
    var p2: Point,
    color: RGBA = Colors.WHITE,
) : View() {
    var p1: Point by ::pos
    var x1: Float get() = x ; set(value) { x = value }
    var y1: Float get() = y ; set(value) { y = value }
    var x2: Float get() = p2.x ; set(value) { p2 = p2.copy(x = value) }
    var y2: Float get() = p2.y ; set(value) { p2 = p2.copy(y = value) }

    init {
        pos = p1
        colorMul = color
    }

    fun setPoints(a: Point, b: Point) {
        this.p1 = a
        this.p2 = b
    }

    override fun renderInternal(ctx: RenderContext) {
        ctx.useLineBatcher { lines ->
            lines.drawWithGlobalMatrix(globalMatrix) {
                val col = renderColorMul
                lines.line(0f, 0f, x2 - x1, y2 - y1, col, col)
            }
        }
    }
}
