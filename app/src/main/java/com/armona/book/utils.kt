package com.armona.book

import android.content.Context

fun readTextFileFromResource(context: Context, resourceId: Int): String {
    return context
        .resources
        .openRawResource(resourceId)
        .readBytes()
        .toString(Charsets.UTF_8)
}