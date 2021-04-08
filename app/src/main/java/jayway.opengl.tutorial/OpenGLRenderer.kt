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

import android.opengl.GLSurfaceView
import android.opengl.GLU
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLRenderer : GLSurfaceView.Renderer {
	private val root: Group

	/*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
     * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
     */
	override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 1.0f, 0.5f)
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH)
		// Depth buffer setup.
		gl.glClearDepthf(1.0f)
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST)
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL)
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
	}

	/*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
     * khronos.opengles.GL10)
     */
	override fun onDrawFrame(gl: GL10) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity()
		// Translates 4 units into the screen.
		gl.glTranslatef(0f, 0f, -4f)
		// Draw our scene.
		root.draw(gl)
	}

	/*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
     * .khronos.opengles.GL10, int, int)
     */
	override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height)
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION)
		// Reset the projection matrix
		gl.glLoadIdentity()
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(
			gl, 45.0f, width.toFloat() / height.toFloat(), 0.1f,
			1000.0f
		)
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW)
		// Reset the modelview matrix
		gl.glLoadIdentity()
	}

	/**
	 * Adds a mesh to the root.
	 *
	 * @param mesh
	 * the mesh to add.
	 */
	fun addMesh(mesh: Mesh?) {
		if (mesh != null) {
			root.add(mesh)
		}
	}

	init {
		// Initialize our root.
		val group = Group()
		root = group
	}
}