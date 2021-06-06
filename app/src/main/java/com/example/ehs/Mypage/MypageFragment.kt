package com.example.ehs.Mypage

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.Login.AutoLogin
import com.example.ehs.Login.LoginActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*

class MypageFragment : Fragment() {
    private var a: Context? = null


    lateinit var tv_id : TextView
    lateinit var tv_pw : TextView
    lateinit var tv_name : TextView
    lateinit var tv_email : TextView
    lateinit var tv_birth : TextView
    lateinit var tv_gender : TextView
    lateinit var tv_level : TextView


    lateinit var modifybtn: ImageButton

    companion object {
        const val TAG : String = "마이페이지 프레그먼트"
        fun newInstance() : MypageFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return MypageFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MypageFragment - onCreate() called")
    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MypageFragment - onAttach() called")

        if (context is Activity) {
            a = context
        }
    }


    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "MypageFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_mypage, container, false)

        modifybtn = view.findViewById(R.id.btn_modify)
        modifybtn.setOnClickListener{
            activity?.let{
                val intent = Intent(context, UserModifyActivity::class.java)
                startActivity(intent) }
        }

        view.btn_logout.setOnClickListener { view ->
            Log.d("클릭!!", "로그아웃 버튼 클릭!!")
            var logoutalert = AlertDialog.Builder(a!!)
            logoutalert.setTitle("로그아웃 확인창")
            logoutalert.setMessage("'Onmonemo'에서 로그아웃 하시겠습니까?")

            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when (p1) {
                        // 확인 버튼 클릭 시
                        DialogInterface.BUTTON_POSITIVE ->
                        {
                            // 로그아웃 설정
                            AutoLogin.clearUser(a!!)
                            val intent = Intent(a, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
            logoutalert.setPositiveButton("확인",listener)
            logoutalert.setNegativeButton("취소", null)
            logoutalert.show()
        }

        tv_id = view.findViewById(R.id.tv_id);
        tv_pw = view.findViewById(R.id.tv_pw);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_birth = view.findViewById(R.id.tv_birth);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_level = view.findViewById(R.id.tv_level);

        var userId = AutoLogin.getUserId(a!!)
        var userPw = AutoLogin.getUserId(a!!)
        var userName = AutoLogin.getUserId(a!!)
        var userEmail = AutoLogin.getUserId(a!!)
        var userBirth = AutoLogin.getUserId(a!!)
        var userGender = AutoLogin.getUserId(a!!)
        var userLevel = AutoLogin.getUserId(a!!)

        //fragment1의 TextView에 전달 받은 text 띄우기
        tv_id.text = userId
        tv_pw.text = userPw
        tv_name.text = userName
        tv_email.text = userEmail
        tv_birth.text = userBirth
        tv_gender.text = userGender
        tv_level.text = userLevel


        return view
    }



}