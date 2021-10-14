package com.example.ehs


import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.android.synthetic.main.fragment_closet.*

class MainLoadingActivity : Activity() {
    val TAG: String = "로딩화면"

    private var backKeyPressedTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            this.finishAffinity()
            System.runFinalization()
            System.exit(0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_loading)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        Log.d("로딩화면", "시작")
        startLoading()
    }

    fun startLoading() {
        val handler = Handler()
        handler.postDelayed(Runnable { finish() }, 3000)
        Log.d("로딩화면", "끝")
    }



}