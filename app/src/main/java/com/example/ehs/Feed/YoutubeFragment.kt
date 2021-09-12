package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import kotlinx.android.synthetic.main.fragment_youtube.view.*
import java.util.*


class YoutubeFragment : Fragment() {
    private var a: Activity? = null

    var listener: YouTubePlayer.OnInitializedListener? = null
    var youtubeView: YouTubePlayerView? = null

    companion object {
        const val TAG : String = "유튜브 프레그먼트"
        fun newInstance() : YoutubeFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return YoutubeFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "YOUTUBEFragment - onCreate() called")

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "YOUTUBEFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "YOUTUBEFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_youtube, container, false)

        view.tv_community.setOnClickListener {
            Log.d("FeedFragment", "커뮤니티로 이동")
            (activity as MainActivity?)!!.replaceFragment(CommunityFragment.newInstance())
        }

        val contents: MutableList<Youtube> = ArrayList<Youtube>()

        contents.add(Youtube("ieZ_qkyhLwU"))
        contents.add(Youtube("rCeM57e2BfU"))

        view.main_recycler_view.layoutManager = GridLayoutManager(a, 1)
        view.main_recycler_view.itemAnimator = DefaultItemAnimator()
        view.main_recycler_view.adapter = YoutubeListAdapter(contents)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }




}