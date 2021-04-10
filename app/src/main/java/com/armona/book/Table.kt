package com.armona.book

import android.opengl.GLES20.GL_TRIANGLE_FAN
import android.opengl.GLES20.glDrawArrays

class Table {
    private val POSITION_COMPONENT_COUNT = 2
    private val TEXTURE_COORDINATES_COMPONENT_COUNT: Int = 2
    private val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT
    private val VERTEX_DATA = floatArrayOf(
        // Order of coordinates: X, Y, S, T
        // Triangle Fan
        0f, 0f, .5f, .5f,
        -.5f, -.8f, 0f, .9f,
        .5f, -.8f, 1f, .9f,
        .5f, .8f, 1f, .1f,
        -.5f, .8f, 0f, .1f,
        -.5f, -.8f, 0f, .9f
    )

    val vertexArray = VertexArray(VERTEX_DATA)
    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0, textureProgram.aPositionLocation, POSITION_COMPONENT_COUNT,
            STRIDE
        );
        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            textureProgram.aTextureCoordinatesLocation,
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        );
    }
    fun draw() {
        println("draw")
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}