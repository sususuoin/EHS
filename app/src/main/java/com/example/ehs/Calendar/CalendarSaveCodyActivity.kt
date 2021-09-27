package com.example.ehs.Calendar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.ehs.Closet.CodyMakeActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_calendar_save_cody.*
class CalendarSaveCodyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_save_cody)
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_savecody) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        var intent = intent
        var arr = intent.getByteArrayExtra("savecody")
        var image : Bitmap = BitmapFactory.decodeByteArray(arr, 0, arr!!.size)

        iv_savecody.setImageBitmap(image)

        tv_selectday.text = AutoCalendar.getSelectday(this)

        btn_savecody.setOnClickListener {
            (CalendarMakeCodyActivity.calendarMakeContext as CalendarMakeCodyActivity).finish()
            (CalendarChoiceActivity.calendarChoiceContext as CalendarChoiceActivity).finish()
            finish()
        }
    }

    /**
     * 툴바 뒤로가기 기능
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}