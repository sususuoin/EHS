package com.example.ehs

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    lateinit var text: TextView

    companion object {
        const val TAG : String = "홈 프레그먼트"
        fun newInstance() : HomeFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return HomeFragment()
        }
    }

    // 프래그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
    }
    // 프래그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "HomeFragment - onAttach() called")
    }

    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "HomeFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        text = view.findViewById(R.id.text);

        text.setOnClickListener {
            activity?.let{
                val intent = Intent(context, ImageActivity::class.java)
                startActivity(intent) }
        }


        return view
    }

}