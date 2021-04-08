package jayway.opengl.tutorial

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.armona.Utils

//import jayway.opengl.tutorial.OpenGLRenderer

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
/**
 * This class is the setup for the Tutorial part VI located at:
 * http://blog.jayway.com/
 *
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 */
class TutorialPartVI2 : Activity() {
    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Remove the title bar from the window.
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Make the windows into full screen mode.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Create a OpenGL view.
        val view = GLSurfaceView(this)
        view.setEGLContextClientVersion(2)
// Assign our renderer.
// Assign our renderer.
//        view.setRenderer(FirstOpenGLProjectRenderer())

        // Creating and attaching the renderer.
        val renderer = OpenGLRenderer()
        view.setRenderer(renderer)
        setContentView(view)

        // Create a new plane.
        val plane = SimplePlane(1f, 1f)
		plane.loadBitmap(Utils.bitmapFromAssetManager(assets, "images/screenshot.png"));

        // Move and rotate the plane.
//        plane.z = 1.7f
//        plane.rx = -65f
        val plane2 = SimplePlane(1f, 1f)
        plane2.loadBitmap(Utils.bitmapFromAssetManager(assets, "images/screenshot2.png"));
//        plane2.x += 0.4f
        plane2.y -= 0.5f
        plane2.z += 0.5f
        plane2.rx = -90f


        // Add the plane to the renderer.
        val arrow = OBJMesh.fromAssetManager(assets, "model/arrow/Arrow5.obj")
        renderer.addMesh(arrow)
        renderer.addMesh(plane)
        renderer.addMesh(plane2)

//        Looper
//        val imgs = arrayOf("images/screenshot.png", "images/screenshot2.png")
//        for (i in 0..10) {
//            Handler(Looper.getMainLooper()).postDelayed({
//                println("LOOPER $i")
//                plane.loadBitmap(
//                    imgs.get(i % 2)
//                        .let { assets.open(it) }
//                        .let { it.readBytes() }
//                        .let { BitmapFactory.decodeByteArray(it, 0, it.size) }
//                )
//            }, (1000 * i).toLong())
//        }
    }
}