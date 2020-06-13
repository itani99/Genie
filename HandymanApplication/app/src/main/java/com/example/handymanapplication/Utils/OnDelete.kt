package com.example.handymanapplication.Utils

import android.content.Context
import org.json.JSONObject

interface OnDelete {
    fun delete(item: JSONObject)
}