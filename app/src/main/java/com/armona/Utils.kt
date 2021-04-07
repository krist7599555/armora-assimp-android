package com.armona

import android.content.Context

class Utils {
    companion object {
        fun readResourceAsRawText(context: Context, id: Int): String {
            return context.resources.openRawResource(id).bufferedReader().use { it.readText() }
        }
    }
}