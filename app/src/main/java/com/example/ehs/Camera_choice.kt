package com.example.ehs

import android.app.Activity.*
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button

class Camera_choice(context: Context) : Dialog(context) {
    private val context = Dialog(context)   //부모 액티비티의 context 가 들어감
    lateinit var btn_choice_camera: Button
    lateinit var btn_choice_album: Button

    fun init() {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.show()
        start()
    }

    fun start() {

        context.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))

        context.setContentView(R.layout.camera_choice)     //다이얼로그에 사용할 xml 파일을 불러옴
        context.setCancelable(false)
        context.setCanceledOnTouchOutside(true)

        val params: WindowManager.LayoutParams? = context.window?.attributes
        params!!.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        context.window!!.attributes = params
        context.window!!.setGravity(Gravity.CENTER)


        btn_choice_camera = context.findViewById(R.id.btn_choice_camera)
        btn_choice_album = context.findViewById(R.id.btn_choice_album)


    }


    fun finish () {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.dismiss()
    }

}
