package com.example.ehs.Calendar

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_calendar_makecody.*
import java.io.ByteArrayOutputStream

class CalendarMakeCodyActivity : AppCompatActivity() , View.OnTouchListener{

    var clickCount = 0

    private var Position_X = 0
    private var Position_Y = 0
    var startTime: Long = 0

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    private lateinit var iv : ImageView // 우선 이미지 만들어 놓기 나중에 이걸 터치 된 걸로 바꿔야 하는데 지금은 모름

    lateinit var savecody : Bitmap

    companion object {
        var calendarMakeContext: Context? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_makecody)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_makecody) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료
        var StringList = AutoCalendar.getCalendarchoiceImg(this)
        calendarMakeContext = this


        Log.d("하하", StringList.size.toString())
        for(i in 0 until StringList.size) {
            Add_image(AutoLogin.StringToBitmap(StringList[i])!!)
        }

        btn_makecody.setOnClickListener {

            RL_makecody.setDrawingCacheEnabled(true)
            RL_makecody.buildDrawingCache()

            //조합한 코디를 캡쳐하여 비트맵으로 변경
            savecody = RL_makecody.getDrawingCache()

            var intent = Intent(this@CalendarMakeCodyActivity, CalendarSaveCodyActivity::class.java)
            var stream = ByteArrayOutputStream()
            savecody.compress(Bitmap.CompressFormat.PNG, 100, stream)
            var byteArray: ByteArray = stream.toByteArray()
            intent.putExtra("savecody", byteArray)
            startActivity(intent)
        }

        // 스케일제스쳐 디텍터 인스턴스s
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        clickCount = 0

    }

    /**
     * 툴바 뒤로가기 버튼 기능 구현
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Add_image(newclothes: Bitmap) { // 이미지 추가

        try {
            // 만들어진 Bitmap 객체를 이미지뷰에 표시.
            iv = ImageView(this)
            iv!!.setImageBitmap(newclothes)
            val layoutParams = RelativeLayout.LayoutParams(500, 500)
            iv!!.layoutParams = layoutParams
            RL_makecody!!.addView(iv, layoutParams)

            iv!!.setOnTouchListener(this) // 이미지 터치 리스너
            mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener()) // 이미지 크기 조정 리스너 등록

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val X = event.rawX.toInt()
        val Y = event.rawY.toInt()
        val pointerCount = event.pointerCount
        iv = view as ImageView // 이미지 크기 조정을 위해 이미지 클릭 시 클릭한 이미지로 iv를 바꿔줌

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
                    val builder = AlertDialog.Builder(this@CalendarMakeCodyActivity)
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
                    view.y = (event.rawY - view.height / 0.5).toFloat()
                    view.x = (event.rawX - view.width / 1.8).toFloat()
                }
            }
        }

// Schedules a repaint for the root Layout.
        RL_makecody!!.invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() { // 이미지 크기 조정을 위한 클래스
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