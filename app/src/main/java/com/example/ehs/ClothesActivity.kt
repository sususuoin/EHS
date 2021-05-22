package com.example.ehs

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_clothes.*

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ClothesActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim)}

    private var clicked = false


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2
    lateinit var currentPhotoPath: String // 문자열 형태의 사진 경로 값 (초기 값을 null로 시작하고 싶을 때)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes)


        // + 버튼 클릭 시
        btn_add.setOnClickListener{
            onAddButtonClicked()
        }

        btn_gallery.setOnClickListener{
            openGallery()
            onAddButtonClicked()
        }
        tv_gallery.setOnClickListener {
            openGallery()
            onAddButtonClicked()

        }

        btn_camera.setOnClickListener {
            takeCapture() // 기본 카메라 앱을 실행하여 사진 촬영
            onAddButtonClicked()

        }
        tv_camera.setOnClickListener {
            takeCapture()
            onAddButtonClicked()

        }
    }

    /**
     * 플러팅 버튼 이벤트 처리
     */

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.visibility = View.VISIBLE
            btn_camera.visibility = View.VISIBLE
            tv_camera.visibility = View.VISIBLE
            tv_gallery.visibility = View.VISIBLE
        }else {
            btn_gallery.visibility = View.INVISIBLE
            btn_camera.visibility = View.INVISIBLE
            tv_camera.visibility = View.INVISIBLE
            tv_gallery.visibility = View.INVISIBLE
            btn_add.backgroundTintList = AppCompatResources.getColorStateList(this, R.color.white)
        }
    }
    fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.startAnimation(fromBottom)
            btn_camera.startAnimation(fromBottom)
            tv_gallery.startAnimation(fromBottom)
            tv_camera.startAnimation(fromBottom)
            btn_add.startAnimation(rotateOpen)
        } else {
            btn_gallery.startAnimation(toBottom)
            btn_camera.startAnimation(toBottom)
            tv_gallery.startAnimation(toBottom)
            tv_camera.startAnimation(toBottom)
            btn_add.startAnimation(rotateClose)
        }
    }

    fun setClickable(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.isClickable = true
            btn_camera.isClickable = true
            tv_camera.isClickable = true
            tv_gallery.isClickable = true
        } else {
            btn_gallery.isClickable = false
            btn_camera.isClickable = false
            tv_camera.isClickable = false
            tv_gallery.isClickable = false
        }
    }

    fun takeCapture() {
        // 기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                } catch(ex: IOException){
                    null
                }
                photoFile?.also{
                    // photoURI는 원본파일 저장하는 경로
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
     * 이미지 파일 생성
     */
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
            .apply { currentPhotoPath = absolutePath }
    }



    // startAcitivityForResult를 통해서 기본 카메라 앱으로부터 받아온 사진 결과 값
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면
                    val bitmap: Bitmap
                    val file = File(currentPhotoPath)
                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                        img_picture.setImageBitmap(bitmap)
                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(
                            this.contentResolver,
                            Uri.fromFile(file)
                        )
                        bitmap = ImageDecoder.decodeBitmap(decode)
                        img_picture.setImageBitmap(bitmap)
                    }
                    savePhoto(bitmap)
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    val currentImageUrl : Uri? = data?.data // data의 data형태로 들어옴
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                        img_picture.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * 갤러리에 저장
     */
    private fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Omonemo/" // 사진폴더로 저장하기 위한 경로 선언
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir() // make diretory 줄임말로 해당 경로에 폴더를 자동으로 새로 만든다
        }
        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.close()
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }




}