package com.example.ehs.Home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import com.example.ehs.ml.ColorModel
import kotlinx.android.synthetic.main.activity_color_recommend.*
import kotlinx.android.synthetic.main.activity_pro_recommend.*
import kotlinx.android.synthetic.main.activity_pro_recommend.tv_userid
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ColorRecommendActivity : AppCompatActivity() {

    lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_recommend)
        userId = AutoLogin.getUserId(this@ColorRecommendActivity)
        tv_userid.text = userId

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_prorecommend) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.colortest)
        val resized1 : Bitmap = Bitmap.createScaledBitmap(bitmap!!, 224, 224, true)
        val model = ColorModel.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

        val tbuffer1 = TensorImage.fromBitmap(resized1)
        val byteBuffer1 = tbuffer1.buffer

        inputFeature0.loadBuffer(byteBuffer1)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val best1 = outputFeature0.floatArray[1].div(255.0)*100 // best값 백분율로

        Log.d("best11122", best1.toString())

        var asdf = ArrayList<Double>()
        for(i in 0 until outputFeature0.floatArray.size) {
            asdf.add(outputFeature0.floatArray[i].div(255.0)*100)
        }

        Log.d("best11122222222222", asdf.toString())

        iv_colorcody1.setImageBitmap(bitmap)


        // Releases model resources if no longer used.
        model.close()


    }

    /**
     * 툴바 뒤로가기 버튼 기능 구현
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