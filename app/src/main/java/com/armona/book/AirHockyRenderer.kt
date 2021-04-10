package com.armona.book

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.armona.R
import com.armona.geometry.Puck
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class AirHockyRenderer(val context: Context): GLSurfaceView.Renderer {

    val projectionMatrix = FloatArray(16)
    val modelMatrix = FloatArray(16)
    val viewMatrix = FloatArray(16)
    val viewProjectionMatrix = FloatArray(16)

    val table = Table()
    val mallet = Mallet(0.08f, 0.15f, 32);
    val puck = Puck(0.06f, 0.02f, 32);

    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram

    private var texture = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(
            0.0f,
            0.9f,
            0.0f,
            0.0f
        )
        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height);
        // proj
//        MatrixHelper.orthoM(projectionMatrix, 0, width, height);// choice 2
        MatrixHelper.perspectiveM(
            projectionMatrix,
            45f,
            width.toFloat() / height.toFloat(),
            1f,
            10f
        )
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);

        // model
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size);
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

//        positionTableInScene


        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureProgram);
        table.draw();
        // Draw the mallets.
//        colorProgram.useProgram();
//        colorProgram.setUniforms(projectionMatrix, 1f, 0f, 1f);
//        mallet.bindData(colorProgram);
//        mallet.draw();
    }
}