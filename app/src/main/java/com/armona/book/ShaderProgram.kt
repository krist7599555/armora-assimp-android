package com.armona.book

import android.content.Context
import android.opengl.GLES20.*
import com.armona.R


open class ShaderProgram(vs: String, fs: String) {
    companion object {
        // Uniform constants
        const val U_COLOR = "u_Color"
        const val U_MATRIX = "u_Matrix";
        const val U_TEXTURE_UNIT = "u_TextureUnit";
        // Attribute constants
        const val A_POSITION = "a_Position";
        const val A_COLOR = "a_Color";
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    }

    protected val program = ShaderHelper.createGlProgram(vs, fs)
    fun useProgram() {
        glUseProgram(program);
    }
}

class TextureShaderProgram(context: Context): ShaderProgram(
    readTextFileFromResource(context, R.raw.texture_vertex_shader),
    readTextFileFromResource(context, R.raw.texture_fragment_shader),
) {
    val uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    val uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
    val aPositionLocation = glGetAttribLocation(program, A_POSITION);
    val aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId)
        // Tell the texture uniform sampler to use this texture in the shader by // telling it to read from texture unit 0. glUniform1i(uTextureUnitLocation, 0);
    }
}

class ColorShaderProgram(context: Context): ShaderProgram(
    readTextFileFromResource(context, R.raw.simple_vertex_shader),
    readTextFileFromResource(context, R.raw.simple_fragment_shader),
) {
    // Retrieve uniform locations for the shader program.
    val uColorLocation = glGetUniformLocation(program, U_COLOR);
    val uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
    // Retrieve attribute locations for the shader program.
    val aPositionLocation = glGetAttribLocation(program, A_POSITION);
    val aColorLocation = glGetAttribLocation(program, A_COLOR);

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1f);
    }
}