package com.armona

import android.opengl.Matrix
import org.lwjgl.PointerBuffer
import org.lwjgl.assimp.AIMaterial
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.Assimp

private fun ptrbufferToAddress(n: Int, ptr: PointerBuffer?): List<Long> {
    return 0.until(n).map { idx -> ptr!!.get(idx) }
}

class Model(val materials: List<AIMaterial>, val meshes: List<AIMesh>) {
    val mModelMatrix: FloatArray  = FloatArray(16).also {
        Matrix.setIdentityM(it, 0)
    }
    companion object {
        fun fromPath(path: String): Model {
            val scene = Assimp.aiImportFile(path, Assimp.aiProcess_JoinIdenticalVertices or Assimp.aiProcess_Triangulate)
                ?: throw IllegalStateException(Assimp.aiGetErrorString())
            val materials = ptrbufferToAddress(scene.mNumMaterials(), scene.mMaterials()).map { AIMaterial.create(it) }
            val meshes = ptrbufferToAddress(scene.mNumMaterials(), scene.mMaterials()).map { AIMesh.create(it) }
            return Model(materials, meshes)
        }
    }
}