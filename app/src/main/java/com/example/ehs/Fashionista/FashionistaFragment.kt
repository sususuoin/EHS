package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_fashionista.*
import kotlinx.android.synthetic.main.fragment_fashionista.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.tv_favorite
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FashionistaFragment : Fragment() {
    private var a: Activity? = null

    var fuserIdArr2 = ArrayList<String>()
    var fuserLevelArr2 = ArrayList<String>()
    var fuserProImgArr2 = ArrayList<String>()
    var favoriteListArr = ArrayList<String>()

    val FashionistaList = mutableListOf<Fashionista>()
    val adapter = FashionistaListAdapter(FashionistaList)

    lateinit var userId :String

    companion object {
        const val TAG : String = "패셔니스타 프래그먼트"
        fun newInstance() : FashionistaFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함

            return FashionistaFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FashionistaFragment - onCreate() called")

        fuserIdArr2 = AutoPro.getProuserId(a!!)
        Log.d(TAG + "내맘", fuserIdArr2.toString())
        fuserLevelArr2 = AutoPro.getProuserLevel(a!!)
        fuserProImgArr2 = AutoPro.getProuserProImg(a!!)

        favoriteListArr = AutoPro.getFavoriteuserId(a!!)

        userId = AutoLogin.getUserId(a!!)

        var fuserProfile : Bitmap?
        for (i in 0 until fuserIdArr2.size) {

            fuserProfile = AutoLogin.StringToBitmap(fuserProImgArr2[i])
            var fashin = Fashionista(fuserIdArr2[i], fuserLevelArr2[i], fuserProfile)
            FashionistaList.add(fashin)
            fuserProfile == null
        }


        favorite_check()

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "FashionistaFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "FashionistaFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_fashionista, container, false)

        view.tv_fashionista.setOnClickListener {
            Log.d("FashionistaFragment", "나로 이동")
            (activity as MainActivity?)!!.replaceFragment(this)
        }
        
        view.tv_favorite.setOnClickListener {
            Log.d("FashionistaFragment", "피드로 이동")
            if(favoriteListArr.size ==0) {
                (activity as MainActivity?)!!.replaceFragment(FavoriteFragment.newInstance())
            }
            else {
                favoriteListUp()
            }

        }

        favorite_check()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRecyclerView.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        Log.d("FashionistaFragment", "RESUME")
        refreshFragment(this, getFragmentManager())
        adapter.notifyDataSetChanged()
    }

    // Fragment 새로고침
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager?) {
        var ft: FragmentTransaction? = fragmentManager?.beginTransaction()
        ft?.detach(fragment)?.attach(fragment)?.commit()
    }



    private fun favoriteListUp() {

        var favoriteuserId: String
        var favoriteuserHashTag: String
        var favoriteuserProfileImg : String

        var favoriteuserIdArr = mutableListOf<String>()
        var favoriteuserHashTagArr = mutableListOf<String>()
        var favoriteuserProImgArr = mutableListOf<String>()

        for (i in 0 until favoriteListArr.size) {
            val responseListener: Response.Listener<String?> =
                Response.Listener<String?> { response ->
                    try {

                        var jsonObject = JSONObject(response)

                        val arr: JSONArray = jsonObject.getJSONArray("response")

                        for (i in 0 until arr.length()) {
                            val fuserObject = arr.getJSONObject(i)

                            favoriteuserId = fuserObject.getString("userId")
                            favoriteuserHashTag = fuserObject.getString("HashTag")
                            favoriteuserProfileImg = fuserObject.getString("userProfileImg")

                            favoriteuserIdArr.add(favoriteuserId)
                            favoriteuserHashTagArr.add(favoriteuserHashTag)
                            favoriteuserProImgArr.add(favoriteuserProfileImg)

                            AutoPro.setFavoriteuserId2(a!!, favoriteuserIdArr as java.util.ArrayList<String>)
                            AutoPro.setFavoriteuserHashTag(a!!, favoriteuserHashTagArr as java.util.ArrayList<String>)
                            AutoPro.setFavoriteuserImg(a!!, favoriteuserProImgArr as java.util.ArrayList<String>)
                            adapter.notifyDataSetChanged()
                            (activity as MainActivity?)?.replaceFragment(FavoriteFragment.newInstance())

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            var proId = favoriteListArr[i]
            val favoriteListUp_Request = FavoriteListUp_Request(proId!!, responseListener)
            val queue = Volley.newRequestQueue(a)
            queue.add(favoriteListUp_Request)
        }

    }

    fun favorite_check() {

        var cuserId: String
        var favoriteuserId: String
        var favorite_true: String
        var favoriteuserIdArr = mutableListOf<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val Object = arr.getJSONObject(i)

                        cuserId = Object.getString("userId")
                        favoriteuserId = Object.getString("prouserId")
                        favorite_true = Object.getString("favorite_true")

                        favoriteuserIdArr.add(favoriteuserId)
                        Log.d("기분?", favoriteuserId)

                        AutoPro.setFavoriteuserId(a!!, favoriteuserIdArr as java.util.ArrayList<String>)

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val favoritecheck_Request = FavoriteCheck_Request(userId!!, responseListener)
        val queue = Volley.newRequestQueue(a)
        queue.add(favoritecheck_Request)
    }




}