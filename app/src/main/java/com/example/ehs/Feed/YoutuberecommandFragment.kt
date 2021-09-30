package com.example.ehs.Feed

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView

class YoutuberecommandFragment : Fragment() {
    private var a: Activity? = null
    private lateinit var youtubethumbnail1: YouTubeThumbnailView
    private lateinit var youtubethumbnail2: YouTubeThumbnailView
    private lateinit var youtubethumbnail3: YouTubeThumbnailView
    private lateinit var youtubethumbnail4: YouTubeThumbnailView
    private lateinit var feed : TextView
    private lateinit var youtube : TextView

    lateinit var listener: YouTubeThumbnailView.OnInitializedListener



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)



    companion object {
        const val TAG : String = "유튜브 추천 프레그먼트"
        fun newInstance() : YoutuberecommandFragment { // newInstance()라는 함수를 호출하면 CommunityFragment를 반환함
            return YoutuberecommandFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "YoutuberecommandFragment - onCreate() called")

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "YoutuberecommandFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "YoutuberecommandFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_youtube_recommand, container, false)
        youtubethumbnail1 = view.findViewById(R.id.view_thumbnail1)
        youtubethumbnail2 = view.findViewById(R.id.view_thumbnail2)
        youtubethumbnail3 = view.findViewById(R.id.view_thumbnail3)
        youtubethumbnail4 = view.findViewById(R.id.view_thumbnail4)
        feed = view.findViewById(R.id.tv_feed)
        youtube = view.findViewById(R.id.tv_youtube)

        feed.setOnClickListener {
            Log.d("FeedFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FeedFragment.newInstance())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        youtubethumbnail1.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=y5tnwXyaB4Y"))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail2.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=8HTyePTTLAo"))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail3.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=bGTt8EStqOk"))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail4.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=_EzVFxXpOf8"))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        fun getThumbnail(videoID:String,thumnail:YouTubeThumbnailView) {
            listener = object : YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader {

                override fun onInitializationSuccess(

                    p0: YouTubeThumbnailView?,
                    p1: YouTubeThumbnailLoader?,
                ) {
                    Log.d("여긴", "돼?")

                    p1?.setVideo(videoID)


                    Log.d("listener", "되나욤")


                }

                override fun onInitializationFailure(
                    p0: YouTubeThumbnailView?,
                    p1: YouTubeInitializationResult?,
                ) {


                    Log.d("listener", "왜안돼ㅜㅜ p1 : $p1 ")
                }

                override fun setOnThumbnailLoadedListener(p0: YouTubeThumbnailLoader.OnThumbnailLoadedListener?) {
                    //새 미리보기 이미지가 모두 로드되고 이 YouTube 미리보기 이미지 뷰에 표시될 때마다 호출되는
                    // YouTubeThumbnailLoader.OnThumbnailLoadedListener를 설정
                    TODO("Not yet implemented")
                }

                override fun setVideo(p0: String?) {
                    //제공된 YouTube 동영상 id에 대한 미리보기 이미지를 표시하도록 이 뷰를 설정
                }

                override fun setPlaylist(p0: String?) {
                    TODO("Not yet implemented")
                }


                override fun setPlaylist(p0: String?, p1: Int) {
                    //제공된 YouTube 재생목록 id에 대한 미리보기 이미지를 표시하도록 이 뷰를 설정
                    TODO("Not yet implemented")
                }

                override fun next() {
                    //재생목록에서 다음 동영상의 미리보기 이미지를 표시
                    TODO("Not yet implemented")
                }

                override fun previous() {
                    //재생목록에서 이전 동영상의 미리보기 이미지를 표시
                    TODO("Not yet implemented")
                }

                override fun first() {
                    //재생목록에서 첫 번째 동영상의 미리보기기
                    TODO("Not yet implemented")
                }

                override fun hasNext(): Boolean {
                    // 현재 로드된 재생목록에 다음 동영상이 있는지를 확인

                    TODO("Not yet implemented")
                }

                override fun hasPrevious(): Boolean {
                    //현재 로드된 재생목록에 이전 동영상이 있는지를 확인
                    TODO("Not yet implemented")
                }

                override fun release() {
                    //이 YouTubeThumbnailLoader에서 사용된 시스템 리소스를 해제
                    TODO("Not yet implemented")
                }

            }

            thumnail.initialize("AIzaSyBjaWg9Rc4kAzMKKJbG4hCU5KzHemiWclM",listener)
        }



        getThumbnail("y5tnwXyaB4Y",youtubethumbnail1)
        getThumbnail("8HTyePTTLAo",youtubethumbnail2)
        getThumbnail("bGTt8EStqOk",youtubethumbnail3)
        getThumbnail("_EzVFxXpOf8",youtubethumbnail4)


//        val gridLayoutManager = LinearLayoutManager(a)
//        rv_community.layoutManager = gridLayoutManager
//
//
//        val adapter = CommunityListAdapter(communityList)
//        rv_community.adapter = adapter
//        adapter.notifyDataSetChanged()
    }

}