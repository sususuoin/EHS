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
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.ehs.Closet.ClosetFragment
import com.example.ehs.Loading
import com.example.ehs.Login.RegisterActivity
import com.example.ehs.R
import com.example.ehs.ml.ModelUnquant
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_ai.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*


class AIActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    var bitmap1 : Bitmap? = null
    var bitmap2 : Bitmap? = null
    var bitmap3 : Bitmap? = null

    lateinit var airesult :String

    var aIloading : Loading? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_ai) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        btn_register.isVisible = false

        //권한설정
        setPermission()

        aIloading = Loading(this)

        btn_album.setOnClickListener {
            openGallery()
        }

        btn_ai.setOnClickListener {

            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    aIloading!!.init("코디 평가")
                }
                delay(2500)

                Log.d("평가하기", "버튼클릭")
                AIpredict()
            }


        }

        btn_register.setOnClickListener {
            Log.d("회원가입 화면으로 이동", "버튼클릭")
            Log.d("333", airesult)
            val registerIntent = Intent(this, RegisterActivity::class.java) // 인텐트를 생성
            registerIntent.putExtra("airesult", airesult)
            startActivity(registerIntent)
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    fun AIpredict() {
        if(bitmap1==null || bitmap2==null || bitmap3==null) {
            Toast.makeText(this, "사진 3개를 선택하시오", Toast.LENGTH_SHORT).show()
        }
        else {
            Log.d("평가하기", bitmap1.toString())

            val resized1 : Bitmap = Bitmap.createScaledBitmap(bitmap1!!, 224, 224, true)
            val resized2 : Bitmap = Bitmap.createScaledBitmap(bitmap2!!, 224, 224, true)
            val resized3 : Bitmap = Bitmap.createScaledBitmap(bitmap3!!, 224, 224, true)


            val model = ModelUnquant.newInstance(this@AIActivity)

            //input
            val inputFeature1 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            val inputFeature2 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            val inputFeature3 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

            val tbuffer1 = TensorImage.fromBitmap(resized1)
            val tbuffer2 = TensorImage.fromBitmap(resized2)
            val tbuffer3 = TensorImage.fromBitmap(resized3)

            val byteBuffer1 = tbuffer1.buffer
            val byteBuffer2 = tbuffer2.buffer
            val byteBuffer3 = tbuffer3.buffer

            inputFeature1.loadBuffer(byteBuffer1)
            inputFeature2.loadBuffer(byteBuffer2)
            inputFeature3.loadBuffer(byteBuffer3)

            //output
            val outputs1 = model.process(inputFeature1)
            val outputs2 = model.process(inputFeature2)
            val outputs3 = model.process(inputFeature3)

            val outputFeature1 = outputs1.outputFeature0AsTensorBuffer
            val outputFeature2 = outputs2.outputFeature0AsTensorBuffer
            val outputFeature3 = outputs3.outputFeature0AsTensorBuffer

            val best1 = outputFeature1.floatArray[0].div(255.0)*100 // best값 백분율로
            val worst1 = outputFeature1.floatArray[1].div(255.0)*100 // worst값 백분율로

            val best2 = outputFeature2.floatArray[0].div(255.0)*100 // best값 백분율로
            val worst2 = outputFeature2.floatArray[1].div(255.0)*100 // worst값 백분율로

            val best3 = outputFeature3.floatArray[0].div(255.0)*100 // best값 백분율로
            val worst3 = outputFeature3.floatArray[1].div(255.0)*100 // worst값 백분율로

            Log.d("best111", best1.toString())
            Log.d("worst1111", worst1.toString())
            Log.d("best2222", best2.toString())
            Log.d("worst2222", worst2.toString())
            Log.d("best33333", best3.toString())
            Log.d("worst3333", worst3.toString())


            val best = (best1 + best2 + best3)/3
            Log.d("베스트 평균", best.toString())

            val worst = (worst1 + worst2 + worst3)/3
            Log.d("베스트 평균", best.toString())

            if(best>worst){
                tv_result.text = "best:$best"
            }else{
                tv_result.text = "worst:$worst"
            } // 박수쳐~~~~~~ 호로로로로로로로롤
            //오예 짞짝짞짜까ㅉ까짞 신은정 짱짱맨~~


            airesult = best.toString()
            btn_ai.isVisible = false
            btn_register.isVisible = true
            Log.d("이것이 베스트점수 결과로다", airesult)

            aIloading!!.finish()
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

                    iv_aiImg1.setImageResource(0)
                    iv_aiImg2.setImageResource(0)
                    iv_aiImg3.setImageResource(0)

                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
                    val clipData: ClipData? = data?.clipData

                    if (clipData != null) {
                        for (i in 0..2) {
                            if (i < clipData.itemCount) {
                                val urione = clipData.getItemAt(i).uri
                                when (i) {
                                    0 -> {
                                        bitmap1 = MediaStore.Images.Media.getBitmap(this.contentResolver, urione)
                                        iv_aiImg1.setImageBitmap(Bitmap.createScaledBitmap(bitmap1!!, 400, 533, true))
                                    }
                                    1 -> {
                                        bitmap2 = MediaStore.Images.Media.getBitmap(this.contentResolver, urione)
                                        iv_aiImg2.setImageBitmap(Bitmap.createScaledBitmap(bitmap2!!, 400, 533, true))
                                    }
                                    2 -> {
                                        bitmap3 = MediaStore.Images.Media.getBitmap(this.contentResolver, urione)
                                        iv_aiImg3.setImageBitmap(Bitmap.createScaledBitmap(bitmap3!!, 400, 533, true))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "취소하였습니다.", Toast.LENGTH_SHORT).show()
        }

        tv_result.text = ""

        ll_margin1.isVisible = false
        btn_album.isVisible = false

        ll_margin2.isVisible = true
        ll_img.isVisible = true

        btn_ai.isVisible = true
        btn_register.isVisible = false

    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
//        startActivityForResult(intent, REQUEST_OPEN_GALLERY)


        val intent = Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
        //사진을 여러개 선택할수 있도록 한다
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_OPEN_GALLERY)
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