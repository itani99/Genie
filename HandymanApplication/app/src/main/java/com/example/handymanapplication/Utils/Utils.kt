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
import com.google.firebase.iid.FirebaseInstanceId
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.os.FileUtils
import android.util.Base64OutputStream
import java.io.FileInputStream
import java.nio.charset.StandardCharsets


class Utils {
    companion object {
        private const val BASE_URL = "https://handiman.club/api"
        const val BASE_IMAGE_URL = "https://handiman.club/public/storage/"
        private const val BASE_URL_EMPLOYEE = "https://handiman.club/api/employee"
        const val AUTHORIZATION = "Authorization"
        const val API_ADD_Service = "$BASE_URL_EMPLOYEE/add-service/"
        const val API_GET_JOBS = "$BASE_URL_EMPLOYEE/jobs"
        const val API_DELETE_SERVICE = "$BASE_URL_EMPLOYEE/delete-service/"
        const val API_LOGIN = "$BASE_URL/login"

        const val API_Services = "$BASE_URL/getServices"
        const val API_Register = "$BASE_URL/register"
        const val API_EDIT_PROFILE = "$BASE_URL/profile-edit"
        const val API_DEVICE_TOKEN = "$BASE_URL/device-token"
        const val API_CREDIT_CARD = "$BASE_URL/credit-card"
        const val API_POST = "$BASE_URL_EMPLOYEE/post"
        const val API_Timeline = "$BASE_URL/timeline-view/"
        const val API_ONGOING_REQUESTS = "$BASE_URL_EMPLOYEE/pending-requests"
        const val API_APPROVE_REQUESTS = "$BASE_URL_EMPLOYEE/reply-request/"
        const val API_RECEIPT = "$BASE_URL_EMPLOYEE/receipt/"
        const val API_OUTGOING_REQUESTS = "$BASE_URL/jobs/"
        const val API_CHAT_REQUESTS = "$BASE_URL_EMPLOYEE/chat-requests"
        const val API_SEND_MESSAGE = "$BASE_URL/message/"
        const val API_SCHEDULE = "$BASE_URL_EMPLOYEE/schedule"

        fun sendRegistrationToServer(context: Context) {
            if (SharedPreferences.getToken(context) != null) {
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {

                    Fuel.post(
                        API_DEVICE_TOKEN,
                        listOf("employee_device_token" to it.token, "device_platform" to "android")
                    )
                        .header(Utils.AUTHORIZATION to SharedPreferences.getToken(context).toString())
                        .responseJson { _, _, result ->
                            result.success {
                                Log.i("Firebase reg", it.content)

                            }
                            result.failure {
                                Log.i("Firebase fail", it.localizedMessage)
                            }
                        }
                }


            }
        }

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
            image.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOS)
            return android.util.Base64.encodeToString(
                byteArrayOS.toByteArray(),
                android.util.Base64.DEFAULT
            )
        }

        fun convertFileToBase64(imageFile: File): String {
            return FileInputStream(imageFile).use { inputStream ->
                ByteArrayOutputStream().use { outputStream ->
                    Base64OutputStream(
                        outputStream,
                        android.util.Base64.DEFAULT
                    ).use { base64FilterStream ->
                        inputStream.copyTo(base64FilterStream)
                        base64FilterStream.close() // This line is required, see comments
                        outputStream.toString()
                    }
                }
            }
        }

        fun hideSoftKeyBoard(context: Context, view: View) {
            try {

                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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

        fun logout(context: Context) {
            SharedPreferences.clearPreferences(context, Constants.FILE_USER)
            val intent = Intent(context, MainActivity::class.java)

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
        fun addChar(str: String, ch: Char, position: Int): String {
            val len = str.length
            val updatedArr = CharArray(len + 1)
            str.toCharArray(updatedArr, 0, 0, position)
            updatedArr[position] = ch
            str.toCharArray(updatedArr, position + 1, position, len)
            return String(updatedArr)
        }

    }


}