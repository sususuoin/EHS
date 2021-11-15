package com.example.ehs.AI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.ehs.Camera_choice
import com.example.ehs.Loading
import com.example.ehs.R
import com.example.ehs.ml.ModelUnquant
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_ai.*
import kotlinx.android.synthetic.main.activity_main_ai.*
import kotlinx.android.synthetic.main.activity_main_ai.btn_ai
import kotlinx.android.synthetic.main.activity_main_ai.ll_margin1
import kotlinx.android.synthetic.main.activity_main_ai.ll_margin2
import kotlinx.android.synthetic.main.activity_main_ai.tv_result
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class Main_AIActivity : AppCompatActivity() {
    lateinit var currentPhotoPath: String

    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    var mainAIloading : Loading? = null
    var camera_choice : Camera_choice? = null

    var bitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ai)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_main_ai) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        mainAIloading = Loading(this)
        camera_choice = Camera_choice(this)

        //권한설정
        setPermission()

        btn_choice.setOnClickListener {
            camera_choice!!.init()

            camera_choice!!.btn_choice_camera.setOnClickListener {
                camera_choice!!.finish()
                takeCapture()
                btn_ai.isClickable =true
            }
            camera_choice!!.btn_choice_album.setOnClickListener {
                camera_choice!!.finish()
                openGallery()
                btn_ai.isClickable =true

            }


        }

        btn_ai.setOnClickListener {
            Log.d("평가하기", "버튼클릭")
            if(bitmap==null) {
                Toast.makeText(this, "사진을 선택하시오", Toast.LENGTH_SHORT).show()
            } else{
                Log.d("adfa", "asdfasdF1111")

                GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    mainAIloading!!.init("코디 평가")
                }
                    delay(2500)

                    AIpredict()
                }
            }
        }
    }

    fun AIpredict() {

        Log.d("평가하기", bitmap.toString())

        val resized : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
        val model = ModelUnquant.newInstance(this@Main_AIActivity)

        //input
        val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        val tbuffer = TensorImage.fromBitmap(resized)
        val byteBuffer = tbuffer.buffer
        inputFeature.loadBuffer(byteBuffer)

        //output
        val outputs = model.process(inputFeature)
        val outputFeature = outputs.outputFeature0AsTensorBuffer

        val best = (outputFeature.floatArray[0].div(255.0) * 100).roundToInt() // best값 백분율로
        val worst = (outputFeature.floatArray[1].div(255.0)*100).roundToInt() // worst값 백분율로

        Log.d("점수", best.toString())
        Log.d("점수2", worst.toString())
        var worst2 = 100-worst
        Log.d("점수23", worst.toString())
        if(best>worst){
            tv_result.text = "$best"
            tv_resultscore.isVisible= true
        }else{
            tv_result.text = "$worst2"
            tv_resultscore.isVisible= true

        } // 박수쳐~~~~~~ 호로로로로로로로롤
        //오예 짞짝짞짜까ㅉ까짞 신은정 짱짱맨~~

        btn_ai.isClickable = false
        mainAIloading!!.finish()


        model.close()

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면
                    val bmp: Bitmap
                    val file = File(currentPhotoPath)
                    var fileuri = Uri.fromFile(file)
                    Glide.with(this).load(fileuri).into(iv_aiImg)

                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, fileuri)
                        bitmap = Bitmap.createScaledBitmap(bmp!!, 300, 400, true)
                        Log.d("zz카메라", bmp.toString())

                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, fileuri)
//                        val decode = ImageDecoder.createSource(this.contentResolver, fileuri)
//                        bmp = ImageDecoder.decodeBitmap(decode)
                        bitmap = Bitmap.createScaledBitmap(bmp!!, 300, 400, true)
                        Log.d("zz카메라", bmp.toString())

                    }
                    savePhoto(bitmap!!)

//                    iv_aiImg.setImageBitmap(bitmap)
                    if (file.exists()) {
                        file.delete()
                    }
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    iv_aiImg.setImageResource(0)
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴

                    iv_aiImg.setImageURI(currentImageUrl)
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, currentImageUrl)
                }
            }
        }
        tv_result.text = ""

        ll_margin1.isVisible = false
        btn_choice.isVisible = false

        ll_margin2.isVisible = true
        iv_aiImg.isVisible = true

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
     * 카메라 오픈 함수
     */
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
     * 이미지 파일 생성
     */
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PNG_${timestamp}_", ".png", storageDir)
            .apply { currentPhotoPath = absolutePath }
    }


    /**
     * 테드 퍼미션 설정
     */
    private fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() { // 설정해놓은 위험 권한들이 허용되었을 경우 이곳을 수행함.
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@Main_AIActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
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
     * 갤러리에 저장
     */
    private fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Omonemo/" // 사진폴더로 저장하기 위한 경로 선언
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.png"
        val folder = File(folderPath)
        if(!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir() // make diretory 줄임말로 해당 경로에 폴더를 자동으로 새로 만든다
        }
        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

    }

    /**
     * 툴바 뒤로가기 기능
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
    }

}