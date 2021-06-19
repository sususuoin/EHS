package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.Fashionista.ProfilePlus_Activity
import com.example.ehs.MainActivity
import com.example.ehs.Mypage.UserModifyActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_community.view.*
import kotlinx.android.synthetic.main.fragment_community_item.*
import kotlinx.android.synthetic.main.fragment_community_item.view.*
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed.view.tv_community

class CommunityFragment : Fragment() {
    private var a: Activity? = null
    val communityList = mutableListOf<Community>()
    lateinit var btncommunityEdit: ImageButton




    companion object {
        const val TAG : String = "커뮤니티 프레그먼트"
        fun newInstance() : CommunityFragment { // newInstance()라는 함수를 호출하면 CommunityFragment를 반환함
            return CommunityFragment()
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
        Log.d(TAG, "CommunityFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        btncommunityEdit = view!!.findViewById(R.id.btn_communityEdit)
        btncommunityEdit.setOnClickListener{
            activity?.let{
                val intent = Intent(context, CommunityEditActivity::class.java)
                startActivity(intent) }
        }

//------------------------------------------------------------------다시
//        view.btn_C_heart.setOnClickListener {
//            btn_C_heart.background = R.drawable.ic_heart
//        }
        //------------------------------------------------------------------

        view.tv_feed2.setOnClickListener {
            Log.d("CommunityFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FeedFragment.newInstance())
        }

        var one = Community("EHS","오늘 뭐입지~?")
        var two = Community("jj","방가방가 햄토리 친구드루")
        communityList.add(one)
        communityList.add(two)
        communityList.add(one)
        communityList.add(one)
        communityList.add(one)
        communityList.add(two)
        communityList.add(one)
        communityList.add(two)
        communityList.add(one)
        communityList.add(two)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = LinearLayoutManager(a)
        rv_community.layoutManager = gridLayoutManager


        val adapter = CommunityListAdapter(communityList)
        rv_community.adapter = adapter
        //recylerview 이거 fashionista.xml에 있는 변수
    }

}