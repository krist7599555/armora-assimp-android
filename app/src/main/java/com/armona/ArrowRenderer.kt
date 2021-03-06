package com.armona

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ArrowRenderer : GLSurfaceView.Renderer {
    lateinit var mSphere: Sphere

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private val mMVPMatrix = FloatArray(16)

    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mModelMatrix = FloatArray(16)
    private val mTmpMatrix = FloatArray(16)
    private val mAngle = 0f
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.2f, 0.0f, 0.0f, 1.0f)
        mSphere = Sphere(10, 10, 0.5f, 1.0f)
    }

    override fun onDrawFrame(unused: GL10) {
//        val scratch = FloatArray(16)

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 0.3f, 0.2f, 0f);
        print(mModelMatrix.toString())
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(mTmpMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mTmpMatrix, 0)

        //Draw Sphere
        mSphere.color = floatArrayOf(0.93671875f, 0.56953125f, 0.22265625f, 1.0f)
        mSphere.draw(mMVPMatrix, true)

        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, -0.3f, -0.2f, 0.9f);
        print(mModelMatrix.toString())
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
        Matrix.multiplyMM(mTmpMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mTmpMatrix, 0)

        //Draw Sphere
        mSphere.color = floatArrayOf(0.23671875f, 0.56953125f, 0.82265625f, 1.0f)
        mSphere.draw(mMVPMatrix, false)

//        Matrix.translateM(mViewMatrix, 0, 0f, 0f, 0f);
//        Matrix.setLookAtM(mViewMatrix, 0, 10f, 10f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
//        mSphere.color = floatArrayOf(0.23671875f, 0.56953125f, 0.82265625f, 1.0f)
//        mSphere.draw(mMVPMatrix)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {

        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
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