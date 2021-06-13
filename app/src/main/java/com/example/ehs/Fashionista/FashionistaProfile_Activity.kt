package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.R
import com.example.ehs.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_fashionista_profile.*

class FashionistaProfile_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fashionista_profile)
        
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_profile) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료
        

        btn_profilePlus.setOnClickListener(
            {
                val intent = Intent(this, ProfilePlus_Activity::class.java)
                startActivity(intent) 
            }) // 액티비티 이동

        val feedList = arrayListOf(
            FashionistaUserProfiles(R.drawable.test_userfeed),
            FashionistaUserProfiles(R.drawable.test_userfeed_b),
            FashionistaUserProfiles(R.drawable.test_userfeed_c),
            FashionistaUserProfiles(R.drawable.test_userfeed_d)
        )

        val gridLayoutManager = GridLayoutManager(applicationContext, 3)
        rv_feed.layoutManager = gridLayoutManager
        rv_feed.setHasFixedSize(true)

        rv_feed.adapter = FashionistaProfileAdapter(feedList)
    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
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
    } // 툴바 뒤로가기 액션 설정 끝
    
    

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
        }
    }
}