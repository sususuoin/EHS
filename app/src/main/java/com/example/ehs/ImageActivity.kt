package com.example.ehs

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_image.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ImageActivity : Activity() {
    // LOG
    private val TAGLOG = "MainActivityLoG"

    // 서버로 업로드할 파일관련 변수
    var uploadFilePath: String? = null
    var uploadFileName: String? = null
    private val REQ_CODE_PICK_PICTURE = 1

    // 파일을 업로드 하기 위한 변수 선언
    private var serverResponseCode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        ivUploadImage.setImageResource(R.drawable.back)
        ivUploadImage.setOnClickListener {
            Log.d("이것이 클릭", "아아아아")
            val i = Intent(Intent.ACTION_PICK)
            i.type = MediaStore.Images.Media.CONTENT_TYPE
            i.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI // images on the SD card.


            // 결과를 리턴하는 Activity 호출
            startActivityForResult(i, REQ_CODE_PICK_PICTURE)
        }

        btnUploadImage.setOnClickListener {
            if (uploadFilePath != null) {
                Log.d("이것이 클릭", "아아아아ㅁㄴㅇㄹ")
                val uploadimagetoserver = UploadImageToServer()
                uploadimagetoserver.execute("http://54.180.101.123/upload.php")


            } else {
                Toast.makeText(this@ImageActivity, "You didn't insert any image", Toast.LENGTH_SHORT).show()
            }
        }

        checkSelfPermission()
        access.setOnClickListener {
            val intent = Intent() //기기 기본 갤러리 접근

            intent.type = MediaStore.Images.Media.CONTENT_TYPE //구글 갤러리 접근

            //intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 101)

        }



    }


    fun checkSelfPermission() {
        var temp = "" //파일 읽기 권한 확인


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE.toString() + " "
        } //파일 쓰기 권한 확인


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE.toString() + " "
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim { it <= ' ' }.split(" ").toTypedArray(), 1)
        } else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show()
        }


    }


    // ==========================================================================================
    // ==================================== 사진을 불러오는 소스코드 ============================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQ_CODE_PICK_PICTURE) {
            if (resultCode == RESULT_OK) {
                val uri = data.data
                val path = getPath(uri)
                val name = getName(uri)
                uploadFilePath = path
                uploadFileName = name
                Log.i(TAGLOG, "[onActivityResult] uploadFilePath:$uploadFilePath, uploadFileName:$uploadFileName")
                val bit = BitmapFactory.decodeFile(path)
                val bitmap = resize(bit);
                ivUploadImage.setImageBitmap(bit)
                var image = bitmap?.let { BitmapToString(it) }
                imageToDB(uploadFileName!!)

//                if (image != null) {
//                    imageToDB(image)
//                }


            }
        }
    }

    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    // 실제 경로 찾기
    private fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // 파일명 찾기
    private fun getName(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor
                .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // uri 아이디 찾기
    private fun getUriId(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.ImageColumns._ID)
        val cursor: Cursor = managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // ==========================================================================================
    // ==========================================================================================
    // ============================== 사진을 서버에 전송하기 위한 스레드 ========================
    private inner class UploadImageToServer : AsyncTask<String?, String?, String?>() {
        var mProgressDialog: ProgressDialog? = null
        var fileName = uploadFilePath
        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        var lineEnd = "\r\n"
        var twoHyphens = "--"
        var boundary = "*****"
        var bytesRead = 0
        var bytesAvailable = 0
        var bufferSize = 0
        var buffer: ByteArray? = null
        var maxBufferSize = 1 * 10240 * 10240
        var sourceFile = File(uploadFilePath)



        protected override fun onPreExecute() {

            // Create a progressdialog
            mProgressDialog = ProgressDialog(this@ImageActivity)
            mProgressDialog!!.setTitle("Loading...")
            mProgressDialog!!.setMessage("Image uploading...")
            mProgressDialog!!.setCanceledOnTouchOutside(false)
            mProgressDialog!!.setIndeterminate(false)
            mProgressDialog!!.show()
        }

        protected override fun doInBackground(vararg serverUrl: String?): String? {
            return if (!sourceFile.isFile) {
                runOnUiThread { Log.i(TAGLOG, "[UploadImageToServer] Source File not exist :$uploadFilePath") }
                null
            } else {
                try {

                    // open a URL connection to the Servlet
                    val fileInputStream = FileInputStream(sourceFile)
                    val url = URL(serverUrl[0])


                    // Open a HTTP  connection to  the URL
                    conn = url.openConnection() as HttpURLConnection
                    conn!!.doInput = true // Allow Inputs
                    conn!!.doOutput = true // Allow Outputs
                    conn!!.useCaches = false // Don't use a Cached Copy
                    conn!!.requestMethod = "POST"
                    conn!!.setRequestProperty("Connection", "Keep-Alive")
                    conn!!.setRequestProperty("ENCTYPE", "multipart/form-data")
                    conn!!.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
                    conn!!.setRequestProperty("uploaded_file", fileName)
                    Log.i(TAGLOG, "fileName: $fileName")
                    dos = DataOutputStream(conn!!.outputStream)


                    // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다.
                    dos!!.writeBytes(twoHyphens + boundary + lineEnd)
                    dos!!.writeBytes("Content-Disposition: form-data; name=\"img\"" + lineEnd);
                    dos!!.writeBytes(lineEnd)
                    dos!!.writeBytes("newImage")
                    dos!!.writeBytes(lineEnd)


                    // 이미지 전송
                    dos!!.writeBytes(twoHyphens + boundary + lineEnd)
                    dos!!.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"$fileName\"$lineEnd")
                    dos!!.writeBytes(lineEnd)


                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    buffer = ByteArray(bufferSize)


                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    while (bytesRead > 0) {
                        dos!!.write(buffer, 0, bufferSize)
                        bytesAvailable = fileInputStream.available()
                        bufferSize = Math.min(bytesAvailable, maxBufferSize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    }


                    // send multipart form data necesssary after file data...
                    dos!!.writeBytes(lineEnd)
                    dos!!.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)


                    // Responses from the server (code and message)
                    serverResponseCode = conn!!.responseCode
                    val serverResponseMessage = conn!!.responseMessage
                    Log.d("아 왠안되냐",serverResponseMessage )
                    Log.i(TAGLOG, "[UploadImageToServer] HTTP Response is : $serverResponseMessage: $serverResponseCode")
                    if (serverResponseCode == 200) {
                        runOnUiThread { Toast.makeText(this@ImageActivity, "File Upload Completed", Toast.LENGTH_SHORT).show() }
                    }

                    //close the streams //
                    fileInputStream.close()
                    dos!!.flush()
                    dos!!.close()
                } catch (ex: MalformedURLException) {
                    ex.printStackTrace()
                    runOnUiThread { Toast.makeText(this@ImageActivity, "MalformedURLException", Toast.LENGTH_SHORT).show() }
                    Log.i(TAGLOG, "[UploadImageToServer] error: " + ex.message, ex)
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread { Toast.makeText(this@ImageActivity, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show() }
                    Log.i(TAGLOG, "[UploadImageToServer] Upload file to server Exception Exception : " + e.message, e)
                }
                Log.i(TAGLOG, "[UploadImageToServer] Finish")
                null
            } // End else block
        }

        protected override fun onPostExecute(s: String?) {
            mProgressDialog?.dismiss()
        }
    }


    private fun resize(bm: Bitmap): Bitmap? {
        var bm: Bitmap? = bm
        val config: Configuration = resources.configuration
        bm = if (config.smallestScreenWidthDp >= 800)
            Bitmap.createScaledBitmap(bm!!, 400, 240, true)
        else if (config.smallestScreenWidthDp >= 600)
            Bitmap.createScaledBitmap(bm!!, 300, 180, true)
        else if (config.smallestScreenWidthDp >= 400)
            Bitmap.createScaledBitmap(bm!!, 200, 120, true)
        else if (config.smallestScreenWidthDp >= 360)
            Bitmap.createScaledBitmap(bm!!, 180, 108, true)
        else
            Bitmap.createScaledBitmap(bm!!, 160, 96, true)

        return bm
    }

    private fun imageToDB(clothesName: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")

                    if (success) {
                        Toast.makeText(this@ImageActivity, "저장성공", Toast.LENGTH_SHORT).show()
                        Log.d("제발이잉이ㅣ", "이것이 저장성공인것인가")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        var userId = "111"
        val clothesPath = "/var/www/html/clothes/"
        val imageRequest = Image_Request(userId, clothesPath, clothesName, responseListener)
        val queue = Volley.newRequestQueue(this@ImageActivity)
        queue.add(imageRequest)
    }

}