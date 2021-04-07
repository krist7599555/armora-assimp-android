package com.armona

import android.opengl.GLES20.*

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer: GLSurfaceView.Renderer {
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        TODO("Not yet implemented")
        glClearColor(1f, 0f, 0f, 1f);
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
//        TODO("Not yet implemented")
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT);
//        TODO("Not yet implemented")
    }

}
