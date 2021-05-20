package com.example.ehs

import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_image.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ImageActivity2 : AppCompatActivity() {
    val TAG: String = "이미지저장화면"



    var serverResponseCode = 0
    var path1 = Uri.parse("android.resource://com.example.ehs/" + R.drawable.back)
    var path: String = path1.toString()

    val uploadFilePath: String = "/storage/emulated/0/Pictures/" //경로를 모르겠으면, 갤러리 어플리케이션 가서 메뉴->상세 정보
    val uploadFileName: String = "asdf.png" //전송하고자하는 파일 이름


    var upLoadServerUri: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)



        checkSelfPermission()
        access.setOnClickListener {
            val intent = Intent() //기기 기본 갤러리 접근

            intent.type = MediaStore.Images.Media.CONTENT_TYPE //구글 갤러리 접근

            //intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 101)

        }





        /************* Php script path  */
        upLoadServerUri = "http://54.180.101.123/image.php"

        btn_comeon.setOnClickListener {
            Log.d(TAG, "불러오기버튼 클릭")
            asdf.setImageURI(path1)

        }

        btn_save.setOnClickListener {
            Log.d(TAG, "저장하버튼 클릭")


//            asdf.setImageResource(R.drawable.back)

            val bmp = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/asdf.png")
            val stbpm = BitmapToString(bmp)
            var bmpimg :String = bitmapToByteArray(bmp)
            asdf.setImageBitmap(bmp)
//            imageToDB(bmpimg)
            if (stbpm != null) {
                uploadFile(stbpm)
            }


        }
    }
    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //권한을 허용 했을 경우
        if (requestCode == 1) {
            val length = permissions.size
            for (i in 0 until length) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("MainActivity", "권한 허용 : " + permissions[i])
                }
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {

                try {
                    val iss: InputStream? = data?.data?.let { contentResolver.openInputStream(it) }
                    val bm = BitmapFactory.decodeStream(iss)

//                    val source: ImageDecoder.Source = ImageDecoder.createSource(getContentResolver())
//                    var bitmap: Bitmap? = ImageDecoder.decodeBitmap(source)
                    val bitmap = resize(bm)
                    val image = bitmap?.let { bitmapToByteArray(it) }
                    if (image != null) {
                        imageToDB(image)
                    } //변경된 프로필 이미지 서버로 보내기
                } catch (e: java.lang.Exception) {
                }
            } else if (resultCode == Activity.RESULT_CANCELED) { // 취소시 호출할 행동 쓰기
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
//            try {
//                val iss: InputStream? = contentResolver.openInputStream(data?.data!!)
//                val bm = BitmapFactory.decodeStream(iss)
//                iss?.close()
//                asdf.setImageBitmap(bm)
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }
//        } else if (requestCode == 101 && resultCode == Activity.RESULT_CANCELED) {
//            Toast.makeText(this, "취소", Toast.LENGTH_SHORT).show()
//        }
//    }

    /**비트맵을 바이너리 바이트배열로 바꾸어주는 메서드 */
    fun bitmapToByteArray (bitmap : Bitmap) :String {
        var image :String = ""
        var stream :ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        image = "&image=" + byteArrayToBinaryString(byteArray);
        return image
        
    }

    /**바이너리 바이트 배열을 스트링으로 바꾸어주는 메서드  */
    fun byteArrayToBinaryString(b: ByteArray): String? {
        val sb = StringBuilder()
        for (i in b.indices) {
            sb.append(byteToBinaryString(b[i]))
        }
        return sb.toString()
    }

    /**바이너리 바이트를 스트링으로 바꾸어주는 메서드  */
    fun byteToBinaryString(n: Byte): String? {
        val sb = StringBuilder("00000000")
        for (bit in 0..7) {
            if (((n.toInt() shr bit) and 1) > 0) {
                sb.setCharAt(7 - bit, '1')
            }
        }
        return sb.toString()
    }


    private fun imageToDB(image: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")

                    if (success) {
                        Toast.makeText(this@ImageActivity2, "저장성공", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "이것이 저장성공인것인가")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }
        var postId = "111"
        val registerRequest = ImageRequest(postId, image, responseListener)
        val queue = Volley.newRequestQueue(this@ImageActivity2)
        queue.add(registerRequest)
    }


    fun uploadFile(sourceFileUri: String): Int {
        Log.d(TAG, sourceFileUri)


        val fileName = sourceFileUri

        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****"
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        var buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024

        val sourceFile = File(sourceFileUri)


        if (!sourceFile.isFile) {
            Log.e("uploadFile", "Source File not exist :" + uploadFilePath + "" + uploadFileName)
            runOnUiThread {
                messageText.text = ("Source File not exist :" +uploadFilePath + "" + uploadFileName)
            }
            return 0
        }
        else{
            try {
                // open a URL connection to the Servlet
                val fileInputStream = FileInputStream(sourceFile)
                val url = URL(upLoadServerUri)

                // Open a HTTP  connection to  the URL
                conn = url.openConnection() as HttpURLConnection
                conn.setDoInput(true) // Allow Inputs
                conn.setDoOutput(true) // Allow Outputs
                conn.setUseCaches(false) // Don't use a Cached Copy
                conn.setRequestMethod("POST")
                conn.setRequestProperty("Connection", "Keep-Alive")
                conn.setRequestProperty("ENCTYPE", "multipart/form-data")
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
                conn.setRequestProperty("uploaded_file", fileName)


                dos = DataOutputStream(conn.getOutputStream())

                dos.writeBytes(twoHyphens + boundary + lineEnd)
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd)

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available()

                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                buffer = ByteArray(bufferSize)

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize)
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                }


                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd)
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)


                // Responses from the server (code and message)
                serverResponseCode = conn.responseCode
                val serverResponseMessage = conn.getResponseMessage()

                Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode)

                if (serverResponseCode == 200) {
                    runOnUiThread {
                        val msg = "File Upload Completed. See uploaded file here : http://www.androidexample.com/media/uploads/$uploadFileName"
                        messageText.text = msg
                        Toast.makeText(this@ImageActivity2, "File Upload Complete.", Toast.LENGTH_SHORT).show()
                    }
                }

                //close the streams //
                fileInputStream.close()
                dos.flush()
                dos.close()


            } catch (ex : MalformedURLException) {
                ex.printStackTrace();
                Log.e("Upload file to server", "서버업로드 에러라네")

            } catch (e : Exception) {
                e.printStackTrace();
                Log.e("Upload file to server Exception", "서버 익셉션 에러라네")
            }

            return serverResponseCode
        }

    }


}
