package com.armona.book

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates


var tableVerticesWithTriangles = floatArrayOf(
    // Order of coordinates: X, Y, R, G, B
    // Triangle Fan
    0f, 0f, 1f, 1f, 1f,
    -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
    0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
    0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
    -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
    -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
// Line 1
    -0.5f, 0f, 1f, 0f, 0f, 0.5f, 0f, 1f, 0f, 0f,
// Mallets
    0f, -0.25f, 0f, 0f, 1f,
    0f, 0.25f, 1f, 0f, 0f
)
val BYTES_PER_FLOAT = Float.SIZE_BYTES
val POSITION_COMPONENT_COUNT = 2 // we use as vec2(x, y)
val COLOR_COMPONENT_COUNT = 3 // we use as vec2(x, y)
val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

private val vertexData = ByteBuffer
    .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .apply { put(tableVerticesWithTriangles) }

private val VERTEX_SHADER_TEXT = """
    uniform mat4 u_Matrix;
    attribute vec4 a_Position;
    attribute vec4 a_Color;
    varying vec4 v_Color;
    
    void main() {
        v_Color = a_Color;
        gl_Position = u_Matrix * a_Position;
//        gl_PointSize = 10.0; 
    }
"""

private val FRAGMENT_SHADER_TEXT = """
//    precision mediump float;
//     
//    uniform vec4 v_Color;
//    void main() {
//        gl_FragColor = v_Color;
////        gl_PointSize = 10.0; // this cause error http://shdr.bkcore.com
//    }
    precision mediump float; 
    varying vec4 v_Color;
    void main() {
        gl_FragColor = v_Color;
    }
"""

//fun readTextFileFromResource(context: Context, resourceId: Int): String? {
//    val body = StringBuilder()
//    try {
//        val inputStream: InputStream = context.resources.openRawResource(resourceId)
////        inputStream.readBytes().toString()
//        val inputStreamReader = InputStreamReader(inputStream)
//        val bufferedReader = BufferedReader(inputStreamReader)
//        var nextLine: String?
//        while (bufferedReader.readLine().also { nextLine = it } != null) {
//            body.append(nextLine)
//            body.append('\n')
//        }
//    } catch (e: IOException) {
//        throw RuntimeException(
//            "Could not open resource: $resourceId", e
//        )
//    } catch (nfe: Resources.NotFoundException) {
//        throw RuntimeException("Resource not found: $resourceId", nfe)
//    }
//    return body.toString()
//}

class BookRenderer : GLSurfaceView.Renderer {

    private var program by Delegates.notNull<Int>()
    private var aPositionLocation by Delegates.notNull<Int>()
    private var aColorLocation by Delegates.notNull<Int>()
    private var uColorLocation by Delegates.notNull<Int>()
    private var uMatrixLocation by Delegates.notNull<Int>()
    private val projectionMatrix = FloatArray(16)

    private fun setup() {
        program = createGlProgram(VERTEX_SHADER_TEXT, FRAGMENT_SHADER_TEXT)
        aPositionLocation = glGetAttribLocation(program, "a_Position");
        aColorLocation = glGetAttribLocation(program, "a_Color");
        uMatrixLocation = glGetUniformLocation(program, "u_Matrix");
//        uColorLocation = glGetUniformLocation(program, "u_Color")
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onSurfaceCreated(_gl: GL10?, config: EGLConfig?) {
        setup()
        glClearColor(.0f, .0f, .0f, .0f)

        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        );
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GL_FLOAT,
            false,
            STRIDE,
            vertexData
        );
        glEnableVertexAttribArray(aColorLocation);
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onSurfaceChanged(_gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height);
        // camera
        when(width > height) {
            true -> {
                val aspectRatio = width.toFloat() / height.toFloat()
                Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
            }
            false -> {
                val aspectRatio = height.toFloat() / width.toFloat()
                Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
            }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onDrawFrame(_gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT);

        // set uniform
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

        // draw
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6); // count: 6 = 2 triangle
        glDrawArrays(GL_LINES, 6, 2);
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
    }

    companion object {

        // util shader
        fun validateShader(id: Int, cb: () -> Unit) {
            val compileStatus: IntArray = IntArray(1)
            glGetShaderiv(id, GL_COMPILE_STATUS, compileStatus, 0)
            if (compileStatus[0] == GL_FALSE) {
                cb()
                throw Exception("compile shader error")
            }
        }
        fun compileShader(source: String, type: Int): Int {
            val id = glCreateShader(type)
            glShaderSource(id, source)
            glCompileShader(id)
            validateShader(id) {
                println("shader source: $source")
            }
            return id;
        }

        // util program
        fun validateProgramLink(id: Int) {
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
