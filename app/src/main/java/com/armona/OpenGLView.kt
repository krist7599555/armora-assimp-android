package com.armona

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES10
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import gli.buffer.toByteBuffer

fun loadTexture(bitmap: Bitmap): Int {
    val textureHandle = IntArray(1)
    GLES20.glGenTextures(1, textureHandle, 0)
    if (textureHandle[0] == 0) {
        throw Exception("can not glGenTexture")
    }
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

    // Set filtering
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
    // Load the bitmap into the bound texture.
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    return textureHandle[0]
    // Recycle the bitmap, since its data has been loaded into OpenGL.
//    bitmap.recycle();
}

class OpenGLView: GLSurfaceView {
    constructor(context: Context) : super(context) {
        println("\n\n\n>>>>>>>HERRE <<<<\n\n\n")
        setEGLContextClientVersion(2);
//        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        preserveEGLContextOnPause = true

        val inp = context.assets.open("images/screenshot.png")
        val c = inp
//        val pic = Picture.createFromStream(inp)
//        val b = inp.readBytes()
        val bytes = inp.readBytes()
        val bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//        val buffer = ByteBuffer.allocate(bm.allocationByteCount)
//        bm.copyPixelsToBuffer(buffer);
//        GLES20.glReadPixels(
//            0, 0, bm.width, bm.height, GLES20.GL_RGB,
//            GLES20.GL_UNSIGNED_BYTE, buffer
//        );

        GLES10.glGenTextures(bytes.size, bytes.toByteBuffer().asIntBuffer())

//        _filename = "model/arrow/Arrow5.obj"
//        val path = _filename
//        val op = context.assets.open(path)
//        val txt = op.bufferedReader().use(BufferedReader::readText)
//
//        val d = Importer().readFile(context.assets, path);
//        val e = d;
//        println("load model success")

        setRenderer(MyGLRenderer())

        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
    }
}