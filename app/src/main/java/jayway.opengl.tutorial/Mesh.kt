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

import android.graphics.Bitmap
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL10.*

/**
 * Mesh is a base class for 3D objects making it easier to create and maintain
 * new primitives.
 *
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 */
open class Mesh {
	private var mVerticesBuffer: FloatBuffer? = null // Our vertex buffer.
	private var mIndicesBuffer: ShortBuffer? = null // Our index buffer.
	private var mTextureBuffer: FloatBuffer? = null // Our UV texture buffer.  // New variable.
	private var mTextureId: Int? = null // Our texture id. // New variable.
	private var mBitmap: Bitmap? = null // The bitmap we want to load as a texture. // New variable.
	private var mShouldLoadTexture = false // Indicates if we need to load the texture.  // New variable.
	private var mNumOfIndices = -1 	// The number of indices.
	private val mRGBA = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // Flat Color
	private var mColorBuffer: FloatBuffer? = null // Smooth Colors

	// Translate params.
	var x = 0f
	var y = 0f
	var z = 0f

	// Rotate params.
	var rx = 0f
	var ry = 0f
	var rz = 0f

	/**
	 * Render the mesh.
	 *
	 * @param gl
	 * the OpenGL context to render to.
	 */
	open fun draw(gl: GL10) {
		with(gl) {
			glFrontFace(GL_CCW) // Counter-clockwise winding.
			glEnable(GL_CULL_FACE) // Enable face culling.
			glCullFace(GL_BACK) // What faces to remove with the face culling.

			// Enabled the vertices buffer for writing and to be used during
			// rendering.
			glEnableClientState(GL_VERTEX_ARRAY)

			// Specifies the location and data format of an array of vertex
			// coordinates to use when rendering.
			glVertexPointer(3, GL_FLOAT, 0, mVerticesBuffer)

			glColor4f(mRGBA[0], mRGBA[1], mRGBA[2], mRGBA[3]) // Set flat color

			// Smooth color
			if (mColorBuffer != null) {
				// Enable the color array buffer to be used during rendering.
				glEnableClientState(GL_COLOR_ARRAY)
				glColorPointer(4, GL_FLOAT, 0, mColorBuffer)
			}

			// New part...
			if (mShouldLoadTexture) {
				loadGLTexture(gl)
				mShouldLoadTexture = false
			}
			if (mTextureId != null && mTextureBuffer != null) {
				glEnable(GL_TEXTURE_2D)
				// Enable the texture state
				glEnableClientState(GL_TEXTURE_COORD_ARRAY)

				// Point to our buffers
				glTexCoordPointer(2, GL_FLOAT, 0, mTextureBuffer)
				glBindTexture(GL_TEXTURE_2D, mTextureId!!)
			}
			// ... end new part.
			
			glTranslatef(x, y, z)
			glRotatef(rx, 1f, 0f, 0f)
			glRotatef(ry, 0f, 1f, 0f)
			glRotatef(rz, 0f, 0f, 1f)

			// Point out the where the color buffer is.
			glDrawElements(GL_TRIANGLES, mNumOfIndices, GL_UNSIGNED_SHORT, mIndicesBuffer)
			// Disable the vertices buffer.
			glDisableClientState(GL_VERTEX_ARRAY)

			// New part...
			if (mTextureId != -1 && mTextureBuffer != null) {
				glDisableClientState(GL_TEXTURE_COORD_ARRAY)
			}
			// ... end new part.

			// Disable face culling.
			glDisable(GL_CULL_FACE)
		}
	}

	/**
	 * Set the vertices.
	 *
	 * @param vertices
	 */
	protected fun setVertices(vertices: FloatArray) {
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
		vbb.order(ByteOrder.nativeOrder())
		mVerticesBuffer = vbb.asFloatBuffer().apply {
			put(vertices)
			position(0)
		}

	}

	/**
	 * Set the indices.
	 *
	 * @param indices
	 */
	protected fun setIndices(indices: ShortArray) {
		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		val ibb = ByteBuffer.allocateDirect(indices.size * 2)
		ibb.order(ByteOrder.nativeOrder())
		mIndicesBuffer = ibb.asShortBuffer().apply {
			put(indices)
			position(0)
		}
		mNumOfIndices = indices.size
	}

	/**
	 * Set the texture coordinates.
	 *
	 * @param textureCoords
	 */
	protected fun setTextureCoordinates(textureCoords: FloatArray) { // New
		// function.
		// float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		val byteBuf = ByteBuffer.allocateDirect(textureCoords.size * 4)
		byteBuf.order(ByteOrder.nativeOrder())
		mTextureBuffer = byteBuf.asFloatBuffer().apply {
			put(textureCoords)
			position(0)
		}
	}

	/**
	 * Set one flat color on the mesh.
	 *
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 */
	protected fun setColor(red: Float, green: Float, blue: Float, alpha: Float) {
		mRGBA[0] = red
		mRGBA[1] = green
		mRGBA[2] = blue
		mRGBA[3] = alpha
	}

	/**
	 * Set the colors
	 *
	 * @param colors
	 */
	protected fun setColors(colors: FloatArray) {
		// float has 4 bytes.
		val cbb = ByteBuffer.allocateDirect(colors.size * 4)
		cbb.order(ByteOrder.nativeOrder())
		mColorBuffer = cbb.asFloatBuffer().apply {
			put(colors)
			position(0)
		}
	}

	/**
	 * Set the bitmap to load into a texture.
	 *
	 * @param bitmap
	 */
	fun loadBitmap(bitmap: Bitmap?) { // New function.
		mBitmap = bitmap
		mShouldLoadTexture = true
	}

	/**
	 * Loads the texture.
	 *
	 * @param gl
	 */
	private fun loadGLTexture(gl: GL10) { // New function
		// Generate one texture pointer...
		val textures = IntArray(1)
		with(gl) {
			glGenTextures(1, textures, 0)
			mTextureId = textures[0]

			// ...and bind it to our array
			glBindTexture(GL_TEXTURE_2D, mTextureId!!)

			// Create Nearest Filtered Texture
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR.toFloat())
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())

			// Different possible texture parameters, e.g. GL_CLAMP_TO_EDGE
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE.toFloat())
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT.toFloat())
		}
		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL_TEXTURE_2D, 0, mBitmap, 0)
	}
}