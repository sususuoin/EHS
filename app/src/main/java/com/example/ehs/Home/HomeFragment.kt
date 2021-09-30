package com.example.ehs.Home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.AI.Main_AIActivity
import com.example.ehs.Calendar.AutoCalendar
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.Closet.ClosetFragment
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.example.ehs.Weather.WeatherActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var a: Activity? = null
    val now: LocalDateTime = LocalDateTime.now()
    var Strnow = now?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    // lateinit var text: TextView
    lateinit var aibtn : ImageView
    lateinit var calendarbtn: ImageButton
    lateinit var weathergo : ImageButton
    lateinit var updatebtn : ImageButton
    lateinit var crecyclerview: androidx.recyclerview.widget.RecyclerView
    lateinit var img_weather: ImageView
    lateinit var tv_weathergo : TextView
    lateinit var tv_cityH : TextView
    lateinit var tv_MinMaxH : TextView
    lateinit var tv_cTempH : TextView


    // 요일 받아오기
    var sun: String? = null
    var mon: String? = null
    var tue: String? = null
    var wed: String? = null
    var thu: String? = null
    var fri: String? = null
    var sat: String? = null

    var getLatitude : String = ""
    var getLongitude : String = ""

    var cAdapter : CalendarlistAdapter? = null


    companion object {
        const val TAG : String = "홈 프레그먼트"
        fun newInstance() : HomeFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return HomeFragment()
        }
        var calendarNameArr = ArrayList<String>()
        var calendarYearArr = ArrayList<String>()
        var calendarMonthArr = ArrayList<String>()
        var calendarDayArr = ArrayList<String>()
    }

    // 프래그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
        AndroidThreeTen.init(a)

        getLongitude = AutoHome.getLongitude(a!!)
        getLatitude = AutoHome.getLatitude(a!!)

        calendarNameArr = AutoCalendar.getCalendarName(a!!)
        calendarYearArr = AutoCalendar.getCalendarYear(a!!)
        calendarMonthArr = AutoCalendar.getCalendarMonth(a!!)
        calendarDayArr = AutoCalendar.getCalendarDay(a!!)


//
//        // MainActivity로부터 위도, 경도 받아오기
//        arguments?.let {
//            getLatitude = it.getDouble("Latitude")
//            getLongitude = it.getDouble("Longitude")
//        }
//        Log.d("HomeFragment", "위도 : ${getLatitude}")
//        Log.d("HomeFragment", "경도 : ${getLongitude}")


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



        // 변수 선언
        img_weather = view.findViewById(R.id.img_weatherH)
        img_weather.setImageResource(R.drawable.ic_add)
        calendarbtn = view.findViewById(R.id.btn_calendar)
        weathergo = view.findViewById(R.id.btn_weathergo)
        updatebtn = view.findViewById(R.id.btn_updateH)
        aibtn = view.findViewById(R.id.btn_goAI)
        tv_weathergo = view.findViewById(R.id.tv_weathergo)
        tv_cityH = view.findViewById(R.id.tv_cityH)
        tv_MinMaxH = view.findViewById(R.id.tv_MinMaxH)
        tv_cTempH = view.findViewById(R.id.tv_cTempH)

        calendarbtn.setOnClickListener{
            val intent = Intent(a, CalendarActivity::class.java)
            startActivity(intent)
        }
        weathergo.setOnClickListener{
            val intent = Intent(a, WeatherActivity::class.java)
            startActivity(intent)
        }
        tv_weathergo.setOnClickListener{
            val intent = Intent(a, WeatherActivity::class.java)
            startActivity(intent)
        }
        aibtn.setOnClickListener {
            val intent = Intent(a, Main_AIActivity::class.java)
            startActivity(intent)
        }

        updatebtn.setOnClickListener {
            Toast.makeText(a, "날씨 업데이트", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).getLocation()

            getLongitude = AutoHome.getLongitude(a!!)
            getLatitude = AutoHome.getLatitude(a!!)

            // MainActivity로부터 위도, 경도 받아오기
            Log.d("HomeFragment", "위도 : ${getLatitude}")
            Log.d("HomeFragment", "경도 : ${getLongitude}")
            getweather()
        }

        /**
         * 캘린더 리사이클러뷰 객체
         */
        var calendarList = arrayListOf<Calendarlist>(
            Calendarlist(sun!!, "일", ""),
            Calendarlist(mon!!, "월", ""),
            Calendarlist(tue!!, "화", ""),
            Calendarlist(wed!!, "수", ""),
            Calendarlist(thu!!, "목", ""),
            Calendarlist(fri!!, "금", ""),
            Calendarlist(sat!!, "토", ""),
            Calendarlist(" 캘린더로 이동", "", "ic_right")
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
        getweather()

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

    fun getweather () {
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
                response: Response<WeatherActivity.WeatherResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("HomeFragment", "result: " + weatherResponse.toString())
                    val cTemp = weatherResponse!!.main!!.temp - 273.15  //켈빈을 섭씨로 변환
                    val minTemp = weatherResponse!!.main!!.temp_min - 273.15
                    val maxTemp = weatherResponse!!.main!!.temp_max - 273.15

//                    var cutting = WeatherActivity.city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기
//                    Log.d("잘리냐", "어케생겨먹었니" + WeatherActivity.city)
//                    WeatherActivity.city = cutting[1]+" "+cutting[2]+" "+cutting[3]
//                    Log.d("잘리냐", "어케생겨먹었니" + WeatherActivity.city)

                    val intcTemp = cTemp.roundToInt()
                    val intMinTemp = minTemp.roundToInt()
                    val intMaxTemp = maxTemp.roundToInt()
                    val weatherIMG = weatherResponse!!.weather!!.get(0).icon.toString()

                    when (weatherIMG) { // 날씨에 맞는 아이콘 출력
                        "01d" -> img_weather.setImageResource(R.drawable.ic_sun)
                        "01n" -> img_weather.setImageResource(R.drawable.ic_sun_night)
                        "02d" -> img_weather.setImageResource(R.drawable.ic_sun_c)
                        "02n" -> img_weather.setImageResource(R.drawable.ic_suncloud_night)
                        "03n", "03d", "04d", "04n" -> img_weather.setImageResource(R.drawable.ic_cloud_many)
                        "09d", "09n", "10d", "10n" -> img_weather.setImageResource(R.drawable.ic_rain)
                        "11d", "11n" -> img_weather.setImageResource(R.drawable.ic_thunder)
                        "13d", "13n" -> img_weather.setImageResource(R.drawable.ic_snow)
                        "50n", "50d" -> img_weather.setImageResource(R.drawable.ic_mist)
                    }

                    tv_cityH.text =AutoHome.getLocation(a!!)
                    tv_MinMaxH.text = intMinTemp.toString() + "\u00B0" + "/ " + intMaxTemp.toString() + "\u00B0"
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

        var token = startDt.split('-');
        sun = token[2]

        token = twoDt.split('-');
        mon = token[2]

        token = threeDt.split('-');
        tue = token[2]

        token = fourDt.split('-');
        wed = token[2]

        token = fiveDt.split('-');
        thu = token[2]

        token = sixDt.split('-');
        fri = token[2]

        token = endDt.split('-');
        sat = token[2]

        Log.d(TAG, "특정 날짜 = [$eventDate] >> 시작 날짜 = [$startDt], 종료 날짜 = [$endDt], 두번째 = [$twoDt]")
        Log.d("일", "요일" + sun)
        Log.d("일", "요일" + mon)
    }



}