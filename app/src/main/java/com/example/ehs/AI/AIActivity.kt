package com.example.ehs.AI

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.ehs.Login.RegisterActivity
import com.example.ehs.R
import com.example.ehs.ml.ModelUnquant
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_ai.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.util.*


class AIActivity : AppCompatActivity() {


    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2


    var bitmap : Bitmap? = null
    lateinit var airesult :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai)
        btn_register.isVisible = false

        //권한설정
        setPermission()

        btn_album.setOnClickListener {
            openGallery()
        }

        btn_ai.setOnClickListener {

            Log.d("평가하기", "버튼클릭")
            AIpredict()
        }

        btn_register.setOnClickListener {
            Log.d("회원가입 화면으로 이동", "버튼클릭")
            Log.d("333", airesult)
            val registerIntent = Intent(this, RegisterActivity::class.java) // 인텐트를 생성
            registerIntent.putExtra("airesult", airesult)
            startActivity(registerIntent)
        }
    }

    fun AIpredict() {
        if(bitmap==null) {
            Toast.makeText(this, "사진을 선택하시오", Toast.LENGTH_SHORT).show()
        }
        else {
            Log.d("평가하기", bitmap.toString())

            val resized : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)


            val model = ModelUnquant.newInstance(this@AIActivity)

            //input
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
            val tbuffer = TensorImage.fromBitmap(resized)
            val byteBuffer = tbuffer.buffer
            inputFeature0.loadBuffer(byteBuffer)

            //output
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val best = outputFeature0.floatArray[0].div(255.0)*100 // best값 백분율로
            val worst = outputFeature0.floatArray[1].div(255.0)*100 // worst값 백분율로
            Log.d("best", best.toString())
            Log.d("worst", worst.toString())


            if(best>worst){
                tv_result.text = "best:"+best.toString()
            }else{
                tv_result.text = "worst:"+worst.toString()
            } // 박수쳐~~~~~~ 호로로로로로로로롤
            //오예 짞짝짞짜까ㅉ까짞 신은정 짱짱맨~~


            airesult = best.toString()
            btn_ai.isVisible = false
            btn_register.isVisible = true
            Log.d("이것이 베스트점수 결과로다", airesult)


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



                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
//                    uploadImgName = getName(currentImageUrl)

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            currentImageUrl
                        )

                        tv_result.text=""
                        btn_ai.isVisible = true
                        btn_register.isVisible = false

                        iv_aiImg1.setImageBitmap(bitmap)


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
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