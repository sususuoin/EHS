package com.example.ehs.Fashionista

import android.R.attr.bitmap
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Fashionista.FashionistaFragment.Companion.dialog
import com.example.ehs.Home.HomeFragment
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class FavoriteFragment : Fragment() {

    private var a: Activity? = null
    val favoriteList = mutableListOf<Favorite>()
    lateinit var userId: String

    var favoriteuserIdArr = ArrayList<String>()
    var favoriteuserHashTagArr = ArrayList<String>()
    var favoriteuserProImgArr = ArrayList<String>()
    var adapter = FavoriteListAdapter(favoriteList)

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

        dialog.dismiss()
        userId = AutoLogin.getUserId(a!!)

        favoriteuserIdArr = AutoPro.getFavoriteuserId2(a!!)
        favoriteuserHashTagArr = AutoPro.getFavoriteuserHashTag(a!!)
        favoriteuserProImgArr = AutoPro.getFavoriteuserImg(a!!)

        Log.d("기분...", favoriteuserIdArr.toString())
        if(favoriteuserIdArr.size!=0) {
            var favoriteuserProfile : Bitmap?

            for(i in 0 until favoriteuserIdArr.size) {
                favoriteuserProfile = AutoLogin.StringToBitmap(favoriteuserProImgArr[i], 100, 100)
                val resizedBmp = Bitmap.createScaledBitmap(favoriteuserProfile!!, 100, 100, true)
                var favorite = Favorite(favoriteuserIdArr[i], '#' + favoriteuserHashTagArr[i], resizedBmp)

                favoriteList.add(favorite)
                favoriteuserProfile == null
            }
        }

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "FavoriteFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "FavoriteFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        view.tv_fashionista2.setOnClickListener {
            Log.d("FavoriteFragment", "패셔니스타로 이동")
            (activity as MainActivity?)!!.replaceFragment(FashionistaFragment.newInstance())
        }

        view.tv_favorite.setOnClickListener {
            Log.d("FashionistaFragment", "즐겨찾기로 두두둥2")
            (activity as MainActivity?)!!.replaceFragment(newInstance())
        }


        // 추천배너 클릭 시
        view.btn_recommend.setOnClickListener {
            (HomeFragment.homeContext as HomeFragment).recommend()
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = LinearLayoutManager(a)
        rv_favorite.layoutManager = gridLayoutManager


        rv_favorite.adapter = adapter
        adapter.notifyDataSetChanged()
    }



}