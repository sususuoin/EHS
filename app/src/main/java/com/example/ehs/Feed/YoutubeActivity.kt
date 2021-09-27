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
import androidx.appcompat.widget.Toolbar
import com.example.ehs.R
import com.google.android.youtube.player.*
import kotlinx.android.synthetic.main.activity_youtube.*
import kotlinx.android.synthetic.main.fragment_youtube.view.*


class YoutubeActivity : YouTubeBaseActivity() {
    private lateinit var youtubethumbnail1: YouTubeThumbnailView
    private lateinit var youtubethumbnail2: YouTubeThumbnailView
    private lateinit var youtubethumbnail3: YouTubeThumbnailView
    private lateinit var youtubethumbnail4: YouTubeThumbnailView


    lateinit var listener: YouTubeThumbnailView.OnInitializedListener



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)

//        /**
//         * 액션바 대신 툴바를 사용하도록 설정
//         */
//        val toolbar = findViewById(R.id.toolbar_youtube) as Toolbar
//        setSupportActionBar(toolbar)
//        val ab = supportActionBar!!
//        ab.setDisplayShowTitleEnabled(false)
//
//        //뒤로 가기 버튼 생성
//        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        youtubethumbnail1 = findViewById(R.id.view_thumbnail1)
        youtubethumbnail2 = findViewById(R.id.view_thumbnail2)
        youtubethumbnail3 = findViewById(R.id.view_thumbnail3)
        youtubethumbnail4 = findViewById(R.id.view_thumbnail4)



        youtubethumbnail1.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=y5tnwXyaB4Y"))
            try {
                this@YoutubeActivity.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail2.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=8HTyePTTLAo"))
            try {
                this@YoutubeActivity.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail3.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=bGTt8EStqOk"))
            try {
                this@YoutubeActivity.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        youtubethumbnail4.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=_EzVFxXpOf8"))
            try {
                this@YoutubeActivity.startActivity(webIntent)
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
        Log.d("원","흑흑흑흑")

        getThumbnail("8HTyePTTLAo",youtubethumbnail2)
        Log.d("투","흑흑흑흑")

        getThumbnail("bGTt8EStqOk",youtubethumbnail3)
        getThumbnail("_EzVFxXpOf8",youtubethumbnail4)

    }

}











