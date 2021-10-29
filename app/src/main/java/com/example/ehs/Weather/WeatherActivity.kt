package com.example.ehs.Weather

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.fragment_closet.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class WeatherActivity : AppCompatActivity() {
    var getLongitude: Double? = null // 위도
    var getLatitude: Double? = null // 경도

    val hourlyList = mutableListOf<HourlyWeathers>()
    val hourlyadapter = HourlyWeatherAdapter(hourlyList)
    val dailyList = mutableListOf<DailyWeathers>()
    val dailyadapter = DailyWeatherAdapter(dailyList)

    private val weatherList = mutableListOf<Weathers>() // 날씨별 옷차림
    var weathericonI: Int = 0

    lateinit var one: Weathers
    lateinit var twoo: Weathers
    lateinit var three: Weathers
    lateinit var four: Weathers
    lateinit var five: Weathers
    lateinit var six: Weathers
    lateinit var seven: Weathers
    lateinit var eight: Weathers
    lateinit var nine: Weathers
    lateinit var ten: Weathers

    companion object {
        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "29ceebd0914454fbb0684b748f59eade"
        lateinit var city: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        //처음실행되는것들

        getLocation()
        getweather()
        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_weather) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)

        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료


        btn_updateW.setOnClickListener {
            getLocation()
            getweather()
        }


        val Linear = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_weather.layoutManager = Linear
        rv_weather.setHasFixedSize(true)

        val Linear2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_Hweather.layoutManager = Linear2
        rv_Hweather.setHasFixedSize(true)

        val Linear3 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_Dweather.layoutManager = Linear3
        rv_Dweather.setHasFixedSize(true)


    } // oncreate 대괄호

    private fun getLocation() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
        if (Build.VERSION.SDK_INT >= 30 &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@WeatherActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        } else {
            when { //프로바이더 제공자 활성화 여부 체크
                isNetworkEnabled -> {
                    val location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                    getLongitude = location?.longitude!!
                    getLatitude = location.latitude
                    Toast.makeText(this, "날씨를 업데이트합니다.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        "호롤",
                        "죽여라" + "위도" + getLatitude + "경도" + getLongitude + "zz" + gpsLocationListener
                    )

                    val mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
                    var mResultList: List<Address>? = null
                    try {
                        mResultList = mGeoCoder.getFromLocation(
                            getLatitude!!, getLongitude!!, 1
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (mResultList != null) {
                        // 내 주소 가져오기
                        city = mResultList[0].getAddressLine(0)
                        Log.d("내 주소 ", mResultList[0].getAddressLine(0))
                    }
                }
                isGPSEnabled -> {
                    val location =
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                    getLongitude = location?.longitude!!
                    getLatitude = location.latitude
                    Toast.makeText(this, "날씨를 업데이트합니다.", Toast.LENGTH_SHORT).show()
                    Log.d("호롤", "죽여라" + "위도" + getLatitude + "경도" + getLongitude)

                    val mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
                    var mResultList: List<Address>? = null
                    try {
                        mResultList = mGeoCoder.getFromLocation(
                            getLatitude!!, getLongitude!!, 1
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (mResultList != null) {
                        // 내 주소 가져오기
                        city = mResultList[0].getAddressLine(0)
                        Log.d("내 주소 ", city)
                    }
                }
                else -> {

                }
            }
        }
    }

    fun getweather() {
        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call =
            service.getCurrentWeatherData(getLatitude.toString(), getLongitude.toString(), AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("WeatherActivity", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>,
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("Hello", "hi")
                    Log.d("WeatherActivity", "result: " + weatherResponse.toString())
                    val cTemp = weatherResponse!!.current!!.Ctemp - 273.15  //켈빈을 섭씨로 변환
                    val minTemp = weatherResponse!!.daily[0].Dtemp!!.Dmin - 273.15
                    val maxTemp = weatherResponse!!.daily[0].Dtemp!!.Dmax - 273.15

                    Log.d("아이콘",
                        asdf(weatherResponse.hourly[0].hweather[0].icon.toString()).toString())


                    img_weather.setImageResource(asdf(weatherResponse.hourly[0].hweather[0].icon.toString()))


                    for (i in 0..12) {
                        var hicon = weatherResponse.hourly[i].hweather[0].icon.toString()
                        var hicon1 = asdf(hicon)
                        Log.d("시간0", hicon)


                        var hTemp = weatherResponse.hourly[i].Htemp - 273.15
                        var hTemp2 = hTemp.roundToInt().toString() + "\u00B0"

                        var htime = weatherResponse.hourly[i].Hdt * 1000L
                        val sdf = SimpleDateFormat("HH시")
                        var htime2 = sdf.format(htime).toString()
                        Log.d("시간", htime2)

                        var hweather = HourlyWeathers(hicon1, hTemp2, htime2)
                        hourlyList.add(hweather)
                    } // 1시간마다

                    for (j in 1..5) {
                        var dicon = weatherResponse.daily[j].dweather[0].icon.toString()
                        var dicon2 = asdf(dicon)

                        var dTempmin = weatherResponse!!.daily[j].Dtemp!!.Dmin - 273.15
                        var dTempmin2 = dTempmin.roundToInt().toString() + "\u00B0"

                        var dTempmax = weatherResponse.daily[j].Dtemp!!.Dmax - 273.15
                        var dTempmax2 = dTempmax.roundToInt().toString() + "\u00B0"

                        var dtime = weatherResponse.daily[j].Ddt * 1000L
                        val sdf = SimpleDateFormat("E")
                        var dtime2 = sdf.format(dtime).toString()
                        Log.d("시간2", dtime2)


                        var dweather = DailyWeathers(dicon2, dTempmin2, dTempmax2, dtime2)
                        dailyList.add(dweather)
                    } // 일별

                    rv_Hweather.adapter = hourlyadapter
                    hourlyadapter.notifyDataSetChanged()

                    rv_Dweather.adapter = dailyadapter
                    dailyadapter.notifyDataSetChanged()

                    var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기

                    city = cutting[1] + " " + cutting[2] + " " + cutting[3]

                    Log.d("잘리냐", "어케생겨먹었니" + city)

                    val intcTemp = cTemp.roundToInt().toString()
                    val intMinTemp = minTemp.roundToInt()
                    val intMaxTemp = maxTemp.roundToInt()

                    viewclothes(intcTemp)


                    tv_city.text = city
                    tv_MinMaxTemp.text =
                        intMinTemp.toString() + "\u00B0" + "/ " + intMaxTemp.toString() + "\u00B0"
                    tv_cTemp.text = intcTemp + "\u00B0" + "C"
                }
            }
        })
    }

    fun asdf(weathericonS: String): Int {
        when (weathericonS) { // 날씨에 맞는 아이콘 출력
            "01d" -> weathericonI = R.drawable.ic_sun
            "01n" -> weathericonI = R.drawable.ic_sun_night
            "02d", "03d" -> weathericonI = R.drawable.ic_sun_c
            "02n", "03n" -> weathericonI = R.drawable.ic_suncloud_night
            "04d" -> weathericonI = R.drawable.ic_sun_c_many
            "04n" -> weathericonI = R.drawable.ic_cloud_many
            "09d", "09n", "10d", "10n" -> weathericonI = R.drawable.ic_rain
            "11d", "11n" -> weathericonI = R.drawable.ic_thunder
            "13d", "13n" -> weathericonI = R.drawable.ic_snow
            "50n", "50d" -> weathericonI = R.drawable.ic_mist
        }
        return weathericonI
    }

    fun addclothes() {
        weatherList.add(one)
        weatherList.add(twoo)
        weatherList.add(three)
        weatherList.add(four)
        weatherList.add(five)
        weatherList.add(six)
        weatherList.add(seven)
        weatherList.add(eight)
        weatherList.add(nine)
        weatherList.add(ten)


        val adapter = WeatherAdapter(weatherList)
        rv_weather.adapter = adapter
        adapter.notifyDataSetChanged()

    } // weathers에 이미지를 추가해줌

    fun viewclothes(intcTemp2: String) {
        Log.d("농심 짜파게티~", intcTemp2)

        when (intcTemp2) {
            "34", "35", "36", "37", "38", "39", "40", "41", "42" -> {
                one = Weathers(R.drawable.weather_i_dress, "원피스")
                twoo = Weathers(R.drawable.weather_i_croptop, "크롭탑")
                three = Weathers(R.drawable.weather_i_shortpants, "반바지")
                four = Weathers(R.drawable.weather_i_swimming, "수영복")
                five = Weathers(R.drawable.weather_i_jjori, "슬리퍼")
                six = Weathers(R.drawable.weather_i_tee, "반팔")
                seven = Weathers(R.drawable.weather_i_bluetop, "나시")
                eight = Weathers(R.drawable.weather_i_skirt, "치마")
                nine = Weathers(R.drawable.weather_i_umbrella, "양산")
                ten = Weathers(R.drawable.weather_i_top, "나시")

                addclothes()

            }

            "28", "29", "30", "31", "32", "33" -> {
                one = Weathers(R.drawable.weather_a_dress, "원피스")
                twoo = Weathers(R.drawable.weather_a_shortpants, "반바지")
                three = Weathers(R.drawable.weather_a_sandel, "샌들")
                four = Weathers(R.drawable.weather_a_croptee, "크롭티")
                five = Weathers(R.drawable.weather_a_ndress, "원피스")
                six = Weathers(R.drawable.weather_a_tee, "나시")
                seven = Weathers(R.drawable.weather_a_denim, "청바지")
                eight = Weathers(R.drawable.weather_a_pinktee, "반팔")
                nine = Weathers(R.drawable.weather_a_whitepants, "린넨바지")
                ten = Weathers(R.drawable.weather_a_shortshirt, "반팔셔츠")

                addclothes()
            }

            "23", "24", "25", "26", "27" -> {
                one = Weathers(R.drawable.weather_b_denim_short, "반바지")
                twoo = Weathers(R.drawable.weather_b_sandel, "샌들")
                three = Weathers(R.drawable.weather_b_skirt, "치마")
                four = Weathers(R.drawable.weather_b_tee, "반팔")
                five = Weathers(R.drawable.weather_b_shirt, "얇은 셔츠")
                six = Weathers(R.drawable.weather_b_begietee, "카라티 셔츠")
                seven = Weathers(R.drawable.weather_b_dress, "원피스")
                eight = Weathers(R.drawable.weather_b_cottenpants, "면바지")
                nine = Weathers(R.drawable.weather_b_piktee, "카라티")
                ten = Weathers(R.drawable.weather_b_pinkshirt, "얇은 셔츠")

                addclothes()
            }
            "20", "21", "22" -> {
                one = Weathers(R.drawable.weather_c_dress, "원피스")
                twoo = Weathers(R.drawable.weather_c_shirtss, "셔츠")
                three = Weathers(R.drawable.weather_c_skirt, "원피스")
                four = Weathers(R.drawable.weather_c_pants, "청바지")
                five = Weathers(R.drawable.weather_c_blouse, "블라우스")
                six = Weathers(R.drawable.weather_c_shirts, "셔츠")
                seven = Weathers(R.drawable.weather_c_longskirt, "치마")
                eight = Weathers(R.drawable.weather_c_tee, "긴팔")
                nine = Weathers(R.drawable.weather_c_denim, "청바지")
                ten = Weathers(R.drawable.weather_c_shortskirt, "치마")

                addclothes()
            }
            "17", "18", "19" -> {
                one = Weathers(R.drawable.weather_d_cardigun, "가디건")
                twoo = Weathers(R.drawable.weather_d_denim, "청바지")
                three = Weathers(R.drawable.weather_d_ymantoman, "맨투맨")
                four = Weathers(R.drawable.weather_d_longskirt, "치마")
                five = Weathers(R.drawable.weather_d_dress, "원피스")
                six = Weathers(R.drawable.weather_d_pants, "바지")
                seven = Weathers(R.drawable.weather_d_mantoman, "맨투맨")
                eight = Weathers(R.drawable.weather_d_denimskirt, "치마")
                nine = Weathers(R.drawable.weather_d_knitedress, "원피스")
                ten = Weathers(R.drawable.weather_d_gcardigun, "가디건")

                addclothes()

            }
            "12", "13", "14", "15", "16" -> {
                one = Weathers(R.drawable.weather_e_cardigun, "가디건")
                twoo = Weathers(R.drawable.weather_e_yjacket, "야상")
                three = Weathers(R.drawable.weather_e_boots, "부츠")
                four = Weathers(R.drawable.weather_e_skirt, "치마")
                five = Weathers(R.drawable.weather_e_tjacket, "자켓")
                six = Weathers(R.drawable.weahter_e_denim, "청바지")
                seven = Weathers(R.drawable.weather_e_dress, "원피스")
                eight = Weathers(R.drawable.weather_e_jumper, "점퍼")
                nine = Weathers(R.drawable.weather_e_bskirt, "치마")
                ten = Weathers(R.drawable.weather_e_jacket, "블루종")

                addclothes()

            }
            "9", "10", "11" -> {
                one = Weathers(R.drawable.weather_f_tjacket, "트렌치코트")
                twoo = Weathers(R.drawable.weather_f_hood, "후드집업")
                three = Weathers(R.drawable.waether_f_knite, "니트")
                four = Weathers(R.drawable.weather_f_ya, "야상")
                five = Weathers(R.drawable.weather_f_jacket, "자켓")
                six = Weathers(R.drawable.weather_f_skirt, "치마")
                seven = Weathers(R.drawable.weather_f_knite, "니트")
                eight = Weathers(R.drawable.weather_f_dress, "원피스")
                nine = Weathers(R.drawable.weather_f_denim, "청바지")
                ten = Weathers(R.drawable.weather_f_skirtt, "치마")

                addclothes()

            }
            "8", "7", "6", "5" -> {
                one = Weathers(R.drawable.weather_g_popojumper, "양털후리스")
                twoo = Weathers(R.drawable.weather_g_dresss, "원피스")
                three = Weathers(R.drawable.weather_g_keckknite, "반폴라")
                four = Weathers(R.drawable.weather_g_leatherjacket, "가죽자켓")
                five = Weathers(R.drawable.weather_g_knite, "니트")
                six = Weathers(R.drawable.weather_g_jumper, "항공점퍼")
                seven = Weathers(R.drawable.weather_g_pants, "청바지")
                eight = Weathers(R.drawable.weather_g_coat, "코트")
                nine = Weathers(R.drawable.weather_g_skirt, "치마")
                ten = Weathers(R.drawable.weather_g_dress, "원피스")

                addclothes()

            }
            "4", "3", "2", "1", "0", "-1", "-2" -> {
                one = Weathers(R.drawable.weather_h_knitey, "목폴라")
                twoo = Weathers(R.drawable.weather_h_coatdd, "떡볶이코트")
                three = Weathers(R.drawable.weather_h_keck, "목도리")
                four = Weathers(R.drawable.weather_h_padding, "패딩")
                five = Weathers(R.drawable.weather_h_knite, "니트")
                six = Weathers(R.drawable.weather_h_skirt, "치마")
                seven = Weathers(R.drawable.weather_h_jackett, "무스탕")
                eight = Weathers(R.drawable.weather_h_coat, "코트")
                nine = Weathers(R.drawable.weather_h_pants, "청바지")
                ten = Weathers(R.drawable.weather_h_skirtt, "치마")

                addclothes()

            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    val gpsLocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val provider: String = location.provider
            val longitude: Double = location.longitude
            val latitude: Double = location.latitude
            val altitude: Double = location.altitude
        }

        //아래 3개함수는 형식상 필수부분
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    interface WeatherService {

        @GET("data/2.5/onecall")
        fun getCurrentWeatherData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String,

            ):
                Call<WeatherResponse>
    }

    class WeatherResponse() {
        @SerializedName("current")
        var current: Current? = null

        @SerializedName("hourly")
        var hourly = ArrayList<Hourly>()

        @SerializedName("daily")
        var daily = ArrayList<Daily>()
    }

    class Current {
        @SerializedName("dt")
        var Cdt: Long = 0

        @SerializedName("temp")
        var Ctemp: Float = 0.toFloat()

        @SerializedName("weather")
        var weather = ArrayList<Weather>()
    }

    class Weather {
        @SerializedName("id")
        var id: Int = 0

        @SerializedName("main")
        var main: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("icon")
        var icon: String? = null
    }

    class Hourly {
        @SerializedName("dt")
        var Hdt: Long = 0

        @SerializedName("temp")
        var Htemp: Float = 0.toFloat()

        @SerializedName("weather")
        var hweather = ArrayList<Weather>()
    }

    class Daily {
        @SerializedName("dt")
        var Ddt: Long = 0

        @SerializedName("temp")
        var Dtemp: Temp? = null

        @SerializedName("weather")
        var dweather = ArrayList<Weather>()
    }

    class Temp {
        @SerializedName("min")
        var Dmin: Float = 0.toFloat()

        @SerializedName("max")
        var Dmax: Float = 0.toFloat()

        @SerializedName("morn")
        var morn: Float = 0.toFloat()

    }
}
