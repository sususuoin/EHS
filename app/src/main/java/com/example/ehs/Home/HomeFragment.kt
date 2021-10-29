package com.example.ehs.Home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.Volley
import com.example.ehs.AI.Main_AIActivity
import com.example.ehs.BottomSheet.BottomSheet_tpo
import com.example.ehs.Calendar.AutoCalendar
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.Closet.CodySaveActivity
import com.example.ehs.Fashionista.AutoPro
import com.example.ehs.Fashionista.FavoriteFragment
import com.example.ehs.Fashionista.ProRecommendActivity
import com.example.ehs.Fashionista.ProRecommend_Request
import com.example.ehs.Loading
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.example.ehs.Weather.WeatherActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var a: Activity? = null
    val now: LocalDateTime = LocalDateTime.now()
    var Strnow = now?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    lateinit var crecyclerview: androidx.recyclerview.widget.RecyclerView

    // 요일 받아오기
    var sun: String? = null
    var mon: String? = null
    var tue: String? = null
    var wed: String? = null
    var thu: String? = null
    var fri: String? = null
    var sat: String? = null

    // 달 받아오기
    var sun2: String? = null
    var mon2: String? = null
    var tue2: String? = null
    var wed2: String? = null
    var thu2: String? = null
    var fri2: String? = null
    var sat2: String? = null

    var getLatitude : String = ""
    var getLongitude : String = ""

    var cAdapter : CalendarlistAdapter? = null
    lateinit var userId: String

    companion object {
        const val TAG : String = "홈 프레그먼트"
        fun newInstance() : HomeFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return HomeFragment()
        }
        var calendarNameArr = ArrayList<String>()
        var calendarYearArr = ArrayList<String>()
        var calendarMonthArr = ArrayList<String>()
        var calendarDayArr = ArrayList<String>()
        var homeContext: Context? = null
        var homeloading : Loading? = null
    }

    var random_clothesCategoryArr = ArrayList<String>()
    var random_clothesNameArr = ArrayList<String>()

    // 프래그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
        AndroidThreeTen.init(a)
        homeContext=a!!
        userId = AutoLogin.getUserId(a!!)

        getLongitude = AutoHome.getLongitude(a!!)
        getLatitude = AutoHome.getLatitude(a!!)

        calendarNameArr = AutoCalendar.getCalendarName(a!!)
        calendarYearArr = AutoCalendar.getCalendarYear(a!!)
        calendarMonthArr = AutoCalendar.getCalendarMonth(a!!)
        calendarDayArr = AutoCalendar.getCalendarDay(a!!)

        homeloading = Loading(a!!)
        getweather()

    }
    // 프래그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "HomeFragment - onAttach() called")
    }

    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        week(Strnow!!)
        Log.d(TAG, "HomeFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.btn_calendar.setOnClickListener{
            val intent = Intent(a, CalendarActivity::class.java)
            startActivity(intent)
        }
        view.btn_weathergo.setOnClickListener{
            val intent = Intent(a, WeatherActivity::class.java)
            startActivity(intent)
        }
        view.tv_weathergo.setOnClickListener{
            val intent = Intent(a, WeatherActivity::class.java)
            startActivity(intent)
        }
        view.iv_recoai.setOnClickListener {
            val intent = Intent(a, Main_AIActivity::class.java)
            startActivity(intent)
        }
        view.iv_recotag.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    homeloading!!.init("스타일 추천")
                }
                delay(2500L)

                val intent = Intent(a, StyleRecommendActivity::class.java)
                startActivity(intent)
            }

        }
        view.iv_recocolor.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    homeloading!!.init("컬러 추천")
                }
                delay(2500L)

                colorRecommend()
            }

        }
        view.iv_recopro.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    homeloading!!.init("전문가 추천")
                }
                delay(2500L)

                recommend()
            }
        }

        view.btn_updateH.setOnClickListener {
            Toast.makeText(a, "날씨 업데이트", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).getLocation()

            getLongitude = AutoHome.getLongitude(a!!)
            getLatitude = AutoHome.getLatitude(a!!)

            // MainActivity로부터 위도, 경도 받아오기
            Log.d("HomeFragment", "위도 : ${getLatitude}")
            Log.d("HomeFragment", "경도 : ${getLongitude}")
            getweather()
        }

        view.btn_retry.setOnClickListener {
            (MainActivity.mContext as MainActivity).CodyRandom()
            Log.d("랜덤랜덤", "새로고침")
            asdf()
        }

        view.btn_saveRandomCody.setOnClickListener {
            view.ll_randomCody.setDrawingCacheEnabled(true)
            view.ll_randomCody.buildDrawingCache()

            //조합한 코디를 캡쳐하여 비트맵으로 변경
            var saveBitmap = view.ll_randomCody.getDrawingCache()
            (MainActivity.mContext as MainActivity).Codycolor(saveBitmap)

            if(CodySaveActivity.codySaveContext != null) {
                (CodySaveActivity.codySaveContext as CodySaveActivity).uploadCody(saveBitmap)
            } else {
                Toast.makeText(a!!, "타임타임 아직 오류가 발생합니다.", Toast.LENGTH_SHORT).show()
            }


        }
        view.tv_tpo.setOnClickListener {
            val BottomSheet_tpo: BottomSheet_tpo = BottomSheet_tpo {
                when (it) {
                    0 -> Toast.makeText(a!!, "추천순", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(a!!, "리뷰순", Toast.LENGTH_SHORT).show()
                }
            }
            BottomSheet_tpo.show((activity as AppCompatActivity).supportFragmentManager, BottomSheet_tpo.tag)
        }

        /**
         * 캘린더 리사이클러뷰 객체
         */
        var calendarList = arrayListOf(
            Calendarlist("", sun!!, "일"),
            Calendarlist("", mon!!, "월"),
            Calendarlist("", tue!!, "화"),
            Calendarlist("", wed!!, "수"),
            Calendarlist("", thu!!, "목"),
            Calendarlist("", fri!!, "금"),
            Calendarlist("", sat!!, "토"),
            Calendarlist("", "캘린더로", " 이동")
        )
        /**
         * 캘린더 리사이클러 뷰
         */
        cAdapter = CalendarlistAdapter(a!!, calendarList)
        crecyclerview = view.findViewById(R.id.rv_homecalendar)
        val gridLayoutManager = GridLayoutManager(a, 4)
        crecyclerview.layoutManager = gridLayoutManager

        crecyclerview.adapter = cAdapter
        cAdapter!!.notifyDataSetChanged()

        // RecyclerView Adapter에서는 레이아웃 매니저 (LayoutManager) 를 설정
        // recyclerView에 setHasFixedSize 옵션에 true 값을 준다.
        crecyclerview.setHasFixedSize(true)

        return view
    } // oncreateview 끝

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        asdf()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "HomeFragment - onResume() called")
        calendarNameArr = AutoCalendar.getCalendarName(a!!)
        calendarYearArr = AutoCalendar.getCalendarYear(a!!)
        calendarMonthArr = AutoCalendar.getCalendarMonth(a!!)
        calendarDayArr = AutoCalendar.getCalendarDay(a!!)

        (activity as MainActivity).CalendarImg()
        cAdapter!!.notifyDataSetChanged()


    }

    fun asdf() {
        //랜덤으로 색 가져오기
        random_clothesCategoryArr = AutoHome.getRandom_clothesCategory(a!!)
        random_clothesNameArr = AutoHome.getRandom_clothesName(a!!)
        Log.d("랜덤랜덤", random_clothesCategoryArr.toString())
        Log.d("랜덤랜덤", random_clothesNameArr.toString())

        var a_bitmap : Bitmap? = null
        for (i in 0 until random_clothesNameArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("코디랜덤추천", random_clothesNameArr[i])

                        val url = URL("http://13.125.7.2/img/clothes/" + random_clothesNameArr[i])

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

                when(random_clothesCategoryArr[i]) {

                    "상의" -> {
                        iv_top.setImageBitmap(a_bitmap)
                        Log.d("랜덤", "상의")
                    }
                    "하의" -> {
                        iv_bottom.setImageBitmap(a_bitmap)
                    }
                    "신발" -> {
                        iv_shoes.setImageBitmap(a_bitmap)
                    }
                    "아우터" -> {
                        iv_outer.setImageBitmap(a_bitmap)
                    }
                    "가방" -> {
                        iv_bag.setImageBitmap(a_bitmap)
                    }
                }



            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

    }

    fun getweather() {
        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(WeatherActivity.BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherActivity.WeatherService::class.java)
        val call = service.getCurrentWeatherData(getLatitude, getLongitude,
            WeatherActivity.AppId
        )
        call.enqueue(object : Callback<WeatherActivity.WeatherResponse> {
            override fun onFailure(call: Call<WeatherActivity.WeatherResponse>, t: Throwable) {
                Log.d("HomeFragment", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherActivity.WeatherResponse>,
                response: Response<WeatherActivity.WeatherResponse>,
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("HomeFragment", "result: " + weatherResponse.toString())
                    val cTemp = weatherResponse!!.current!!.Ctemp - 273.15  //켈빈을 섭씨로 변환

                    val minTemp = weatherResponse!!.daily[0].Dtemp!!.Dmin - 273.15
                    val maxTemp = weatherResponse!!.daily[0].Dtemp!!.Dmax - 273.15

                    val intcTemp = cTemp.roundToInt()
                    val intMinTemp = minTemp.roundToInt()
                    val intMaxTemp = maxTemp.roundToInt()
                    val weatherIMG = weatherResponse!!.current!!.weather[0].icon.toString()

                    when (weatherIMG) { // 날씨에 맞는 아이콘 출력
                        "01d" -> view!!.img_weatherH.setImageResource(R.drawable.ic_sun)
                        "01n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_sun_night)
                        "02d" -> view!!.img_weatherH.setImageResource(R.drawable.ic_sun_c)
                        "02n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_suncloud_night)
                        "03n", "03d", "04d", "04n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_cloud_many)
                        "09d", "09n", "10d", "10n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_rain)
                        "11d", "11n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_thunder)
                        "13d", "13n" -> view!!.img_weatherH.setImageResource(R.drawable.ic_snow)
                        "50n", "50d" -> view!!.img_weatherH.setImageResource(R.drawable.ic_mist)
                    }

                    tv_cityH.text = AutoHome.getLocation(a!!)
                    tv_MinMaxH.text =
                        intMinTemp.toString() + "\u00B0" + "/ " + intMaxTemp.toString() + "\u00B0"
                    tv_cTempH.text = intcTemp.toString() + "\u00B0"
                }
            }

        })
    }


    /**
     * 특정 날짜의 같은 한 주간의 날짜 범위
     */

    fun week(eventDate: String) {
        val dateArray = eventDate.split("-").toTypedArray()
        val cal = Calendar.getInstance()
        cal[dateArray[0].toInt(), dateArray[1].toInt() - 1] = dateArray[2].toInt()

        // 일주일의 첫날을 일요일로 지정
        cal.firstDayOfWeek = Calendar.SUNDAY

        // 시작일과 특정날짜의 차이를 구한다
        val dayOfWeek = cal[Calendar.DAY_OF_WEEK] - cal.firstDayOfWeek

        // 해당 주차의 첫째날을 지정한다
        cal.add(Calendar.DAY_OF_MONTH, -dayOfWeek)
        val sf = SimpleDateFormat("yyyy-MM-dd")

        // 해당 주차의 첫째 날짜
        val startDt = sf.format(cal.time)
        Log.d("zzz호호잇", startDt)

        // 둘째 날짜
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val twoDt = sf.format(cal.time)
        // 셋째 날짜
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val threeDt = sf.format(cal.time)
        // 넷째 날짜
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val fourDt = sf.format(cal.time)
        // 다섯째 날짜
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val fiveDt = sf.format(cal.time)
        // 여섯째 날짜
        cal.add(Calendar.DAY_OF_MONTH, 1)
        val sixDt = sf.format(cal.time)
        // 해당 주차의 마지막 날짜 지정
        cal.add(Calendar.DAY_OF_MONTH, 1)
        // 해당 주차의 마지막 날짜
        val endDt = sf.format(cal.time)

        var token = startDt.split('-')
        sun = token[2] //일
        sun2 = token[1] //2자리수 월
        Log.d("zzz호호잇333", sun2!!)

        token = twoDt.split('-')
        mon = token[2]
        mon2 = token[1]

        token = threeDt.split('-')
        tue = token[2]
        tue2 = token[1]

        token = fourDt.split('-')
        wed = token[2]
        wed2 = token[1]

        token = fiveDt.split('-')
        thu = token[2]
        thu2 = token[1]

        token = sixDt.split('-')
        fri = token[2]
        fri2 = token[1]

        token = endDt.split('-')
        sat = token[2]
        sat2 = token[1]

        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 = [$startDt], 종료 날짜 = [$endDt], 두번째 = [$twoDt]")
        Log.d("일", "요일" + sun)
        Log.d("일", "요일" + mon)
    }


    fun recommend() {
        var proStyle : String
        var prouserId : String
        var proprofileImg : String
        var proplusImgPath : String
        var proplusImgName : String

        var proIdArr = ArrayList<String>()
        var proImgArr = ArrayList<String>()
        var proplusImgPathArr = ArrayList<String>()
        var proplusImgNameArr = ArrayList<String>()

        val responseListener: com.android.volley.Response.Listener<String?> =
            com.android.volley.Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    Log.d("~~1", response)
                    Log.d("~~2", arr.toString())
                    Log.d("~~22", arr.length().toString())

                    if(arr.length() == 0 || arr.length() == 1 || arr.length() ==2) {
                        Toast.makeText(a!!, "전문가부족현상으로 다음에 이용해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }

                    else {
                        for (i in 0 until arr.length()) {
                            val proObject = arr.getJSONObject(i)
                            Log.d("~~3", arr[i].toString())

                            prouserId = proObject.getString("userId")
                            proprofileImg = proObject.getString("userProfileImg")
                            proplusImgPath = proObject.getString("plusImgPath")
                            proplusImgName =  proObject.getString("plusImgName")
                            proStyle = proObject.getString("plusImgStyle")

                            proIdArr.add(prouserId)
                            proImgArr.add(proprofileImg)
                            proplusImgPathArr.add(proplusImgPath)
                            proplusImgNameArr.add(proplusImgName)

                            AutoPro.setStyle(a!!, proStyle)
                        }

                        AutoPro.setProProfileId(a!!, proIdArr)
                        AutoPro.setProProfileImg(a!!, proImgArr)
                        AutoPro.setProplusImgPath(a!!, proplusImgPathArr)
                        AutoPro.setProplusImgName(a!!, proplusImgNameArr)


                        // 추천 액티비티로 이동
                        val intent = Intent(a!!, ProRecommendActivity::class.java)
                        startActivity(intent)
                    }



                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(a!!, "코디를 한개이상 등록해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        val proRecommendRequest = ProRecommend_Request(userId!!, responseListener)
        val queue = Volley.newRequestQueue(a)
        queue.add(proRecommendRequest)

    }

    fun colorRecommend() {
        var color: String
        var coloruserId: String
        var colorplusImgPath: String
        var colorplusImgName: String
        var colorplusImgStyle: String

        var coloruserIdArr = ArrayList<String>()
        var colorplusImgPathArr = ArrayList<String>()
        var colorplusImgNameArr = ArrayList<String>()
        var colorplusImgStyleArr = ArrayList<String>()

        val responseListener: com.android.volley.Response.Listener<String?> =
            com.android.volley.Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    if (arr.length() == 0) {
                        Toast.makeText(a!!, "전문가부족현상으로 다음에 이용해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                        return@Listener
                    } else {
                        for (i in 0 until arr.length()) {
                            val proObject = arr.getJSONObject(i)

                            color = proObject.getString("plusImgColor")
                            coloruserId = proObject.getString("userId")
                            colorplusImgPath = proObject.getString("plusImgPath")
                            colorplusImgName = proObject.getString("plusImgName")
                            colorplusImgStyle = proObject.getString("plusImgStyle")


                            coloruserIdArr.add(coloruserId)
                            colorplusImgPathArr.add(colorplusImgPath)
                            colorplusImgNameArr.add(colorplusImgName)
                            colorplusImgStyleArr.add(colorplusImgStyle)

                            AutoHome.setColorcody(a!!, color)
                        }

                        AutoHome.setColoruserId(a!!, coloruserIdArr)
                        AutoHome.setColorplusImgPath(a!!, colorplusImgPathArr)
                        AutoHome.setColorplusImgName(a!!, colorplusImgNameArr)
                        AutoHome.setColorplusImgStyle(a!!, colorplusImgStyleArr)

                        val intent = Intent(a!!, ColorRecommendActivity::class.java)
                        startActivity(intent)
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(a!!, "코디를 한개이상 등록해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        val colorRecommend_Request = ColorRecommend_Request(userId!!, responseListener)
        val queue = Volley.newRequestQueue(a!!)
        queue.add(colorRecommend_Request)
    }



}