package com.example.handymanapplication.Utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity


class SharedPreferences {

    companion object {

        fun setPrefernces(context: Context, file: String, key: String, value: Any) {
            when (value) {
                is Int -> context.getSharedPreferences(
                    file,
                    Context.MODE_PRIVATE
                ).edit().putInt(key, value.toInt()).apply()
                is Boolean -> context.getSharedPreferences(
                    file,
                    Context.MODE_PRIVATE
                ).edit().putBoolean(key, value).apply()
                is Float -> context.getSharedPreferences(
                    file,
                    Context.MODE_PRIVATE
                ).edit().putFloat(key, value.toFloat()).apply()
                is Long -> context.getSharedPreferences(file, Context.MODE_PRIVATE).edit().putLong(
                    key,
                    value.toLong()
                ).apply()
                is String -> context.getSharedPreferences(
                    file,
                    Context.MODE_PRIVATE
                ).edit().putString(key, value.toString()).apply()
            }
        }

        fun clearPreferences(context: Context, file: String) {
            context.getSharedPreferences(file, Context.MODE_PRIVATE).edit().clear().apply()
        }

        fun clearPreference(context: Context, file: String, item: String) {
            context.getSharedPreferences(file, Context.MODE_PRIVATE).edit().remove(item).apply()
        }


        fun getPreferences(context: Context, file: String, key: String): Any? {
            val map: Map<String, *> = context.getSharedPreferences(file, Context.MODE_PRIVATE).all

            for ((k, v) in map) {
                if (k == key) return v
            }
            return null
        }

        fun getID(context: Context): String? {
            return if (getPreferences(
                    context,
                    Constants.FILE_USER,
                    Constants.USER_ID
                ).toString() != "null"
            ) {
                getPreferences(
                    context,
                    Constants.FILE_USER,
                    Constants.USER_ID
                ).toString()
            } else {
                null
            }
        }

        fun getToken(context: Context): String? {
            return if (getPreferences(
                    context,
                    Constants.FILE_USER,
                    Constants.USER_TOKEN
                ).toString() != "null"
            ) {
                "Bearer " + getPreferences(
                    context,
                    Constants.FILE_USER,
                    Constants.USER_TOKEN
                ).toString()
            } else {
                null
            }
        }


    }
}


