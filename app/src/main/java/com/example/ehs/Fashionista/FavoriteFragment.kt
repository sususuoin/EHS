package com.example.ehs.Fashionista

import android.app.Activity
import android.content.Context
import android.content.LocusId
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import org.json.JSONException
import org.json.JSONObject

class FavoriteFragment : Fragment() {

    private var a: Activity? = null
    lateinit var userId: String


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
        Log.d(TAG, "FavoriteFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        view.tv_fashionista2.setOnClickListener {
            Log.d("FashionistaFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FashionistaFragment.newInstance())
        }
        // 추천배너 클릭 시
        view.btn_recommend.setOnClickListener {

            recommend()
//            val intent = Intent(context, ProRecommendActivity::class.java)
//            startActivity(intent)

        }

        return view
    }

    fun recommend() {
        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")
                    var codyStyle = jsonObject.getString("codyStyle")
                    Log.d(TAG, userId)
                    Log.d(TAG, success.toString())

                    //이거 auto로 저장시킨다음에 액티비티로 넘겨주자 어떄 ?좋아~~~~~예 하늘이짱짱맨 콜~~
                    Log.d(TAG, codyStyle)




                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val proRecommendRequest = ProRecommend_Request(userId!!, responseListener)
        val queue = Volley.newRequestQueue(a)
        queue.add(proRecommendRequest)

    }

}