package com.armona

import android.content.Context
import android.opengl.GLSurfaceView
import assimp.Importer
import assimp._filename
import java.io.BufferedReader

class OpenGLView: GLSurfaceView {
    constructor(context: Context) : super(context) {
        println("\n\n\n>>>>>>>HERRE <<<<\n\n\n")
        setEGLContextClientVersion(2);
//        super.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
        preserveEGLContextOnPause = true
        val b = context.assets
        val c = b.list(".")
        val fl2 = b.list("")
        val o = b.list("obj")
        val o2 = b.list("*.obj")
        val o3 = b.list("*")
        _filename = "model/arrow/Arrow5.obj"
        val path = _filename
        val op = context.assets.open(path)
        val txt = op.bufferedReader().use(BufferedReader::readText)

        val d = Importer().readFile(context.assets, path);
        val e = d;
        println("load model success")
//        val m1 = Assimp.aiImportFile("file:///android_asset/sculpt.obj", Assimp.aiProcess_JoinIdenticalVertices or Assimp.aiProcess_Triangulate)
//        val m2 = Assimp.aiImportFile("file:///android_asset/sculpt.obj", 0)
//        setRenderer(OpenGLRenderer())
//        val obj = Utils.readResourceAsRawText(context, R.raw.magnet_obj)
//        println(obj)
//        val fileIo = AIFileIO.create()
//            .OpenProc { pFileIO: Long, fileName: Long, openMode: Long ->
//                val data: ByteBuffer
//                val fileNameUtf8 = fileName.toString()
//                try {
//                    val a = context.resources.getIdentifier(fileNameUtf8, "raw", context.packageName)
//                    data = ByteBuffer.wrap(readResourceAsRawText(context, a).toByteArray())
//                } catch (e: Exception) {
//                    throw RuntimeException("Could not open file: $fileNameUtf8")
//                }
//                AIFile.create()
//                    .ReadProc { pFile: Long, pBuffer: Long, size: Long, count: Long ->
//                        val max = Math.min(data.remaining().toLong(), size * count)
//                        memCopy(memAddress(data) + data.position(), pBuffer, max)
//                        max
//                    }
//                    .SeekProc { pFile: Long, offset: Long, origin: Int ->
//                        if (origin == Assimp.aiOrigin_CUR) {
//                            data.position(data.position() + offset.toInt())
//                        } else if (origin == Assimp.aiOrigin_SET) {
//                            data.position(offset.toInt())
//                        } else if (origin == Assimp.aiOrigin_END) {
//                            data.position(data.limit() + offset.toInt())
//                        }
//                        0
//                    }
//                    .FileSizeProc { pFile: Long -> data.limit().toLong() }
//                    .address()
//            }
//            .CloseProc { pFileIO: Long, pFile: Long ->
//                val aiFile = AIFile.create(pFile)
//                aiFile.ReadProc().free()
//                aiFile.SeekProc().free()
//                aiFile.FileSizeProc().free()
//            }
//        val lo = Assimp.aiImportFileEx("/raw/magnet_obj.obj", Assimp.aiProcess_JoinIdenticalVertices or Assimp.aiProcess_Triangulate, fileIo)
//        val buffer = ByteBuffer.wrap(obj.toByteArray())
//        val hint: ByteBuffer? = null
//        val model = Assimp.aiImportFileFromMemory(buffer, Assimp.aiProcess_JoinIdenticalVertices or Assimp.aiProcess_Triangulate, hint)
        setRenderer(MyGLRenderer())
//        val fl = File("/assets").listFiles()
//        setRenderer(World())
//        println("craete openglView")
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
    }
}