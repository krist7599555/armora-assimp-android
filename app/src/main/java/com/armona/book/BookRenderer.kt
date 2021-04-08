package com.armona.book

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.properties.Delegates

var tableVerticesWithTriangles = floatArrayOf(
    // Triangle 1
    -0.5f, -0.5f,
    0.5f, 0.5f,
    -0.5f, 0.5f,
    // Triangle 2
    -0.5f, -0.5f,
    0.5f, -0.5f,
    0.5f, 0.5f,
    // Line 1
    -0.5f, 0f,
    0.5f, 0f,
    // Mallets
    0f, -0.25f, // P1
    0f, 0.25f // P2
)
val POSITION_COMPONENT_COUNT = 2 // we use as vec2(x, y)

private const val BYTES_PER_FLOAT = Float.SIZE_BYTES
private val vertexData = ByteBuffer
    .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
    .order(ByteOrder.nativeOrder())
    .asFloatBuffer()
    .apply { put(tableVerticesWithTriangles) }

private val VERTEX_SHADER_TEXT = """
    attribute vec4 a_Position;
    void main() {
        gl_Position = a_Position;
    }
"""

private val FRAGMENT_SHADER_TEXT = """
    precision mediump float; 
    uniform vec4 u_Color;
    void main() {
        gl_FragColor = u_Color;
//        gl_PointSize = 10.0; // this cause error http://shdr.bkcore.com
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
    private var uColorLocation by Delegates.notNull<Int>()

    private fun setup() {
        program = createGlProgram(VERTEX_SHADER_TEXT, FRAGMENT_SHADER_TEXT)
        aPositionLocation = glGetAttribLocation(program, "a_Position");
        uColorLocation = glGetUniformLocation(program, "u_Color")
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onSurfaceCreated(_gl: GL10?, config: EGLConfig?) {
        setup()
        glClearColor(.0f, .0f, .0f, .0f)

        vertexData.position(0)
        glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
            false, 0, vertexData
        );
        glEnableVertexAttribArray(aPositionLocation);
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onSurfaceChanged(_gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height);
    }

    @Suppress("UNUSED_PARAMETER")
    override fun onDrawFrame(_gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT);

        // draw rect
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6); // count: 6 = 2 triangle

        // draw line
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet blue.
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);
// Draw the second mallet red.
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }

    // util
    fun validateShader(id: Int) {
        val compileStatus: IntArray = IntArray(1)
        glGetShaderiv(id, GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == GL_FALSE) {
            throw Exception("compile shader error")
        }
    }
    fun compileShader(source: String, type: Int): Int {
        val id = glCreateShader(type)
        glShaderSource(id, source)
        glCompileShader(id)
        validateShader(id)
        return id;
    }

    fun validateProgramCreate(id: Int) {
//        glValidateProgram(id);
        val validateStatus = IntArray(1) { 0 }
        glGetProgramiv(id, GL_VALIDATE_STATUS, validateStatus, 0);
        println("Results of validating program: " + validateStatus[0])
        println("LogsInfo: ${glGetProgramInfoLog(id)}")
        if (validateStatus[0] == GL_FALSE) {
            throw Exception("create program fail")
        }
    }
    fun validateProgramLink(id: Int) {
        val linkStatus: IntArray = IntArray(1);
        glGetProgramiv(id, GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == GL_FALSE) {
            throw Exception("Linking of program failed.");
        }
    }
    fun createGlProgram(vsSource: String, fsSource: String): Int {
        val id = glCreateProgram();
//        validateProgramCreate(id);
        glAttachShader(id, compileShader(vsSource, GL_VERTEX_SHADER));
        glAttachShader(id, compileShader(fsSource, GL_FRAGMENT_SHADER));
        glLinkProgram(id);
        validateProgramLink(id);
        glUseProgram(id)
        return id
    }
}
