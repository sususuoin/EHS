package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_community.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

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

        userId = AutoLogin.getUserId(a!!)

        favoriteuserIdArr = AutoPro.getFavoriteuserId2(a!!)
        favoriteuserHashTagArr = AutoPro.getFavoriteuserHashTag(a!!)
        favoriteuserProImgArr = AutoPro.getFavoriteuserImg(a!!)


        if(favoriteuserIdArr.size!=0) {
            var favoriteuserProfile : Bitmap?
            for(i in 0 until favoriteuserIdArr.size) {
                favoriteuserProfile = AutoLogin.StringToBitmap(favoriteuserProImgArr[i])
                var favorite = Favorite(favoriteuserIdArr[i], '#'+favoriteuserHashTagArr[i], favoriteuserProfile)
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
        savedInstanceState: Bundle?
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
            recommend()
        }


//        val resources: Resources = this.resources
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.basicprofile)

//        var one = Favorite("lu__eun","#스트릿", bitmap)
//        var two = Favorite("Ha_nle","#데일리", bitmap)
//        var three = Favorite("tndlstksxk","#아메카지", bitmap)
//
//        favoriteList.add(one)
//        FfavoriteList.add(two)
//        favoriteList.add(three)

//        if(favoriteuserIdArr.size!=0) {
//            for(i in 0 until favoriteuserIdArr.size) {
//                var favoriteuserProfile = AutoLogin.StringToBitmap(favoriteuserProImgArr[i])
//                var favorite = Favorite(favoriteuserIdArr[i], '#'+favoriteuserHashTagArr[i], favoriteuserProfile)
//                favoriteList.add(favorite)
//                favoriteuserProfile == null
//            }
//        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = LinearLayoutManager(a)
        rv_favorite.layoutManager = gridLayoutManager


        rv_favorite.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun recommend() {
        var proStyle : String
        var prouserId : String
        var proprofileImg : String
        var proIdArr = mutableListOf<String>()
        var proImgArr = mutableListOf<String>()
        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    Log.d("~~1", response)
                    Log.d("~~2", arr.toString())
                    Log.d("~~22", arr.length().toString())

                    if(arr.length().toString() == "0") {
                        Toast.makeText(a!!, "전문가부족현상으로 다음에 이용해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    else {
                        for (i in 0 until arr.length()) {
                            val proObject = arr.getJSONObject(i)
                            Log.d("~~3", arr[i].toString())

                            proStyle = proObject.getString("codyStyle")
                            prouserId = proObject.getString("userId")
                            proprofileImg = proObject.getString("userProfileImg")


                            proIdArr.add(prouserId)
                            proImgArr.add(proprofileImg)


                            AutoPro.setStyle(a!!, proStyle)
                            AutoPro.setProProfileId(a!!, proIdArr as ArrayList<String>)
                            AutoPro.setProProfileImg(a!!, proImgArr as ArrayList<String>)
                        }
                        // 추천 액티비티로 이동
                        val intent = Intent(a!!, ProRecommendActivity::class.java)
                        startActivity(intent)
                    }



                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(a!!, "코디를 한개이상 등록해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        val proRecommendRequest = ProRecommend_Request(userId!!, responseListener)
        val queue = Volley.newRequestQueue(a)
        queue.add(proRecommendRequest)

    }

}