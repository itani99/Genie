package com.example.handymanapplication.Helpers

import android.content.Context
import android.widget.Toast
import com.example.handymanapplication.Utils.SharedPreferences
import com.example.handymanapplication.Utils.Utils
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success

class ServiceHelper {
    companion object {
        fun saveService(id: String, context: Context) {

            Fuel.post(
                Utils.API_ADD_Service.plus(id)
            )
                .header(
                    "accept" to "application/json",
                    Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
                ).responseJson { _, _, result ->

                    result.success {
                        Toast.makeText(context, "Successfully added", Toast.LENGTH_LONG)
                            .show()

                    }

                    result.failure {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }
        }

        fun  deleteService(id:String,context: Context){

            Fuel.get(
                Utils.API_DELETE_SERVICE.plus(id)
            )
                .header(
                    "accept" to "application/json",
                    Utils.AUTHORIZATION to SharedPreferences.getToken(context!!).toString()
                )
                .responseJson { _, _, result ->

                    result.success {
                        Toast.makeText(context, "Successfully added", Toast.LENGTH_LONG)
                            .show()

                    }

                    result.failure {
                        Toast.makeText(context, it.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }

                }

        }
    }
}