package com.armona.geometry

import android.R
import android.opengl.GLES20.*
import kotlin.math.cos
import kotlin.math.sin


class ObjectBuilder(sizeInVertices: Int) {
    private val FLOATS_PER_VERTEX: Int = 3
    private var offset = 0
    private val vertexData = FloatArray(sizeInVertices * FLOATS_PER_VERTEX);

    val drawList = ArrayList<DrawCommand>()

    companion object {
        fun sizeOfCircleInVertices(numPoints: Int) = 1 + (numPoints + 1)
        fun sizeOfOpenCylinderInVertices(numPoints: Int) = (numPoints + 1) * 2

        class GeneratedData(val vertexData: FloatArray, val drawList: List<DrawCommand>) {

        }
        interface DrawCommand {
            fun draw()
        }

        fun createPuck(puck: Cylinder, numPoints: Int): GeneratedData {
            val size = (sizeOfCircleInVertices(numPoints)
                    + sizeOfOpenCylinderInVertices(numPoints))
            val builder = ObjectBuilder(size)
            val puckTop = Circle(puck.center.translateY(puck.height / 2f), puck.radius)
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)
            return builder.build()
        }
        fun createMallet(center: Point, radius: Float, height: Float, numPoints: Int): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(
                numPoints
            ) * 2;
            val builder = ObjectBuilder(size);
            // First, generate the mallet base.
            val baseHeight = height * 0.25f;
            val baseCircle = Circle(center.translateY(-baseHeight), radius);
            val baseCylinder = Cylinder(
                baseCircle.center.translateY(-baseHeight / 2f),
                radius,
                baseHeight
            );
            builder.appendCircle(baseCircle, numPoints);
            builder.appendOpenCylinder(baseCylinder, numPoints);

            // Second, handle grip
            val handleHeight = height * 0.75f;
            val handleRadius = R.attr.radius / 3f
            val handleCircle = Circle(center.translateY(height * 0.5f), handleRadius)
            val handleCylinder = Cylinder(
                handleCircle.center.translateY(-handleHeight / 2f),
                handleRadius,
                handleHeight
            )
            builder.appendCircle(handleCircle, numPoints);
            builder.appendOpenCylinder(handleCylinder, numPoints);

            return builder.build();
        }
    }

    fun appendCircle(circle: Circle, numPoints: Int) { // Center point of fan
        val startVertex = offset / FLOATS_PER_VERTEX;
        val numVertices = sizeOfCircleInVertices(numPoints);

        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;
        // Fan around center point. <= is used because we want to generate // the point at the starting angle twice to complete the fan.
        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat() * (Math.PI.toFloat() * 2f))
            vertexData[offset++] = (circle.center.x + circle.radius * cos(angleInRadians));
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * sin(angleInRadians)
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    fun appendOpenCylinder(cylinder: Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX;
        val numVertices = sizeOfOpenCylinderInVertices(numPoints);
        val yStart = cylinder.center.y - (cylinder.height / 2f);
        val yEnd = cylinder.center.y + (cylinder.height / 2f);
        for (i in 0..numPoints) {
            val angleInRadians = i.toFloat() / numPoints.toFloat() * (Math.PI.toFloat() * 2f)
            val xPosition: Float = (cylinder.center.x + cylinder.radius * cos(angleInRadians))
            val zPosition: Float = (cylinder.center.z + cylinder.radius * sin(angleInRadians))

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }
        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }
}