package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_feed.view.*
import kotlinx.android.synthetic.main.fragment_feed.view.tv_youtube
import kotlinx.android.synthetic.main.fragment_feed2.*
import kotlinx.android.synthetic.main.fragment_feed2.view.*
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList


class FeedFragment2 : Fragment() {
    private var a: Activity? = null

    companion object {
        const val TAG : String = "피드 프레그먼트"
        fun newInstance() : FeedFragment2 { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return FeedFragment2()
        }
    }

    val feedList = mutableListOf<Feed>()
    var adapter = FeedsListAdapter2(feedList)

    var before_page : Int = 0
    var after_page : Int = 0

    var feedNumArr = ArrayList<String>()
    var feedIdArr = ArrayList<String>()
    var feedStyleArr = ArrayList<String>()
    var feedImgArr = ArrayList<String>()
    var feedlikeCntArr = ArrayList<String>()
    var feednolikeCntArr = ArrayList<String>()
    var feeduserprofileImgArr = ArrayList<String>()

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FeedFragment - onCreate() called")

        feedNumArr = AutoFeed.getFeedNum(a!!)
        feedIdArr = AutoFeed.getFeedId(a!!)
        feedStyleArr = AutoFeed.getFeedStyle(a!!)
        feedImgArr = AutoFeed.getFeedName(a!!)
        feedlikeCntArr = AutoFeed.getFeedLikeCnt(a!!)
        feednolikeCntArr = AutoFeed.getFeednoLikeCnt(a!!)
        feeduserprofileImgArr = AutoFeed.getFeeduserprofileImg(a!!)

        after_page = 6
        parseResult(before_page, after_page)

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "새로고침 실행")

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
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "FeedFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_feed2, container, false)

        view.tv_youtube.setOnClickListener {
            Log.d("FeedFragment", "커뮤니티로 이동")
            (activity as MainActivity?)!!.replaceFragment(YoutubeFragment.newInstance())
        }

        view.scrollView333.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d("피드갯수", "스크롤")
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                Log.d("피드갯수1", before_page.toString())
                Log.d("피드갯수2", after_page.toString())
                before_page += 6
                after_page += 6
                Log.d("피드갯수3", before_page.toString())
                Log.d("피드갯수4", after_page.toString())
                view.progress_bar.visibility = View.VISIBLE
                if(feedNumArr.size <= after_page) {
                    Log.d("피드갯수5", feedNumArr.size.toString())
                    Log.d("피드갯수6", after_page.toString())
                    after_page = feedNumArr.size
                }
                parseResult(before_page, after_page)

            }
        })

        return view
    }

    private fun parseResult(before_page: Int, after_page: Int) {
        var a_bitmap: Bitmap? = null
        for (i in before_page until after_page) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        Log.d("Closet프래그먼터리스트123", feedImgArr[i])

                        //서버에 올려둔 이미지 URL
                        val url = URL("http://13.125.7.2/img/cody/" + feedImgArr[i])
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        conn.setDoInput(true) //Server 통신에서 입력 가능한 상태로 만듦
                        conn.connect() //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                        val iss: InputStream = conn.getInputStream() //inputStream 값 가져오기
                        a_bitmap = BitmapFactory.decodeStream(iss) // Bitmap으로 반환

                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    var fuserProfile = AutoLogin.StringToBitmap(feeduserprofileImgArr[i], 100, 100)
                    val resizedBmp = Bitmap.createScaledBitmap(fuserProfile!!, 100, 100, true)
//                val resizedBmp2 = Bitmap.createScaledBitmap(a_bitmap!!, 160, 160, true)
                    var feed = Feed(feedNumArr[i],
                        resizedBmp!!,
                        feedIdArr[i],
                        feedStyleArr[i],
                        a_bitmap,
                        feedlikeCntArr[i],
                        feednolikeCntArr[i])
                    feedList.add(feed)
                }
            }

            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        view?.progress_bar?.visibility = View.GONE
        adapter.notifyDataSetChanged()
        MainActivity.homeProgressDialog?.dismiss()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(a, 2)
        rv_userfeed2.layoutManager = gridLayoutManager

        rv_userfeed2.adapter = adapter
        adapter.notifyDataSetChanged()
        //recylerview 이거 fashionista.xml에 있는 변수
    }


}