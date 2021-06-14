package com.example.ehs.Fashionista

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment() {

    companion object {
        const val TAG : String = "커뮤니티 프레그먼트"
        fun newInstance() : FavoriteFragment { // newInstance()라는 함수를 호출하면 CommunityFragment를 반환함
            return FavoriteFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FeedFragment - onCreate() called")

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "FeedFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "FavoriteFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        view.tv_fashionista2.setOnClickListener {
            Log.d("FashionistaFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FashionistaFragment.newInstance())
        }
        // 추천배너 클릭 시
        view.btn_recommend.setOnClickListener {
            val intent = Intent(context, ProRecommendActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}