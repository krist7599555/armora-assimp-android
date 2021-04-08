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
import javax.microedition.khronos.opengles.GL10.*

class OpenGLRenderer : GLSurfaceView.Renderer {
	private val root = Group()

	/*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
     * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
     */
	override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
		with (gl) {
			glClearColor(0.0f, 0.0f, 1.0f, 0.5f) // Set the background color to black ( rgba ).
			glShadeModel(GL_SMOOTH) // Enable Smooth Shading, default not really needed.
			glClearDepthf(1.0f) // Depth buffer setup.
			glEnable(GL_DEPTH_TEST) // Enables depth testing.
			glDepthFunc(GL_LEQUAL) // The type of depth testing to do.
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST) // Really nice perspective calculations.
		}
	}

	/*
     * (non-Javadoc)
     *
     * @see
     * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
     * khronos.opengles.GL10)
     */
	override fun onDrawFrame(gl: GL10) {
		with (gl) {
			glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // Clears the screen and depth buffer.
			glLoadIdentity() // Replace the current matrix with the identity matrix
//			glTranslatef(0f, 0f, -4f) // Translates 4 units into the screen.
			glTranslatef(0f, 0f, -4f) // Translates 4 units into the screen.
		}
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
		val aspect = width.toFloat() / height.toFloat()
		with (gl) {
			glViewport(0, 0, width, height) // Sets the current view port to the new size.

			glMatrixMode(GL_PROJECTION) // Select the projection matrix
			glLoadIdentity() // Reset the projection matrix
			GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 1000.0f) // Calculate the aspect ratio of the window

			glMatrixMode(GL_MODELVIEW) // Select the modelview matrix
			glLoadIdentity() // Reset the modelview matrix
		}
	}

	/**
	 * Adds a mesh to the root.
	 *
	 * @param mesh
	 * the mesh to add.
	 */
	fun addMesh(mesh: Mesh) {
		if (mesh != null) {
			root.add(mesh)
		}
	}

}