package com.example.ehs.Closet

import android.app.AlertDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.Toolbar
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_cody_make.*
import java.io.InputStream

class CodyMakeActivity : AppCompatActivity(), View.OnTouchListener {
    var clickCount = 0
    private var RootLayout: ViewGroup? = null
    private var Position_X = 0
    private var Position_Y = 0
    var startTime: Long = 0

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    private var iv : ImageView? = null // 우선 이미지 만들어 놓기 나중에 이걸 터치 된 걸로 바꿔야 하는데 지금은 모름

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_make)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById<Toolbar>(R.id.toolbar_codymake)
        RootLayout = findViewById<View>(R.id.ll_codymake) as ViewGroup
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        // 옷 추가 버튼 클릭
        btn_addclothes.setOnClickListener { Add_Image() }

        // 스케일제스쳐 디텍터 인스턴스
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        clickCount = 0

    }

    private fun Add_Image() { // 이미지 추가
        // 애셋매니저
        val am = resources.assets
        var `is`: InputStream? = null

        try {
            // 애셋 폴더에 저장된 field.png 열기.
            `is` = am.open("cody.jpg")

            // 입력스트림 is를 통해 field.png 을 Bitmap 객체로 변환.
            val bm = BitmapFactory.decodeStream(`is`)

            // 만들어진 Bitmap 객체를 이미지뷰에 표시.
            iv = ImageView(this)
            iv!!.setImageBitmap(bm)
            val layoutParams = RelativeLayout.LayoutParams(400, 400)
            iv!!.layoutParams = layoutParams
            RootLayout!!.addView(iv, layoutParams)

            iv!!.setOnTouchListener(this)
            mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

            `is`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (`is` != null) {
            try {
                `is`.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


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

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val X = event.rawX.toInt()
        val Y = event.rawY.toInt()
        val pointerCount = event.pointerCount
        mScaleGestureDetector!!.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                Position_X = X - layoutParams.leftMargin
                Position_Y = Y - layoutParams.topMargin
            }
            MotionEvent.ACTION_UP -> if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            } else { // 두 번 누르면 삭제
                if (System.currentTimeMillis() - startTime < 200) {
                    val builder = AlertDialog.Builder(this@CodyMakeActivity)
                    builder.setMessage("선택하신 옷을 삭제하시겠습니까?")
                    builder.setPositiveButton("삭제") { dialog, which -> view.visibility = View.GONE }
                    builder.setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                startTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
            // 이미지 움직이기
            MotionEvent.ACTION_MOVE -> {
                if (pointerCount == 1) {
                    view.y = (event.rawY - view.height / 1).toFloat()
                    view.x = (event.rawX - view.width / 2).toFloat()
                }
            }
        }

// Schedules a repaint for the root Layout.
        RootLayout!!.invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            // ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
            mScaleFactor *= scaleGestureDetector.scaleFactor

            // 최대 10배, 최소 10배 줌 한계 설정
            mScaleFactor = Math.max(0.1f,
                Math.min(mScaleFactor, 10.0f))

            // 이미지뷰 스케일에 적용
            iv!!.scaleX = mScaleFactor
            iv!!.scaleY = mScaleFactor
            return true
        }
    }
}