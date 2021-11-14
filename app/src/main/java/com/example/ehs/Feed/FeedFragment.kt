package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList


class FeedFragment : Fragment() {
    private var a: Activity? = null

    companion object {
        const val TAG : String = "피드 프레그먼트"
        fun newInstance() : FeedFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return FeedFragment()
        }
    }

    val feedList = mutableListOf<Feed>()
    var adapter = FeedsListAdapter(feedList)

    var before_page : Int = 0
    var after_page : Int = 0

    var feedrank_feedNumArr = ArrayList<String>()
    var feedrank_feeduserId = ArrayList<String>()
    var feedrank_likecnt = ArrayList<String>()
    var feedrank_ImgName = ArrayList<String>()

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

        feedrank_feedNumArr = AutoFeed.getFeedRank_feedNum(a!!)
        feedrank_feeduserId = AutoFeed.getFeedRank_feed_userId(a!!)
        feedrank_likecnt = AutoFeed.getFeedRank_like_cnt(a!!)
        feedrank_ImgName = AutoFeed.getFeedRank_feed_ImgName(a!!)

        feedNumArr = AutoFeed.getFeedNum(a!!)
        feedIdArr = AutoFeed.getFeedId(a!!)
        feedStyleArr = AutoFeed.getFeedStyle(a!!)
        feedImgArr = AutoFeed.getFeedName(a!!)
        feedlikeCntArr = AutoFeed.getFeedLikeCnt(a!!)
        feednolikeCntArr = AutoFeed.getFeednoLikeCnt(a!!)
        feeduserprofileImgArr = AutoFeed.getFeeduserprofileImg(a!!)

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
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        view.tv_youtube.setOnClickListener {
            Log.d("FeedFragment", "유튜브추천 이동")
            if (requireFragmentManager().findFragmentByTag("youtube") != null) {
                requireFragmentManager().beginTransaction().show(requireFragmentManager().findFragmentByTag("youtube")!!).commit()
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    launch(Dispatchers.Main) {
                        MainActivity.homeProgressDialog!!.show()
                    }
                    delay(500L)

                    requireFragmentManager().beginTransaction().add(R.id.fragments_frame, YoutubeFragment(), "youtube").commit()
                }
            }
            if (requireFragmentManager().findFragmentByTag("feed") != null) {
                requireFragmentManager().beginTransaction().hide(requireFragmentManager().findFragmentByTag("feed")!!).commit()
            }
        }

        var a_bitmap : Bitmap? = null
        Log.d("피드랭킹이미지", feedrank_ImgName.size.toString())
        if(feedrank_feedNumArr.size==1 || feedrank_feedNumArr.size==2 || feedrank_feedNumArr.size==3){
            for (i in 0 until feedrank_feedNumArr.size) {
                val uThread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            Log.d("피드랭킹이미지", feedrank_ImgName[i])

                            val url = URL("http://13.125.7.2/img/cody/" + feedrank_ImgName[i])

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

                    feedIdArr = AutoFeed.getFeedId(a!!)
                    var int_idx = feedIdArr.indexOf(feedrank_feeduserId[i])
                    Log.d("zzzz키득", int_idx.toString())

                    var ranking_profile = AutoLogin.StringToBitmap(feeduserprofileImgArr[int_idx], 100, 100)


                    if(feedIdArr.contains(feedrank_feeduserId[i])) {
                        when(i) {
                            0 -> {
                                view.top1Img.setImageBitmap(a_bitmap)
                                view.top1Id.setText(feedrank_feeduserId[i])
                                view.top1cnt.setText(feedrank_likecnt[i])
                                view.iv_userphoto1.setImageBitmap(ranking_profile)
                            }
                            1 -> {
                                view.top2Img.setImageBitmap(a_bitmap)
                                view.top2Id.setText(feedrank_feeduserId[i])
                                view.top2cnt.setText(feedrank_likecnt[i])
                                view.iv_userphoto2.setImageBitmap(ranking_profile)
                            }
                            2 -> {
                                view.top3Img.setImageBitmap(a_bitmap)
                                view.top3Id.setText(feedrank_feeduserId[i])
                                view.top3cnt.setText(feedrank_likecnt[i])
                                view.iv_userphoto3.setImageBitmap(ranking_profile)
                            }
                        }
                    }


                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

        }


        view.scrollView3.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
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
                    var feed = Feed(feedNumArr[i], resizedBmp!!,
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
        view.rv_userfeed.layoutManager = gridLayoutManager

        view.rv_userfeed.adapter = adapter
        adapter.notifyDataSetChanged()
    }


}