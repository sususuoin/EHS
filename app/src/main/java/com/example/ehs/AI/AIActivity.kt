package com.example.ehs.AI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.example.ehs.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_ai.*
import java.io.File

class AIActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)

        //권한설정
        setPermission()

        btn_album.setOnClickListener {
            openGallery()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면
//                    val bitmap: Bitmap
//                    val file = File(currentPhotoPath)
//                    var fileuri = Uri.fromFile(file)
//                    uploadImgName = getName(fileuri)
//                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
//                        bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, fileuri)
//
//                        bmp = bitmap
//
//                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
//                        val decode = ImageDecoder.createSource(a!!.contentResolver, fileuri)
//                        bitmap = ImageDecoder.decodeBitmap(decode)
//
//                        bmp = bitmap
//
//
//                    }
//
//                    if (file.exists()) {
//                        file.delete()
//                    }
//                    if (fileuri != null) {
//                        fileuri = null
//                    }
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
//                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
//                    uploadImgName = getName(currentImageUrl)
//
//                    try {
//                        val bitmap = MediaStore.Images.Media.getBitmap(
//                            a!!.contentResolver,
//                            currentImageUrl
//                        )
//
//                        bmp = bitmap
//
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
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
                Toast.makeText(this@AIActivity, "권한이 혀용 되었습니다.", Toast.LENGTH_SHORT).show()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@AIActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
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

}