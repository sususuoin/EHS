package com.example.ehs.Feed

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ehs.R
import com.google.android.youtube.player.*
import kotlinx.android.synthetic.main.fragment_youtube.view.*


class YoutubeFragment : YouTubePlayerFragment() {
    private var a: Activity? = null
    lateinit var youtubethumbnail: YouTubeThumbnailView
    lateinit var listener: YouTubeThumbnailView.OnInitializedListener

    companion object {
        var a: Activity? = null
        const val TAG : String = "유튜브 프레그먼트"
        fun newInstance() : YoutubeFragment { // newInstance()라는 함수를 호출하면 CommunityFragment를 반환함
            return YoutubeFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(YoutubeFragment.TAG, "YoutubeFragment - onCreate() called")

    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(FeedFragment.TAG, "FeedFragment - onAttach() called")
    }


    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "YoutubeFragment - onCreateView() called")
        val view : View = inflater.inflate(R.layout.fragment_youtube, container, false)

        youtubethumbnail = view.findViewById(R.id.view_youtube_)
        Log.d("허허허허", "안녕")

        listener = object : YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader {
            override fun onInitializationSuccess(
                p0: YouTubeThumbnailView?,
                p1: YouTubeThumbnailLoader?,
            ) {
//                p0?.initialize("5X7WWVTrBvM",listener)
                p1?.setVideo("5X7WWVTrBvM")

                Log.d("listener","되나욤")



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

        youtubethumbnail.initialize("AIzaSyBjaWg9Rc4kAzMKKJbG4hCU5KzHemiWclM",listener)




        return view

    }
}



