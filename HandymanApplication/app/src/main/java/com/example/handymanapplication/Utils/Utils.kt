package com.example.handymanapplication.Utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import com.example.handymanapplication.activities.MainActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class Utils {
    companion object {
        private const val BASE_URL = "https://handiman.club/api"
        const val BASE_IMAGE_URL = "https://uneteaforever.es/"
        const val AUTHORIZATION = "Authorization"

        const val API_CHECK_DISTRIBUTOR = "$BASE_URL/check-distributor/"
        const val API_LOGIN = "$BASE_URL/login"
        const val API_Register ="$BASE_URL/register"
        const val API_PROFILE = "$BASE_URL/profile"
        const val API_EDIT_PROFILE = "$BASE_URL/profile/edit"
        const val API_NIF_PROFILE = "$BASE_URL/profile/nif"
        const val API_DISTRIBUTOR = "$BASE_URL/distributor"
        const val API_PROFILE_PHOTO = "$BASE_URL/profile/photo"
        const val API_CATEGORIES = "$BASE_URL/categories"
        const val API_PRODUCTS = "$BASE_URL/products/"
        const val API_DEVICE_TOKEN = "$BASE_URL/device/token"
        const val API_SET_CART = "$BASE_URL/cart/set"
        const val API_CART = "$BASE_URL/cart"
        const val API_FAVORITE = "$BASE_URL/favorite"
        const val API_SET_FAVORITE = "$BASE_URL/favorite/set"
        const val API_Addresses = "$BASE_URL/addresses"
        const val API_Address = "$BASE_URL/address"
        const val API_Address_DELETE = "$BASE_URL/address-delete"
        const val API_CITY = "$BASE_URL/city-select/"
        const val API_CHECKOUT = "$BASE_URL/checkout-list/"
        const val API_ORDERS = "$BASE_URL/orders"
        const val API_CONFIRM_PAYMENT = "$BASE_URL/confirm-payment"
        const val API_REQUEST_PASSWORD = "$BASE_URL/request-password"
        const val API_RESET_PASSWORD = "$BASE_URL/reset-password"


//        fun sendRegistrationToServer(context: Context) {
//            if (SharedPreferences.getToken(context) != null) {
//
//                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//
//                    Fuel.post(
//                        API_DEVICE_TOKEN,
//                        listOf("device_token" to it.token, "device_platform" to "android")
//                    )
//                        .header(Utils.AUTHORIZATION to SharedPreferences.getToken(context).toString())
//                        .responseJson { _, _, result ->
//                            result.success {
//                                Log.i("Firebase reg", it.content)
//                            }
//                            result.failure {
//                                Log.i("Firebase fail", it.localizedMessage)
//                            }
//                        }
//                }
//
//
//            }
//        }

        fun isReadStoragePermissionGranted(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= 23) {
                if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    true
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity, arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), 0
                    )
                    false
                }
            } else {
                true
            }
        }

        fun encodeToBase64(image: Bitmap): String {
            val byteArrayOS = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOS)
            return android.util.Base64.encodeToString(byteArrayOS.toByteArray(), android.util.Base64.DEFAULT)
        }

        fun hideSoftKeyBoard(context: Context, view: View) {
            try {

                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
                // TODO: handle exception
                e.printStackTrace()
            }

        }

        fun stringToCalendar(date: String, pattern: String): Calendar {
            val format =
                SimpleDateFormat(pattern, Locale.getDefault())
            val d: Date = format.parse(date)!!
            val c: Calendar = Calendar.getInstance()
            c.time = d
            return c
        }
        fun logout(context : Context){
            SharedPreferences.clearPreferences(context, Constants.FILE_USER)
            val intent = Intent(context, MainActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }




}