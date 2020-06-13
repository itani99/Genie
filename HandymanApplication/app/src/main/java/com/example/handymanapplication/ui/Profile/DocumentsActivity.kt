package com.example.handymanapplication.ui.Profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.handymanapplication.R
import com.example.handymanapplication.Utils.Utils

import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.example.handymanapplication.Utils.SharedPreferences
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.failure
import com.github.kittinunf.result.success
import kotlinx.android.synthetic.main.activity_documents_atcivity.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class DocumentsActivity : AppCompatActivity() {
    val RequestCode = 10001

    private var file_base64: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documents_atcivity)

        resume_view.webViewClient = WebViewClient()
        resume_view.settings.setSupportZoom(true)
        resume_view.settings.javaScriptEnabled = true
        val url = getPdfUrl()
        resume_view.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        upload.setOnClickListener {
            checkStoragePermission(0)
        }

    }

    fun getPdfUrl(): String? {
        return (Utils.BASE_IMAGE_URL.plus(intent!!.extras!!.getString("url")))
    }

    private fun checkStoragePermission(id: Int) {
        if (ContextCompat.checkSelfPermission(
                this!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this!!,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )

        } else {
            if (id == 0) {
                val intent = getFileChooserIntentForImageAndPdf()
                startActivityForResult(intent, RequestCode)

            }
        }
    }

    fun getFileChooserIntentForImageAndPdf(): Intent {
        val mimeTypes = arrayOf("application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == RequestCode) && (resultCode == Activity.RESULT_OK)) {
            var pdfFile: File? = null
            if (data != null) {
                data.data?.let {
                    if (it.scheme.equals("content")) {
                        val pdfBytes =
                            (this!!.contentResolver?.openInputStream(it))?.readBytes()
                        pdfFile = File(
                            this!!.getExternalFilesDir(null),
                            "Lesson ${Calendar.getInstance().time}t.pdf"
                        )

                        if (pdfFile!!.exists())
                            pdfFile!!.delete()
                        try {
                            val fos = FileOutputStream(pdfFile!!.path)
                            fos.write(pdfBytes)
                            fos.close()
                        } catch (e: Exception) {

                        }
                    } else {
                        pdfFile = it.toFile()
                    }

                }
            }



            file_base64 = Utils.convertFileToBase64(pdfFile!!)
            savePDF(file_base64.toString(), 0)

        }
    }

    fun savePDF(document: String, id: Int) {
        var pdf_type: String? = null
        when (id) {
            0 -> {
                pdf_type = "cv"
            }
            1 -> {
                pdf_type = "criminal_record"
            }
            2 -> {
                pdf_type = "certificate"
            }
        }


        // add progress bar
        Fuel.post(
            Utils.API_EDIT_PROFILE, listOf(pdf_type!! to document)
        ).header(
            "accept" to "application/json",
            Utils.AUTHORIZATION to SharedPreferences.getToken(this!!).toString()
        )
            .responseJson { _, _, result ->

                result.success {

                    var res = it.obj()
                    if (res.optString("status", "error") == "success") {
                        runOnUiThread {
                            Toast.makeText(this!!, "saved", Toast.LENGTH_LONG)
                                .show()

                            val i = Intent(this!!, ProfileActivity::class.java)
                            startActivity(i)
                        }
                    }
                }
                result.failure {
                    Toast.makeText(this!!, it.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }


}


