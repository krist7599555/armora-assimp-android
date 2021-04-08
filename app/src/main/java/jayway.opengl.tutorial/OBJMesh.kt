package jayway.opengl.tutorial

import android.content.res.AssetManager
import android.opengl.GLES11.GL_MODELVIEW_MATRIX
import android.opengl.GLES11.glGetFloatv
import assimp.AiVector3D
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

fun floatArrayFromArrayList(face: Int, list: List<AiVector3D>): FloatArray {
    var fa = FloatArray(list.size * face)
    var idx = 0
    for (vec in list) {
        for (i in 0.until(face)) {
            fa[idx++] = vec[i]
        }
    }
    return fa
}

class OBJMesh(val ai: assimp.AiScene): Mesh() {
    val root = Group();
//    class InnerOBJMesh: Mesh {
//        constructor(mesh: assimp.AiMesh, material: assimp.AiMaterial) {
//            setVertices(floatArrayFromArrayList(3, mesh.mVertices))
////            setColor()
////            setTextureCoordinates(floatArrayFromArrayList(2, mesh.mTextureCoords))
//        }
//    }

    override fun draw(gl: GL10) {
        println("draw nothing")
        with (gl) {
//          FUCK IT NOT WORK
            glFrontFace(GL10.GL_CW);
            glEnableClientState(GL10.GL_VERTEX_ARRAY)
            for (mesh in ai.mMeshes) {
                val buff = ByteBuffer
                    .allocateDirect(mesh.mNumVertices * Float.SIZE_BYTES * 3)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .apply {
                        for (vtx in mesh.mVertices) {
                            put(vtx[0])
                            put(vtx[1])
                            put(vtx[2])
                        }
                        position(0)
//                        flip()
                    }
                gl.glVertexPointer(mesh.mNumVertices, GL10.GL_FLOAT, 3, buff)

                val buffFace = ByteBuffer
                    .allocateDirect(mesh.mNumFaces * Short.SIZE_BYTES * 6) // this obj use polygon
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .apply {
                        for (quads in mesh.mFaces) { // GL_QUADS
                            put(quads[0].toShort()); put(quads[1].toShort()); put(quads[2].toShort());
                            put(quads[2].toShort()); put(quads[3].toShort()); put(quads[0].toShort());
                        }
                        position(0)
//                        flip()
                    }
                println("GL VERSION = ${gl.glGetString(GL10.GL_VERSION)}")
//                glMatrixMode(GL10.GL_MODELVIEW) // Select the modelview matrix
//			    glLoadIdentity() // Reset the modelview matrix() // Replace the current matrix with the identity matrix
                val matrix = FloatBuffer.allocate(16);
                glGetFloatv(GL_MODELVIEW_MATRIX, matrix);
//                glScalef(1f, 1f, 1f);
                println("MMATRIX = [${matrix[0]} ${matrix[1]} ${matrix[2]} ${matrix[3]}]")
                println("MMATRIX = [${matrix[4]} ${matrix[5]} ${matrix[6]} ${matrix[7]}]")
                println("MMATRIX = [${matrix[8]} ${matrix[9]} ${matrix[10]} ${matrix[11]}]")
                println("MMATRIX = [${matrix[12]} ${matrix[13]} ${matrix[14]} ${matrix[15]}]")
//			glTranslatef(0f, 0f, -4f) // Translates 4 units into the screen.
//                glTranslatef(0f, 0f, -4f)
                glColor4f(1f, 1f, 0f, 1f);
                gl.glDrawElements(GL10.GL_TRIANGLES, mesh.mNumFaces, GL10.GL_UNSIGNED_SHORT, buffFace)
////            TODO("chake how draw object work")
////            mat.bind()
////            glDrawElements(GL_TRIANGLES, mesh.mNumVertices, GL_UNSIGNED_INT, mesh.)
            }
            glDisableClientState(GL10.GL_VERTEX_ARRAY)
        }
    }
    companion object {
        fun fromAssetManager(assets: AssetManager, path: String): Mesh {
            assimp._filename = path
            assimp._assets = assets
            val ai = assimp.Importer().readFile(assets, path)!!
            return OBJMesh(ai)
        }
    }
}