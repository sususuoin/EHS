package com.example.ehs.Closet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.ehs.BottomSheet.BottomSheet_fashion
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_cody_save.*

class CodySaveActivity : AppCompatActivity(), BottomSheet_fashion.BottomSheetButtonClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cody_save)

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_codysave) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        // 바텀시트 열기
        tv_fashion.setOnClickListener {
            val bottomSheet = BottomSheet_fashion()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
        // 완료하기 버튼 클릭 시
        btn_complete_cody.setOnClickListener{
            
        }


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
    
    // 바텀시트에서 선택한 항목 보여주기
    override fun onFashionButtonClicked(text: String) {
        tv_fashion.text = text
    }
}