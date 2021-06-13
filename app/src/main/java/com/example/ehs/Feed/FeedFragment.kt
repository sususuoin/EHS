package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FeedFragment : Fragment() {
    private var a: Activity? = null
    val feedsList = mutableListOf<Feeds>()

    companion object {
        const val TAG : String = "피드 프레그먼트"
        fun newInstance() : FeedFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return FeedFragment()
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
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "FeedFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "FeedFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        view.tv_community.setOnClickListener {
            Log.d("FeedFragment", "커뮤니티로 이동")
            (activity as MainActivity?)!!.replaceFragment(CommunityFragment.newInstance())
        }



        var fashin = Feeds("gkgk","gkgk")
        var hi = Feeds("jj","안냥")
        feedsList.add(fashin)
        feedsList.add(hi)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = GridLayoutManager(a, 2)
        rv_userfeed.layoutManager = gridLayoutManager


        val adapter = FeedsListAdapter(feedsList)
        rv_userfeed.adapter = adapter
        //recylerview 이거 fashionista.xml에 있는 변수
    }





}