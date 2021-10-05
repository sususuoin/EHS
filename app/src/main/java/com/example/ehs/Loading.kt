package com.example.ehs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.widget.ImageView


class Loading(context: Context) : Dialog(context) {
    private val context = Dialog(context)   //부모 액티비티의 context 가 들어감
    lateinit var imageView: ImageView
    var animationDrawable: AnimationDrawable? = null


    fun asdf() {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.show()
        start()
    }

    fun start() {
        context.requestWindowFeature(Window.FEATURE_NO_TITLE)   //타이틀바 제거
        context.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        context.setContentView(R.layout.loading)     //다이얼로그에 사용할 xml 파일을 불러옴
        context.setCancelable(false)    //다이얼로그의 바깥 화면을 눌렀을 때 다이얼로그가 닫히지 않도록 함

        imageView = context.findViewById(R.id.progresss)
        imageView.setBackgroundResource(R.drawable.loading_item)
        animationDrawable = imageView.background as AnimationDrawable?
        Log.d("로딩1",imageView.toString())
        animationDrawable!!.start()
        Log.d("로딩1",animationDrawable.toString())

    }

    fun finish () {
        Log.d("로딩1", "코가막힘 ㅜㅜ")
        context.dismiss()
    }

}

//class MainActivity : AppCompatActivity() {
//    var customProgress: CustomProgress? = null
//    var start: Button? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        start = findViewById(R.id.start)
//        customProgress = CustomProgress(this@MainActivity)
//        start.setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//
//                // CustomProgress Start
//                customProgress.show()
//
//                //서브화면으로 이동
////                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
////                startActivity(intent);
//                start.postDelayed(Runnable { //CustomProgress Close
//                    customProgress.dismiss()
//                }, 5000)
//            }
//        })
//    }
//
//    override fun onResume() {
//        super.onResume()
//        /**
//         * CustomProgress Close
//         * 다시 화면에 들어오면 프로그레스바를 종료.
//         */
//        customProgress.dismiss()
//    }
//}