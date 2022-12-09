package com.soywiz.korag.gl

import com.soywiz.kds.*
import com.soywiz.kds.lock.*
import com.soywiz.kgl.*
import com.soywiz.kmem.unit.*
import com.soywiz.korag.*
import com.soywiz.korag.AGNativeObject

class GLGlobalState(val gl: KmlGl, val ag: AG) {
    internal val buffers = mutableSetOf<GLBuffer>()
    internal val objectsToDeleteLock = Lock()
    internal val objectsToDelete = fastArrayListOf<GLBaseObject>()

    fun readStats(out: AGStats) {
        out.buffersCount = buffers.size
        out.buffersMemory = ByteUnits.fromBytes(buffers.sumOf { it.estimatedBytes })
    }
}

internal class GLBaseProgram(globalState: GLGlobalState, val programInfo: GLProgramInfo) : GLBaseObject(globalState) {
    override fun delete() {
        programInfo.delete(gl)
    }
    fun use() {
        programInfo.use(gl)
    }
}

internal open class GLBaseObject(val globalState: GLGlobalState) : AGNativeObject {
    val gl: KmlGl = globalState.gl

    open fun delete() {
    }

    final override fun markToDelete() {
        globalState.objectsToDeleteLock { globalState.objectsToDelete += this }
    }
}

internal fun AGBuffer.gl(state: GLGlobalState): GLBuffer = this.createOnce(state) { GLBuffer(state) }
internal class GLBuffer(state: GLGlobalState) : GLBaseObject(state) {
    var id = gl.genBuffer()
    var estimatedBytes: Int = 0
    init {
        globalState.buffers += this
    }
    override fun delete() {
        globalState.buffers -= this
        gl.deleteBuffer(id)
        id = -1
    }
}

internal fun AGFrameBufferBase.gl(state: GLGlobalState): GLFrameBuffer = this.createOnce(state) { GLFrameBuffer(state, this) }
internal class GLFrameBuffer(state: GLGlobalState, val ag: AGFrameBufferBase) : GLBaseObject(state) {
    var renderBufferId = gl.genRenderbuffer()
    var frameBufferId = gl.genFramebuffer()
    var info: AGFrameBufferInfo = AGFrameBufferInfo.INVALID

    override fun delete() {
        gl.deleteRenderbuffer(renderBufferId)
        gl.deleteFramebuffer(frameBufferId)
        renderBufferId = -1
        frameBufferId = -1
    }
}

internal fun AGTexture.gl(state: GLGlobalState): GLTexture = this.createOnce(state) { GLTexture(state) }
internal class GLTexture(state: GLGlobalState) : GLBaseObject(state) {
    var id = gl.genTexture()
    var cachedContentVersion: Int = -2

    override fun delete() {
        gl.deleteTexture(id)
        id = -1
    }
}

////////////////

internal fun <T : AGObject, R: AGNativeObject> T.createOnce(state: GLGlobalState, block: (T) -> R): R {
    if (this._native == null || this._cachedContextVersion != state.ag.contextVersion) {
        this._cachedContextVersion = state.ag.contextVersion
        this._native = block(this)
    }
    return this._native as R
}

