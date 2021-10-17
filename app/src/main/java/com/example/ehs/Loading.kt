package com.example.ehs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView


class Loading(context: Context) : Dialog(context) {
    private val context = Dialog(context)   //부모 액티비티의 context 가 들어감
    lateinit var imageView: ImageView
    var animationDrawable: AnimationDrawable? = null


    fun init() {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.show()
        start()
    }

    fun start() {

        context.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        context.window!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CC000000")))

        context.setContentView(R.layout.loading)     //다이얼로그에 사용할 xml 파일을 불러옴
        context.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함
        context.setCanceledOnTouchOutside(false)

        val params: WindowManager.LayoutParams? = context.window?.attributes
        params!!.width = WindowManager.LayoutParams.MATCH_PARENT
        params!!.height = WindowManager.LayoutParams.MATCH_PARENT
        context.window!!.attributes = params
        context.window!!.setGravity( Gravity.CENTER )

        imageView = context.findViewById(R.id.progresss)
        imageView.setBackgroundResource(R.drawable.loading_item)
        animationDrawable = imageView.background as AnimationDrawable?
        Log.d("로딩1", imageView.toString())
        animationDrawable!!.start()
        Log.d("로딩1", animationDrawable.toString())

    }

    fun finish () {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.dismiss()
    }

}
