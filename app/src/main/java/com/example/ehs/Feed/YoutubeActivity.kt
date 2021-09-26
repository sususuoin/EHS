package com.example.ehs.Feed

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ehs.R
import com.google.android.youtube.player.*
import com.google.common.reflect.Reflection.initialize
import kotlinx.android.synthetic.main.activity_youtube.*
import kotlinx.android.synthetic.main.fragment_youtube.view.*


class YoutubeActivity : YouTubeBaseActivity() {
    private var a: Activity? = null
    lateinit var youtubethumbnail: YouTubeThumbnailView
    lateinit var listener: YouTubeThumbnailView.OnInitializedListener


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

        youtubethumbnail = findViewById(R.id.view_thumbnail)



        youtubethumbnail.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=5X7WWVTrBvM"))
            try {
                this@YoutubeActivity.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })


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



        //            YouTubePlayer.OnInitializedListener {
//            override fun onInitializationSuccess(
//                provider: YouTubePlayer.Provider?,
//                youTubePlayer: YouTubePlayer,
//                b: Boolean,
//            ) {
//
//                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//                youTubePlayer.setPlaybackEventListener(playbackEventListener);
//        /** Start buffering **/
//        if (!b) {
//            youTubePlayer.cueVideo(VIDEO_ID);
//        }
//
//
//                Log.d("성공", "와우우우")
//                youTubePlayer.loadVideo("vewH-f3fAes") //
//                //https://www.youtube.com/watch?v=NmkYHmiNArc 유투브에서 v="" 이부분이 키에 해당
//
//            }

//            fun onInitializationFailure(
//                p0: YouTubeThumbnailView?,
//                p1: YouTubeInitializationResult?
//            ) {
//
//
//            }
    }


}


//        btn_youtube.setOnClickListener(View.OnClickListener {
//            Log.d("1111", "1111")
//            youTubePlayerView.initialize("AIzaSyBjaWg9Rc4kAzMKKJbG4hCU5KzHemiWclM",
//                listener)
//
//            Log.d("2222", "2222")
//
//        })






