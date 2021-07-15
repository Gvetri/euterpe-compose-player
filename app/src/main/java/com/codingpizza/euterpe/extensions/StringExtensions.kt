package com.codingpizza.euterpe.extensions

import com.google.android.exoplayer2.util.MimeTypes

fun String.obtainMimeTypeFromUri(): String {
    return when {
        this.substringBefore(".") == "m3u8" -> {
            MimeTypes.APPLICATION_M3U8
        }
        this.substringBefore(".") == "mkv" -> {
            MimeTypes.APPLICATION_MATROSKA
        }
        else -> {
            MimeTypes.getMediaMimeType(this) ?: ""
        }
    }
}
