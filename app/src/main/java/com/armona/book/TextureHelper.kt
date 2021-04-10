package com.armona.book

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils

class TextureHelper {
    companion object {
        fun loadTexture(context: Context, resourceId: Int): Int {
            val textureObjectIds: IntArray = IntArray(1)
            glGenTextures(1, textureObjectIds, 0)
            if (textureObjectIds[0] == 0) {
                throw Exception("Could not generate a new OpenGL texture object.")
            }
            val options = BitmapFactory.Options();
            options.inScaled = false;
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options);
            if (bitmap == null) {
                glDeleteTextures(1, textureObjectIds, 0);
                throw Exception("Resource ID $resourceId could not be decoded.");
            }
            glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
            glGenerateMipmap(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);
            return textureObjectIds[0]
        }
    }
}