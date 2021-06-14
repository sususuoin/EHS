package com.example.ehs.Weather

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Insets.add
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.OneShotPreDrawListener.add
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.example.ehs.Feed.Feeds
import com.example.ehs.R
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_fashionista_profile.*
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class WeatherActivity : AppCompatActivity() {
    var getLongitude: Double? = null // 위도
    var getLatitude: Double? = null // 경도
    val weatherList = arrayListOf<Weathers>() // 날씨별 옷차림
    val one = Weathers(R.drawable.weather_dress,"hi")



//    val weatherList =  arrayListOf<Weathers>()
//    val five = Weathers(R.drawable.weather_dress,"hi")





    companion object {
        var BaseUrl = "http://api.openweathermap.org/"
        var AppId = "29ceebd0914454fbb0684b748f59eade"
        lateinit var city: String

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getLocation()
        getweather ()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

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
            getweather ()
        }

//        lm.removeUpdates(gpsLocationListener)



        //----------------------



        val Linear = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)
        rv_weather.layoutManager = Linear
        rv_weather.setHasFixedSize(true)

        rv_weather.adapter = WeatherAdapter(weatherList)

        //--------------------------


    } // oncreate 대괄호

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

    fun getweather () {
        //Create Retrofit Builder
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(getLatitude.toString(), getLongitude.toString(), AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("WeatherActivity", "result :" + t.message)
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("Hello", "hi")
                    Log.d("WeatherActivity", "result: " + weatherResponse.toString())
                    val cTemp = weatherResponse!!.main!!.temp - 273.15  //켈빈을 섭씨로 변환
                    val minTemp = weatherResponse!!.main!!.temp_min - 273.15
                    val maxTemp = weatherResponse!!.main!!.temp_max - 273.15

                    var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기

                    city = cutting[1]+" "+cutting[2]+" "+cutting[3]

                    Log.d("잘리냐", "어케생겨먹었니" + city)

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
                    when (intcTemp) {
                        24, 25, 26,27, 28, 29, 30 -> weatherList.add(one)
                    }
                    tv_city.text = city
                    //tv_city.text = cutting?.subList(2, 6).toString().replace(",", " ").replace("[", " ").replace("]", " ") // []가 같이 출력되어서 []를 공백으로 치환
                    tv_MinMaxTemp.text = intMinTemp.toString() + "\u00B0" + "/ " + intMaxTemp.toString() + "\u00B0"
                    tv_cTemp.text = intcTemp.toString() + "\u00B0" + "C"
                }
            }

        })
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

        @GET("data/2.5/weather")
        fun getCurrentWeatherData(
            @Query("lat") lat: String,
            @Query("lon") lon: String,
            @Query("appid") appid: String
        ):
                Call<WeatherResponse>
    }

    class WeatherResponse() {
        @SerializedName("weather")
        var weather = ArrayList<Weather>()
        @SerializedName("main")
        var main: Main? = null
        @SerializedName("wind")
        var wind: Wind? = null
        @SerializedName("sys")
        var sys: Sys? = null
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

    class Main {
        @SerializedName("temp")
        var temp: Float = 0.toFloat()

        @SerializedName("humidity")
        var humidity: Float = 0.toFloat()

        @SerializedName("pressure")
        var pressure: Float = 0.toFloat()

        @SerializedName("temp_min")
        var temp_min: Float = 0.toFloat()

        @SerializedName("temp_max")
        var temp_max: Float = 0.toFloat()

    }

    class Wind {
        @SerializedName("speed")
        var speed: Float = 0.toFloat()

        @SerializedName("deg")
        var deg: Float = 0.toFloat()
    }

    class Sys {
        @SerializedName("country")
        var country: String? = null

        @SerializedName("sunrise")
        var sunrise: Long = 0

        @SerializedName("sunset")
        var sunset: Long = 0
    }

}
