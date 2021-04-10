package com.armona.book

import android.opengl.Matrix
import kotlin.math.tan

class MatrixHelper {
    companion object {
        fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, n: Float, f: Float) {
            val angleInRadians = (yFovInDegrees * Math.PI / 180.0).toFloat()
            val a = (1.0 / tan(angleInRadians / 2.0)).toFloat()
            m[0] = a / aspect;
            m[1] = 0f;
            m[2] = 0f;
            m[3] = 0f;
            m[4] = 0f;
            m[5] = a;
            m[6] = 0f;
            m[7] = 0f;
            m[8] = 0f;
            m[9] = 0f;
            m[10] = -((f + n) / (f - n)); m[11] = -1f;
            m[12] = 0f;
            m[13] = 0f;
            m[14] = -((2f * f * n) / (f - n)); m[15] = 0f;
            // OpenGL stores matrix data in column-major order
        }
        fun orthoM(m: FloatArray, mOffset: Int, width: Int, height: Int) {
            when(width > height) {
                true -> {
                    val aspectRatio = width.toFloat() / height.toFloat()
                    Matrix.orthoM(m, mOffset, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
                }
                false -> {
                    val aspectRatio = height.toFloat() / width.toFloat()
                    Matrix.orthoM(m, mOffset, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
                }
            }
        }

    }
}