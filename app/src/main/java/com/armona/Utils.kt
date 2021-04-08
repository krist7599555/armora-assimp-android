package com.armona

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import java.io.InputStream

class Utils {
    companion object {
        fun readResourceAsRawText(context: Context, id: Int): String {
            return context.resources.openRawResource(id).bufferedReader().use { it.readText() }
        }
        fun bitmapFromInputstream(inp: InputStream)
            =  inp.readBytes().let { BitmapFactory.decodeByteArray(it, 0, it.size) }
        fun bitmapFromAssetManager(assets: AssetManager, path: String)
            =  assets.open("images/screenshot.png").let { bitmapFromInputstream(it) }
    }
}