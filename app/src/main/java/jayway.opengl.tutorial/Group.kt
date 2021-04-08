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

import java.util.*
import javax.microedition.khronos.opengles.GL10

/**
 * Group is a mesh with no mesh data itself but can contain one or more other
 * meshes.
 *
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 */
class Group : Mesh() {
	private val mChildren = Vector<Mesh>()
	override fun draw(gl: GL10) {
		val size = mChildren.size
		for (i in 0 until size) mChildren[i].draw(gl)
	}

	/**
	 * @param location
	 * @param object
	 * @see Vector.add
	 */
	fun add(location: Int, `object`: Mesh) {
		mChildren.add(location, `object`)
	}

	/**
	 * @param object
	 * @return
	 * @see Vector.add
	 */
	fun add(`object`: Mesh): Boolean {
		return mChildren.add(`object`)
	}

	/**
	 *
	 * @see Vector.clear
	 */
	fun clear() {
		mChildren.clear()
	}

	/**
	 * @param location
	 * @return
	 * @see Vector.get
	 */
	operator fun get(location: Int): Mesh {
		return mChildren[location]
	}

	/**
	 * @param location
	 * @return
	 * @see Vector.remove
	 */
	fun remove(location: Int): Mesh {
		return mChildren.removeAt(location)
	}

	/**
	 * @param object
	 * @return
	 * @see Vector.remove
	 */
	fun remove(`object`: Any?): Boolean {
		return mChildren.remove(`object`)
	}

	/**
	 * @return
	 * @see Vector.size
	 */
	fun size(): Int {
		return mChildren.size
	}
}