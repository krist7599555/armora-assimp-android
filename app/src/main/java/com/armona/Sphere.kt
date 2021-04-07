package com.armona

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


/**
 * Created by amil101 on 6/02/16.
 */
class Sphere(var m_Stacks: Int, var m_Slices: Int, var m_Radius: Float, var m_Squash: Float) {
    private val vertexShaderCode = "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +  // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}"
    private val fragmentShaderCode = "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"
    private lateinit var vertexBuffer: FloatBuffer

    // Set color with red, green, blue and alpha (opacity) values
    var color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)
    private val mProgram: Int
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private var mMVPMatrixHandle = 0
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    fun draw(mvpMatrix: FloatArray?, use_program: Boolean) {

        // Add program to OpenGL ES environment
        if (use_program) {
            GLES20.glUseProgram(mProgram)
        }

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer
        )

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor")

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0)

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, (m_Slices + 1) * 2 * (m_Stacks - 1) + 2)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    var m_Scale = 0f
    private fun init(stacks: Int, slices: Int, radius: Float, squash: Float, textureFile: String) {
        var vertexData: FloatArray
        var colorData: FloatArray
        var colorIncrement = 0f
        var blue = 0f
        var red = 1.0f
        var vIndex = 0 //vertex index
        var cIndex = 0 //color index
        m_Scale = radius
        m_Squash = squash
        colorIncrement = 1.0f / stacks.toFloat()
        run {
            m_Stacks = stacks
            m_Slices = slices

            //vertices
            vertexData = FloatArray(3 * ((m_Slices * 2 + 2) * m_Stacks))

            //color data
            colorData = FloatArray(4 * (m_Slices * 2 + 2) * m_Stacks)
            var phiIdx: Int
            var thetaIdx: Int

            //latitude
            phiIdx = 0
            while (phiIdx < m_Stacks) {

                //starts at -90 degrees (-1.57 radians) goes up to +90 degrees (or +1.57 radians)

                //the first circle
                val phi0 =
                    Math.PI.toFloat() * ((phiIdx + 0).toFloat() * (1.0f / m_Stacks.toFloat()) - 0.5f)

                //the next, or second one.
                val phi1 =
                    Math.PI.toFloat() * ((phiIdx + 1).toFloat() * (1.0f / m_Stacks.toFloat()) - 0.5f)
                val cosPhi0 = Math.cos(phi0.toDouble()).toFloat()
                val sinPhi0 = Math.sin(phi0.toDouble()).toFloat()
                val cosPhi1 = Math.cos(phi1.toDouble()).toFloat()
                val sinPhi1 = Math.sin(phi1.toDouble()).toFloat()
                var cosTheta: Float
                var sinTheta: Float

                //longitude
                thetaIdx = 0
                while (thetaIdx < m_Slices) {

                    //increment along the longitude circle each "slice"
                    val theta =
                        (-2.0f * Math.PI.toFloat() * thetaIdx.toFloat() * (1.0 / (m_Slices - 1).toFloat())).toFloat()
                    cosTheta = Math.cos(theta.toDouble()).toFloat()
                    sinTheta = Math.sin(theta.toDouble()).toFloat()

                    //we're generating a vertical pair of points, such
                    //as the first point of stack 0 and the first point of stack 1
                    //above it. This is how TRIANGLE_STRIPS work,
                    //taking a set of 4 vertices and essentially drawing two triangles
                    //at a time. The first is v0-v1-v2 and the next is v2-v1-v3. Etc.

                    //get x-y-z for the first vertex of stack
                    vertexData[vIndex + 0] = m_Scale * cosPhi0 * cosTheta
                    vertexData[vIndex + 1] = m_Scale * (sinPhi0 * m_Squash)
                    vertexData[vIndex + 2] = m_Scale * (cosPhi0 * sinTheta)
                    vertexData[vIndex + 3] = m_Scale * cosPhi1 * cosTheta
                    vertexData[vIndex + 4] = m_Scale * (sinPhi1 * m_Squash)
                    vertexData[vIndex + 5] = m_Scale * (cosPhi1 * sinTheta)
                    colorData[cIndex + 0] = red
                    colorData[cIndex + 1] = 0f
                    colorData[cIndex + 2] = blue
                    colorData[cIndex + 4] = red
                    colorData[cIndex + 5] = 0f
                    colorData[cIndex + 6] = blue
                    colorData[cIndex + 3] = 1.0.toFloat()
                    colorData[cIndex + 7] = 1.0.toFloat()
                    cIndex += 2 * 4
                    vIndex += 2 * 3
                    thetaIdx++
                }
                blue += colorIncrement
                red -= colorIncrement

                // create a degenerate triangle to connect stacks and maintain winding order
                vertexData[vIndex + 3] = vertexData[vIndex - 3]
                vertexData[vIndex + 0] = vertexData[vIndex + 3]
                vertexData[vIndex + 4] = vertexData[vIndex - 2]
                vertexData[vIndex + 1] = vertexData[vIndex + 4]
                vertexData[vIndex + 5] = vertexData[vIndex - 1]
                vertexData[vIndex + 2] = vertexData[vIndex + 5]
                phiIdx++
            }
        }
        makeFloatBuffer(vertexData)
    }

    fun makeFloatBuffer(arr: FloatArray) {
        val bb = ByteBuffer.allocateDirect(arr.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(arr)
        vertexBuffer.position(0)
    }

    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3
        fun loadShader(type: Int, shaderCode: String?): Int {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            val shader = GLES20.glCreateShader(type)

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }
    }

    init {
        init(m_Stacks, m_Slices, m_Radius, m_Squash, "dummy")
        val vertexShader = MyGLRenderer.loadShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )
        val fragmentShader = MyGLRenderer.loadShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram()

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader)

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader)

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram)
    }
}