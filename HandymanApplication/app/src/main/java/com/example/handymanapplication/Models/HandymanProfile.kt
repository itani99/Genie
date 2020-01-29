package com.example.handymanapplication.Models

import  com.github.kittinunf.fuel.core.ResponseDeserializable
data class  HandymanProfile(
    var email:String, var name:String, var api_token:String
){
    class  Deserializer : ResponseDeserializable<HandymanProfile>{
        override fun deserialize(content: String): HandymanProfile? {
            return super.deserialize(content)
        }
    }
}