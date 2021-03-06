/**
 * Copyright 2010 Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jayway.opengl.tutorial

/**
 * SimplePlane is a setup class for Mesh that creates a plane mesh.
 *
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 */
class SimplePlane @JvmOverloads constructor(width: Float = 1f, height: Float = 1f) : Mesh() {
    /**
     * Create a plane.
     *
     * @param width
     * the width of the plane.
     * @param height
     * the height of the plane.
     */
    /**
     * Create a plane with a default with and height of 1 unit.
     */
    init {
        // Mapping coordinates for the vertices
        val textureCoordinates = floatArrayOf(
            0.0f, 1.0f,  //
            1.0f, 1.0f,  //
            0.0f, 0.0f,  //
            1.0f, 0.0f
        )
        val indices = shortArrayOf(0, 1, 2, 1, 3, 2)
        val vertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f,
            -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f
        )
        setIndices(indices)
        setVertices(vertices)
        setTextureCoordinates(textureCoordinates)
    }
}