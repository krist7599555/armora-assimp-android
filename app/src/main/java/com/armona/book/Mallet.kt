package com.armona.book

import com.armona.geometry.ObjectBuilder
import com.armona.geometry.ObjectBuilder.Companion.DrawCommand
import com.armona.geometry.Point


class Mallet(val radius: Float, val height: Float, numPointsAroundMallet: Int) {
//    private val POSITION_COMPONENT_COUNT: Int = 2
//    private val COLOR_COMPONENT_COUNT: Int = 3
//    private val STRIDE = ((POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)
//                            * BYTES_PER_FLOAT)
//    private val VERTEX_DATA = floatArrayOf( // Order of coordinates: X, Y, R, G, B
//        0f, -0.4f, 0f, 0f, 1f,
//        0f, 0.4f, 1f, 0f, 0f
//    )
//    private var vertexArray = VertexArray(VERTEX_DATA)
//
//    fun bindData(colorProgram: ColorShaderProgram) {
//        vertexArray.setVertexAttribPointer(
//            0, colorProgram.aPositionLocation, POSITION_COMPONENT_COUNT,
//            STRIDE
//        )
//        vertexArray.setVertexAttribPointer(
//            POSITION_COMPONENT_COUNT,
//            colorProgram.aColorLocation,
//            COLOR_COMPONENT_COUNT,
//            STRIDE
//        )
//    }
//
//    fun draw() {
//        glDrawArrays(GL_POINTS, 0, 2)
//    }
    private val POSITION_COMPONENT_COUNT = 3

    private var vertexArray: VertexArray
    private var drawList: List<DrawCommand>
    init {
        val generatedData = ObjectBuilder.createMallet(
            Point(
                0f,
                0f, 0f
            ), radius, height, numPointsAroundMallet
        )

        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }
    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0,
            colorProgram.aPositionLocation, POSITION_COMPONENT_COUNT, 0
        )
    }

    fun draw() = drawList.map { it.draw() }
}