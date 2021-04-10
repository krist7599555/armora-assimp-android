package com.armona.geometry

import com.armona.book.ColorShaderProgram
import com.armona.book.VertexArray


open class Geometry {
}

class Point(val x: Float, val y: Float, val z: Float) {
    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }
}

class Circle(val center: Point, val radius: Float) {
    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }
}

class Cylinder(val center: Point, val radius: Float, val height: Float) {

}


class Puck(val radius: Float, val height: Float, val numPointsAroundPuck: Int) {
    companion object {
        const val POSITION_COMPONENT_COUNT = 3;
    }
    private var vertexArray: VertexArray;
    private var drawList: List<ObjectBuilder.Companion.DrawCommand>;
    init {
        val generatedData = ObjectBuilder.createPuck(
            Cylinder(Point(0f, 0f, 0f), radius, height),
            numPointsAroundPuck
        );
        vertexArray = VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.aPositionLocation, POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() = drawList.map { it.draw() }
}
