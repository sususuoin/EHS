package com.example.ehs.Feed

import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import kotlinx.android.synthetic.main.fragment_youtube_recommand.*
import kotlinx.android.synthetic.main.fragment_youtube_recommand.view.*
import java.io.IOException


class YoutuberecommandFragment : Fragment() {
    private var a: Activity? = null
    lateinit var listener: YouTubeThumbnailView.OnInitializedListener

    var videoTitle = ArrayList<String>()
    var videoId = ArrayList<String>()
    lateinit var mProgressDialog : ProgressDialog
    val API_KEY: String? = "AIzaSyAZ52VKQsE8cDA4jDT9QCubOjy5q9N-L30"


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    companion object {
        const val TAG: String = "유튜브 추천 프레그먼트"
        fun newInstance(): YoutuberecommandFragment { // newInstance()라는 함수를 호출하면 CommunityFragment를 반환함
            return YoutuberecommandFragment()
        }
    }



    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "YoutuberecommandFragment - onCreate() called")
        val youtubeAsyncTask = YoutubeAsyncTask()
        youtubeAsyncTask.execute()


    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is Activity){
            a = context}
        Log.d(TAG, "YoutuberecommandFragment - onAttach() called")
    }

    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "YoutuberecommandFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_youtube_recommand, container, false)

        view.tv_feed.setOnClickListener {
            Log.d("FeedFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FeedFragment.newInstance())
        }




        view.view_thumbnail0.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v="+videoId[0]))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        view.view_thumbnail1.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v="+ videoId[1]))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        view.view_thumbnail2.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v="+videoId[2]))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })

        view.view_thumbnail3.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v="+videoId[3]))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })
        view.view_thumbnail4.setOnClickListener(View.OnClickListener {

            val webIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v="+videoId[4]))
            try {
                this.startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
            }
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getThumbnail(videoID: String, thumnail: YouTubeThumbnailView) {
        listener = object : YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader {

            override fun onInitializationSuccess(p0: YouTubeThumbnailView?, p1: YouTubeThumbnailLoader?) {
                p1?.setVideo(videoID)
                Log.d("listener", "되나욤")
            }
            override fun onInitializationFailure(p0: YouTubeThumbnailView?, p1: YouTubeInitializationResult?) {
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

        thumnail.initialize("AIzaSyBjaWg9Rc4kAzMKKJbG4hCU5KzHemiWclM", listener)
    }

    // Youtube 정보 가져오기
    open inner class YoutubeAsyncTask : AsyncTask<Void?, Void?, ArrayList<String>>() {

        // 실행될때까지 아무것도 안되게
        override fun onPreExecute() {

            // Create a progressdialog
            mProgressDialog = ProgressDialog(a)
            mProgressDialog.setTitle("Loading...")
            mProgressDialog.setMessage("Image uploading...")
            mProgressDialog.setCanceledOnTouchOutside(false)
            mProgressDialog.setIndeterminate(false)
            mProgressDialog.show()
        }


        override fun doInBackground(vararg params: Void?): ArrayList<String> {
            try {
                Log.d("호호호호호", "우우우우우")
                val HTTP_TRANSPORT: HttpTransport = NetHttpTransport()
                val JSON_FACTORY: JsonFactory = JacksonFactory()
                val NUMBER_OF_VIDEOS_RETURNED: Long = 5
                val youtube = YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY
                ) { }.setApplicationName("youtube-search-sample").build()
                val search = youtube.search().list("id,snippet")
                search.key = API_KEY
                Log.d("호호호호호", "우우우우우" + API_KEY)
                search.q = "가을 여자 코디" // video 검색 키워드
                search.order = "relevance" //date relevance
                search.type = "video"
                search.fields =
                    "items(id/videoId,snippet/title)"
                search.maxResults = NUMBER_OF_VIDEOS_RETURNED
                val searchResponse = search.execute()
                val searchResultList = searchResponse.items

                for ( i in 0 until searchResultList.size) {
                    Log.d("비디오", searchResultList.toString())
                    videoId.add(searchResultList[i].id.videoId)
                    videoTitle.add(searchResultList[i].snippet.title)
                }

            } catch (e: GoogleJsonResponseException) {
                System.err.println("There was a service error: " + e.details.code + " : "
                        + e.details.message)
                System.err.println("There was a service error 2: " + e.localizedMessage + " , " + e.toString())
            } catch (e: IOException) {
                System.err.println("There was an IO error: " + e.cause + " : " + e.message)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
            return videoId
        }

        override fun onPostExecute(videoId: ArrayList<String>) {
            mProgressDialog.dismiss()
            Log.d("33비디오", videoId.toString())


            for ( i in 0 until videoId.size) {
                when (i) {
                    0 -> {
                        getThumbnail(videoId[0], view_thumbnail0)
                        tv_youtubetitle0.text = videoTitle[i]
                    }
                    1 -> {
                        getThumbnail(videoId[1], view_thumbnail1)
                        tv_youtubetitle1.text = videoTitle[i]
                    }
                    2 -> {
                        getThumbnail(videoId[2], view_thumbnail2)
                        tv_youtubetitle2.text =  videoTitle[i]
                    }
                    3 -> {
                        getThumbnail(videoId[3], view_thumbnail3)
                        tv_youtubetitle3.text =  videoTitle[i]
                    }
                    4 -> {
                        getThumbnail(videoId[4], view_thumbnail4)
                        tv_youtubetitle4.text =  videoTitle[i]
                    }

                }

            }

        }
    }


}