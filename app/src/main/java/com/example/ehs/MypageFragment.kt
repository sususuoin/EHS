package com.example.ehs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        view.btn_logout.setOnClickListener { view ->
            Log.d("클릭!!", "로그아웃 버튼 클릭!!")
            AutoLogin.clearUser(a!!)
            val intent = Intent(a, LoginActivity::class.java)
            startActivity(intent)

        }


        tv_id = view.findViewById(R.id.tv_id);
        tv_pw = view.findViewById(R.id.tv_pw);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_birth = view.findViewById(R.id.tv_birth);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_level = view.findViewById(R.id.tv_level);

        //MainActivity에서 전달한 번들 저장
        val bundle = getArguments()
        //번들 안의 텍스트 불러오기
        val userId = bundle?.getString("userId")
        if (userId != null) {
            Log.d("아이디가 나올때가", userId)
        }else
            Log.d("아이디가 나올때가", "안나오네 ;;;진짜죽어버려")
        val userPw = bundle?.getString("userPw")
        val userName = bundle?.getString("userName")
        val userEmail = bundle?.getString("userEmail")
        val userBirth = bundle?.getString("userBirth")
        val userGender = bundle?.getString("userGender")
        val userLevel = bundle?.getString("userLevel")

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