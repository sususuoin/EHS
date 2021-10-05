package com.example.ehs.AI

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ehs.R
import com.example.ehs.ml.ModelUnquant
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main_ai.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*
import kotlin.math.roundToInt


class Main_AIActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2


    var bitmap : Bitmap? = null

    lateinit var airesult :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_ai)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
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


        //권한설정
        setPermission()

        btn_album.setOnClickListener {
            openGallery()
        }

        btn_ai.setOnClickListener {

            Log.d("평가하기", "버튼클릭")
            AIpredict()
        }

    }

    @SuppressLint("SetTextI18n")
    fun AIpredict() {
        if(bitmap==null) {
            Toast.makeText(this, "사진을 선택하시오", Toast.LENGTH_SHORT).show()
        }
        else {
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
                tv_result.text = "당신의 점수는~~?   $best"
            }else{
                tv_result.text = "당신의 점수는~~?   $worst2"
            } // 박수쳐~~~~~~ 호로로로로로로로롤
            //오예 짞짝짞짜까ㅉ까짞 신은정 짱짱맨~~




            btn_ai.isClickable = false



            model.close()


        }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면

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