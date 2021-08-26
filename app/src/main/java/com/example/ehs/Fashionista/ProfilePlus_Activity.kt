package com.example.ehs.Fashionista


import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.ClothesUpload_Request
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_clothes_save.*
import kotlinx.android.synthetic.main.activity_cody_save.*
import kotlinx.android.synthetic.main.activity_profile_plus_.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

class ProfilePlus_Activity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    lateinit var currentPhotoPath: String // 문자열 형태의 사진 경로 값 (초기 값을 null로 시작하고 싶을 때)

    lateinit var bmp : Bitmap
    lateinit var uploadImgName : String

    lateinit var plusImgName : String
    lateinit var clothesImg : Bitmap
    lateinit var userId : String

    lateinit var dialog : ProgressDialog
    var plusContent : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_plus_)

        userId = AutoLogin.getUserId(this)


        setSupportActionBar(toolbar_profile_Edit)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료

        var profile_intent = intent
        var plusImgArr = profile_intent.getByteArrayExtra("plusImgArr")
        var plusImg2 = BitmapFactory.decodeByteArray(plusImgArr, 0, plusImgArr!!.size)
        iv_profileplus.setImageBitmap(plusImg2)


        btn_profile_ok.setOnClickListener {
            plusContent = et_PlusContent.text.toString()
            dialog = ProgressDialog(this)
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            dialog.setMessage("업로드 중입니다.")
            dialog.show()

            uploadPlus(plusImg2)
        }


        btn_profile_gallery.setOnClickListener {
            openGallery()
        }
        btn_profile_camera.setOnClickListener {
            takeCapture()
        }

        //키보드입력시 다른 곳 클릭시 키보드 내려감
        layout_ProfilePlus.setOnClickListener{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(et_PlusContent.windowToken, 0)
        }

    }


    /**
     * 이미지 파일 생성
     */
    private fun createImageFile(): File {
        val timestamp: String = java.text.SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            .apply { currentPhotoPath = absolutePath }
    }


    fun takeCapture() {
        // 기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager!!)?.also {
                val photoFile: File? = try{
                    createImageFile()
                } catch (ex: IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.closet.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }


    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }


    /**
     * 테드 퍼미션 설정
     */
    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // 설정해놓은 위험 권한들이 허용되었을 경우 이곳을 수행함.
                Toast.makeText(this@ProfilePlus_Activity, "권한이 혀용 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@ProfilePlus_Activity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).check()
    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    } // 툴바 뒤로가기 액션 설정 끝



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면
                    val bitmap: Bitmap
                    val file = File(currentPhotoPath)
                    var fileuri = Uri.fromFile(file)
                    uploadImgName = getName(fileuri)
                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, fileuri)

                        bmp = bitmap

                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(this.contentResolver, fileuri)
                        bitmap = ImageDecoder.decodeBitmap(decode)

                        bmp = bitmap


                    }

                    if (file.exists()) {
                        file.delete()
                    }
                    if (fileuri != null) {
                        fileuri = null
                    }
                    iv_profileplus.setImageBitmap(bmp)
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
//                    uploadImgName = getName(currentImageUrl)

                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            currentImageUrl
                        )

                        bmp = bitmap
                        iv_profileplus.setImageBitmap(bmp)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }
    // 파일명 찾기
    private fun getName(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val cursor = managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


    fun uploadPlus(bitmap : Bitmap) {
        Log.d("aehsehsehs", plusContent)
        val clothesUploadRequest: ClothesUpload_Request =
            object : ClothesUpload_Request(
                Method.POST, "http://13.125.7.2/uploadPlus.php",
                Response.Listener<NetworkResponse> { response ->
                    try {

                        val obj = JSONObject(String(response!!.data))
//                        Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show()
                        plusImgName = obj.getString("file_name")
                        uploadDB(userId)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                    Log.e("GotError", "" + error.message)
                }) {
                override fun getByteData(): Map<String, DataPart>? {
                    val params: MutableMap<String, DataPart> = HashMap()
                    val imagename = System.currentTimeMillis()

                    params["image"] = DataPart("$imagename.JPEG", getFileDataFromDrawable(bitmap)!!)
                    return params
                }
            }

        //adding the request to volley
        Volley.newRequestQueue(this).add(clothesUploadRequest)

    }

    fun uploadDB(userId: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
//                        Toast.makeText(this@ProfilePlus_Activity, jsonObject.toString(), Toast.LENGTH_LONG).show()
                        if (dialog != null){
                            dialog.dismiss()
                            finish()
                        }

                    } else {
                        Toast.makeText(this@ProfilePlus_Activity, "실패 두둥탁", Toast.LENGTH_LONG).show()

                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }
        val plusImgPath = "http://13.125.7.2/img/fashionista_profile/"
        val profilePlusSave_Request = ProfilePlusSave_Request(userId, plusImgPath, plusImgName, plusContent, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(profilePlusSave_Request)
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }




}