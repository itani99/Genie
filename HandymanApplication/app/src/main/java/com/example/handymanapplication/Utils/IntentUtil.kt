package com.example.handymanapplication.Utils


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import android.content.Intent

const val DEFAULT_NAME = "object"

object IntentUtil {
    @Suppress("SpellCheckingInspection")
    val gson: Gson = GsonBuilder().create()
}

fun Intent.putExtraJson(name: String, src: Any) {
    putExtra(name, IntentUtil.gson.toJson(src))
}

fun Intent.putExtraJson(src: Any) {
    putExtra(DEFAULT_NAME, IntentUtil.gson.toJson(src))
}

fun <T> Intent.getJsonExtra(name: String, `class`: Class<T>): T? {
    val stringExtra = getStringExtra(name)
    if (stringExtra != null) {
        return IntentUtil.gson.fromJson<T>(stringExtra, `class`)
    }
    return null
}

fun <T> Intent.getJsonExtra(`class`: Class<T>): T? {
    val stringExtra = getStringExtra(DEFAULT_NAME)
    if (stringExtra != null) {
        return IntentUtil.gson.fromJson<T>(stringExtra, `class`)
    }
    return null
}