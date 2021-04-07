package com.armona

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private fun loadShader(type: Int, shaderCode: String?): Int {
    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
    val shader = glCreateShader(type)

    // add the source code to the shader and compile it
    glShaderSource(shader, shaderCode)
    glCompileShader(shader)
    return shader
}

private class Program {
    val vertexShader = loadShader(GL_VERTEX_SHADER,"uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +  // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}")
    val fragmentShader = loadShader(GL_FRAGMENT_SHADER,"precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}")

    // relate to field
    val mProjectionMatrix = FloatArray(16).also { updateViewport(300, 400) }
    val mViewMatrix = FloatArray(16).also {
        Matrix.setLookAtM(it, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
    }

    val mProgram = glCreateProgram().also {
        glAttachShader(it, vertexShader)
        glAttachShader(it, fragmentShader)
        glLinkProgram(it)
    }

    fun useProgram() {
        glUseProgram(mProgram)
    }
    fun updateViewport(width: Int, height: Int) {
        glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }
    fun draw(model: Model) {
        val COORDS_PER_VERTEX = 3
        for (mesh in model.meshes) {
            // get handle to vertex shader's vPosition member
            val mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")
            // Enable a handle to the triangle vertices
            glEnableVertexAttribArray(mPositionHandle)
            // Prepare the triangle coordinate data
            glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GL_FLOAT, false, 0, mesh.mVertices().position())

            // get handle to fragment shader's vColor member
            val mColorHandle = glGetUniformLocation(mProgram, "vColor")
            // Set color for drawing the triangle
            val color = floatArrayOf(0.93671875f, 0.56953125f, 0.22265625f, 1.0f)
            glUniform4fv(mColorHandle, 1, color, 0)

            // get handle to shape's transformation matrix
            val mMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix")
            // Apply the projection and view transformation
            val mv = FloatArray(16)
            Matrix.multiplyMM(mv, 0, mViewMatrix, 0, model.mModelMatrix, 0)
            val mvp = FloatArray(16)
            Matrix.multiplyMM(mvp, 0, mProjectionMatrix, 0, mv, 0)

            glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvp, 0)

            // Draw the triangle
            glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, model.meshes[0].mNumVertices())

            // Disable vertex array
            glDisableVertexAttribArray(mPositionHandle)
        }
    }
}

class World: GLSurfaceView.Renderer {

    private val prog = Program()
    private val models = ArrayList<Model>();

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        glClearColor(0.2f, 0.0f, 0.0f, 1.0f)
        models.add(Model.fromPath("/models/arrow.obj"))
        println(models)
    }

    override fun onDrawFrame(unused: GL10) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        prog.useProgram()
        for (model in models) {
            prog.draw(model)
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        prog.updateViewport(width, height)
    }

    companion object {
        private const val TAG = "MyGLRenderer"

        /**
         * Utility method for compiling a OpenGL shader.
         *
         *
         * **Note:** When developing shaders, use the checkGlError()
         * method to debug shader coding errors.
         *
         * @param type - Vertex or fragment shader type.
         * @param shaderCode - String containing the shader code.
         * @return - Returns an id for the shader.
         */
        fun loadShader(type: Int, shaderCode: String?): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            val shader = GLES20.glCreateShader(type)

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }

        /**
         * Utility method for debugging OpenGL calls. Provide the name of the call
         * just after making it:
         *
         * <pre>
         * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
         * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
         *
         * If the operation is not successful, the check throws an error.
         *
         * @param glOperation - Name of the OpenGL call to check.
         */
        fun checkGlError(glOperation: String) {
            var error: Int
            while (GLES20.glGetError().also { error = it } != GLES20.GL_NO_ERROR) {
                Log.e(TAG, "$glOperation: glError $error")
                throw RuntimeException("$glOperation: glError $error")
            }
        }
    }
}