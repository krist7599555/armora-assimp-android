package com.armona.book

import android.opengl.GLES20
import android.opengl.GLES20.*

class ShaderHelper {
    companion object {
        // util shader
        private fun validateShader(id: Int, cb: () -> Unit) {
            val compileStatus: IntArray = IntArray(1)
            GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == GLES20.GL_FALSE) {
                cb()
                throw Exception("compile shader error")
            }
        }
        private fun compileShader(source: String, type: Int): Int {
            val id = GLES20.glCreateShader(type)
            GLES20.glShaderSource(id, source)
            GLES20.glCompileShader(id)
            validateShader(id) {
                println("shader source: $source")
            }
            return id;
        }

        private fun validateProgramLink(id: Int) {
            val linkStatus: IntArray = IntArray(1);
            glGetProgramiv(id, GL_LINK_STATUS, linkStatus, 0)
            if (linkStatus[0] == GL_FALSE) {
                throw Exception("Linking of program failed.");
            }
        }
        fun createGlProgram(vsSource: String, fsSource: String): Int {
            val id = glCreateProgram();
            glAttachShader(id, compileShader(vsSource, GL_VERTEX_SHADER));
            glAttachShader(id, compileShader(fsSource, GL_FRAGMENT_SHADER));
            glLinkProgram(id);
            validateProgramLink(id);
            glUseProgram(id)
            return id
        }
    }
}