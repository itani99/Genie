package com.example.handymanapplication.Models

import android.view.View

import java.util.ArrayList

/**
 * Simple POJO model for example
 */
class ItemCell(

    var pos: String,
    var client: String?,
    var image: String,
    var created_at_date: String,
    var request_to_date: String,
    var head_image: String,
    var service_image: String,
    var request_title: String,
    var description: String,
    var street: String,
    val has_receipt:Boolean?=null,
    var state: String,
    var longitude: Double,
    var latitude: Double,
    var request_from: String,
    var request_to: String,
    var service_name:String
)

{

}