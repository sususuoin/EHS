package com.example.ehs.Fashionista

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Feed.Feed
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_fashionista.*
import kotlinx.android.synthetic.main.fragment_fashionista.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.tv_favorite
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class FashionistaFragment : Fragment() {
    private var a: Activity? = null

    var fuserIdArr2 = ArrayList<String>()
    var fuserLevelArr2 = ArrayList<String>()
    var fuserProImgArr2 = ArrayList<String>()
    var favoriteListArr = ArrayList<String>()

    val FashionistaList = mutableListOf<Fashionista>()
    val FashionistaCodyList = mutableListOf<FashionistaCody>()
    val adapter = FashionistaListAdapter(FashionistaList)
    val adapter2 = FashionistaCodyListAdapter(FashionistaCodyList)

    lateinit var userId :String

    var favoriteuserIdArr = mutableListOf<String>()
    var favoriteListArr2 : ArrayList<String>? = null

    var fuserIdArr = ArrayList<String>()
    var fcodyImgName = ArrayList<String>()
    var fcodyStyle = ArrayList<String>()

    companion object {
        const val TAG : String = "패셔니스타 프래그먼트"
        lateinit var dialog : ProgressDialog
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

        //dialog가 lateinit이기떄문에 null이면ㅇ ㅏㄴ되서 oncreate에서 한번 선언해준 뒤 사용
        dialog = ProgressDialog(a)

        var fuserProfile : Bitmap?
        for (i in 0 until fuserIdArr2.size) {

            fuserProfile = AutoLogin.StringToBitmap(fuserProImgArr2[i])
            var fashin = Fashionista(fuserIdArr2[i], fuserLevelArr2[i], fuserProfile)
            FashionistaList.add(fashin)
            fuserProfile == null
        }

        fuserIdArr = AutoPro.getFuserId(a!!)
        fcodyImgName = AutoPro.getFcodyImgName(a!!)
        fcodyStyle = AutoPro.getFcodyStyle(a!!)

        var a_bitmap : Bitmap? = null

        Log.d("fasionista프래그먼터리스트", fuserIdArr.size.toString())

        for (i in 0 until fuserIdArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("fasionista프래그먼터리스트", fcodyImgName[i])

                        val url = URL("http://13.125.7.2/img/cody/" + fcodyImgName[i])

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        conn.setDoInput(true)
                        conn.connect()
                        val iss: InputStream = conn.getInputStream()
                        a_bitmap = BitmapFactory.decodeStream(iss)

                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

                var fashionistaCody = FashionistaCody(a_bitmap!!)
                FashionistaCodyList.add(fashionistaCody)


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        adapter.notifyDataSetChanged()






        (activity as MainActivity).Favorite_check()


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
            (activity as MainActivity?)!!.replaceFragment(newInstance())
        }
        
        view.tv_favorite.setOnClickListener {
            Log.d("FashionistaFragment", "즐겨찾기로 이동")
            if(favoriteListArr.size !=0) {

                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                dialog.setMessage("업로드 중입니다.")
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()

                favoriteListUp()
                Log.d("기분1111111111","!11111111111")
            } else {
                (activity as MainActivity?)?.replaceFragment(FavoriteFragment.newInstance())
            }
        }

        if(fuserIdArr.size == 0) {
            view.tv_ready.visibility = View.VISIBLE
        } else {
            view.tv_ready.visibility = View.GONE
        }

        favoriteListArr = AutoPro.getFavoriteuserId(a!!)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val Linear = LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false)
        rc_fashionistacody.layoutManager = Linear
        rc_fashionistacody.setHasFixedSize(true)

        rc_fashionistacody.adapter = adapter2
        adapter2.notifyDataSetChanged()

        mRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        Log.d("FashionistaFragment", "RESUME")
        refreshFragment(this, getFragmentManager())
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


        var favoriteuserHashTagArr = mutableListOf<String>()
        var favoriteuserProImgArr = mutableListOf<String>()

        favoriteListArr2 = AutoPro.getFavoriteuserId(a!!)

        for (i in 0 until favoriteListArr2!!.size) {
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

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.d("기분1111111111",favoriteuserIdArr.toString())
                    Log.d("기분2222222222",favoriteListArr2.toString())
                    if (favoriteuserIdArr.size == favoriteListArr2?.size) {
                        (activity as MainActivity?)?.replaceFragment(FavoriteFragment.newInstance())
                    }
                }

            var proId = favoriteListArr2!![i]
            val favoriteListUp_Request = FavoriteListUp_Request(proId!!, responseListener)
            val queue = Volley.newRequestQueue(a)
            queue.add(favoriteListUp_Request)

        }
    }






}