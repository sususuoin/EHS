package com.example.ehs

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Calendar.AutoCalendar
import com.example.ehs.Calendar.CalendarCodyServer_Request
import com.example.ehs.Calendar.CalendarSaveCodyActivity
import com.example.ehs.Closet.*
import com.example.ehs.Fashionista.*
import com.example.ehs.Feed.*
import com.example.ehs.Home.AutoHome
import com.example.ehs.Home.CodyRandom_Request
import com.example.ehs.Home.HomeFragment
import com.example.ehs.Login.AutoLogin
import com.example.ehs.Mypage.ClothesColor_Response
import com.example.ehs.Mypage.MypageFragment
import com.example.ehs.ml.ModelColor
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.threeten.bp.LocalDate
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val TAG: String = "메인페이지"
    companion object {
        var mContext: Context? = null
//        var loading : Loading? = null
        lateinit var codycolorRecommend : String

        lateinit var basic_detail_top : String
        lateinit var basic_detail_bottom : String
        lateinit var basic_detail_shoes : String
        lateinit var basic_detail_outer : String
        lateinit var basic_detail_bag : String

        lateinit var mainTodayMonth: LocalDate
        lateinit var maintodaymonth : String

        var homeProgressDialog: ProgressDialog? = null

        var feedNumArr = mutableListOf<String>()
        var feedliketrueArr = mutableListOf<String>()
        var feedlikefalseArr = mutableListOf<String>()

    }

    lateinit var getLatitude : String
    lateinit var getLongitude : String
    lateinit var city: String

    private val REQUEST_ACCESS_FINE_LOCATION = 1000

    lateinit var userId: String

    private var backKeyPressedTime: Long = 0

    override fun onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 1500) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show()
            return
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 1500) {
            moveTaskToBack(true)
            finish()

        }
    }


    // 화면이 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        AndroidThreeTen.init(this)
        mContext = this

        userId = AutoLogin.getUserId(this)

        // 바텀 네비게이션
        bottom_nav.setOnNavigationItemSelectedListener(onBottomNavItemSeletedListener)

        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragments_frame, HomeFragment(), "home").commitAllowingStateLoss()

        homeProgressDialog = ProgressDialog(this)
        homeProgressDialog!!.setCanceledOnTouchOutside(false)
        homeProgressDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //권한설정
        setPermission()
        setLocation_Permission()


        mainTodayMonth = LocalDate.now()
        maintodaymonth = mainTodayMonth.monthValue.toString() // 현재월 가져오기
        CalendarImg(maintodaymonth)

        FashionistaCody()
        Favorite_check()
        Feed_like_check()
        ClosetImg()
        CodyImg()
        FeedImg()

        GetFeedLikeTotalcnt()
        GetColor()

        basic_detail_top = "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='" +userId+ "' OR  clothesCategory='원피스' AND clothesSeason!='여름' AND userId='$userId' ORDER BY rand() LIMIT 1"
        basic_detail_bottom = "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='" +userId+ "' ORDER BY rand() LIMIT 1"
        basic_detail_shoes = "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='" +userId+ "' ORDER BY rand() LIMIT 1"
        basic_detail_outer = "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='" +userId+ "' ORDER BY rand() LIMIT 1"
        basic_detail_bag = "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='" +userId+ "' ORDER BY rand() LIMIT 1"

        CodyRandom(basic_detail_top,
            basic_detail_bottom,
            basic_detail_shoes,
            basic_detail_outer,
            basic_detail_bag)


    }

    // 바텀 네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSeletedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            val fragmentManager: FragmentManager = supportFragmentManager

            // when은 코틀린에서 switch문
            when (it.itemId) {
                R.id.menu_home -> {
                    Log.d(TAG, "MainActivity - 홈버튼 클릭!")

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("home")!!).commit()
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            launch(Dispatchers.Main) {
                                homeProgressDialog!!.show()
                            }
                            delay(1000L)

                            fragmentManager.beginTransaction().add(R.id.fragments_frame, HomeFragment.newInstance(), "home").commit()
                        }

                    }
                    if (fragmentManager.findFragmentByTag("fashionista") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fashionista")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("closet") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("closet")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("feed") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("feed")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("mypage") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("mypage")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("test") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("test")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("cody") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("cody")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("youtube") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("youtube")!!).commit()
                    }

                }
                R.id.menu_fashionista -> {
                    Log.d(TAG, "MainActivity - 패셔니스타 버튼 클릭!")

                    if (fragmentManager.findFragmentByTag("fashionista") != null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("fashionista")!!).commit()
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            launch(Dispatchers.Main) {
                                homeProgressDialog!!.show()
                            }
                            delay(1000L)

                            fragmentManager.beginTransaction().add(R.id.fragments_frame, FashionistaFragment.newInstance(), "fashionista").commit()

                        }

                    }

                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("closet") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("closet")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("feed") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("feed")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("mypage") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("mypage")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("test") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("test")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("cody") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("cody")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("youtube") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("youtube")!!).commit()
                    }

                }
                R.id.menu_closet -> {
                    Log.d(TAG, "MainActivity - 옷장 버튼 클릭!")

                    if (fragmentManager.findFragmentByTag("closet") != null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("closet")!!).commit()
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            launch(Dispatchers.Main) {
                                homeProgressDialog!!.show()
                            }
                            delay(1000L)

                            fragmentManager.beginTransaction().add(R.id.fragments_frame, ClosetFragment(), "closet").commit()
                        }

                    }
                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("fashionista") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fashionista")!!).commit()
                    }

                    if (fragmentManager.findFragmentByTag("feed") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("feed")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("mypage") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("mypage")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("test") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("test")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("cody") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("cody")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("youtube") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("youtube")!!).commit()
                    }

                }
                R.id.menu_feed -> {
                    Log.d(TAG, "MainActivity - 피드 버튼 클릭!")
                    if (fragmentManager.findFragmentByTag("feed") != null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("feed")!!).commit()
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            launch(Dispatchers.Main) {
                                homeProgressDialog!!.show()
                            }
                            delay(1000L)

                            fragmentManager.beginTransaction().add(R.id.fragments_frame, FeedFragment(), "feed").commit()
                        }

                    }
                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("fashionista") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fashionista")!!).commit()
                    }

                    if (fragmentManager.findFragmentByTag("closet") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("closet")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("mypage") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("mypage")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("test") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("test")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("cody") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("cody")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("youtube") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("youtube")!!).commit()
                    }

                }
                R.id.menu_mypage -> {
                    if (fragmentManager.findFragmentByTag("mypage") != null) {
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("mypage")!!).commit()
                    } else {
                        GlobalScope.launch(Dispatchers.Main) {
                            launch(Dispatchers.Main) {
                                homeProgressDialog!!.show()
                            }
                            delay(1000L)

                            fragmentManager.beginTransaction().add(R.id.fragments_frame, MypageFragment(), "mypage").commit()
                        }

                    }
                    if (fragmentManager.findFragmentByTag("home") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("fashionista") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("fashionista")!!).commit()
                    }

                    if (fragmentManager.findFragmentByTag("closet") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("closet")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("feed") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("feed")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("test") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("test")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("cody") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("cody")!!).commit()
                    }
                    if (fragmentManager.findFragmentByTag("youtube") != null) {
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("youtube")!!).commit()
                    }


                }
            } // when문 끝
            true
        }

    fun replaceFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame, fragment!!, "test")
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame, fragment!!)
        fragmentTransaction.commit()
    }

    fun showFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame, fragment!!)
        fragmentTransaction.commit()
    }

    /**
     * 테드 퍼미션 설정
     */
    private fun setPermission() {
        val permission = object : PermissionListener {

            override fun onPermissionGranted() { // 설정해놓은 위험 권한들이 허용되었을 경우 이곳을 수행함.
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) { // 설정해놓은 위험 권한들 중 거부를 한 경우 이곳을 수행함.
                Toast.makeText(this@MainActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permission)
//            .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
            .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).check()
    }


    private fun setLocation_Permission() {
        // OS가 Marshmallow 이상일 경우 권한체크를 해야 합니다.
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {

            // 권한 없음
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            getLocation()
            // ACCESS_FINE_LOCATION 에 대한 권한이 이미 있음.
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // grantResults[0] 거부 -> -1
        // grantResults[0] 허용 -> 0 (PackageManager.PERMISSION_GRANTED)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // ACCESS_FINE_LOCATION 에 대한 권한 획득.
            Log.d("권한", "4")
            getLocation()
        } else {
            // ACCESS_FINE_LOCATION 에 대한 권한 거부.
            Log.d("권한", "5")
        }
    }


    fun getLocation() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )

        } else {

            when { //프로바이더 제공자 활성화 여부 체크
                isNetworkEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                    getLongitude = location?.longitude.toString()
                    getLatitude = location?.latitude.toString()

                    AutoHome.setLongitude(this, getLongitude)
                    AutoHome.setLatitude(this, getLatitude)


                    Log.d(
                        "호롤",
                        "죽여라" + "위도" + getLatitude + "경도" + getLongitude + "zz" + gpsLocationListener
                    )

                    val mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
                    var mResultList: List<Address>? = null
                    try {
                        mResultList = mGeoCoder.getFromLocation(location?.latitude!!,
                            location?.longitude!!,
                            1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (mResultList != null) {
                        // 내 주소 가져오기
                        city = mResultList[0].getAddressLine(0)
                        Log.d("MainActivity 내 주소 ", mResultList[0].getAddressLine(0))
                        var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기
                        city = cutting[1] + " " + cutting[2] + " " + cutting[3]
                        AutoHome.setLocation(this@MainActivity, city)

                    }
                }
                isGPSEnabled -> {
                    val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                    getLongitude = location?.longitude.toString()
                    getLatitude = location?.latitude.toString()

                    Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
                    Log.d("호롤", "죽여라" + "위도" + getLatitude + "경도" + getLongitude)

                    val mGeoCoder = Geocoder(applicationContext, Locale.KOREAN)
                    var mResultList: List<Address>? = null
                    try {
                        mResultList = mGeoCoder.getFromLocation(
                            location?.latitude!!,
                            location?.longitude!!,
                            1
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (mResultList != null) {
                        // 내 주소 가져오기
                        city = mResultList[0].getAddressLine(0)
                        Log.d("내 주소 ", mResultList[0].getAddressLine(0))
                        var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기
                        city = cutting[1] + " " + cutting[2] + " " + cutting[3]
                        AutoHome.setLocation(this@MainActivity, city)

                    }
                }

            }

        }
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

    fun FashionistaCody() {
        var fuserId: String
        var fcodyImgName: String
        var fcodyStyle: String

        var fuserIdArr = ArrayList<String>()
        var fcodyImgNameArr = ArrayList<String>()
        var fcodyStyleArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)

                        fuserId = fuserObject.getString("userId")
                        fcodyImgName = fuserObject.getString("codyImgName")
                        fcodyStyle = fuserObject.getString("codyStyle")

                        fuserIdArr.add(fuserId)
                        fcodyImgNameArr.add(fcodyImgName)
                        fcodyStyleArr.add(fcodyStyle)

                    }

                    AutoPro.setFuserId(this@MainActivity, fuserIdArr)
                    AutoPro.setFcodyImgName(this@MainActivity, fcodyImgNameArr)
                    AutoPro.setFcodyStyle(this@MainActivity, fcodyStyleArr)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val fashionistaCody_Request = FashionistaCody_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(fashionistaCody_Request)
    }


    fun ClosetImg() {

        var cuserId: String
        var cclothesName: String
        var clothesArr = mutableListOf<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")


                    for (i in 0 until arr.length()) {
                        val clothesObject = arr.getJSONObject(i)
                        cuserId = clothesObject.getString("userId")
                        cclothesName = clothesObject.getString("clothesName")


                        clothesArr.add(cclothesName)

                        AutoCloset.setClothesName(this, clothesArr as ArrayList<String>)

                    }
                    if(ClothesSaveActivity.clothesSaveContext!=null) {
                        (ClothesSaveActivity.clothesSaveContext as ClothesSaveActivity).finish()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val closetServer_Request = ClosetServer_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(closetServer_Request)
    }

    fun CodyImg() {

        var cuserId: String
        var codyImgName: String
        var codyArr = mutableListOf<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val codyObject = arr.getJSONObject(i)

                        cuserId = codyObject.getString("userId")
                        codyImgName = codyObject.getString("codyImgName")

                        codyArr.add(codyImgName)
                    }
                    if(CodySaveActivity.codySaveContext!=null) {
                        (CodySaveActivity.codySaveContext as CodySaveActivity).finish()
                    }
                    AutoCody.setCodyName(this, codyArr as ArrayList<String>)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val codyServer_Request = CodyServer_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(codyServer_Request)
    }

    fun FeedImg() {

        var feed_userId: String
        var feedNum: String
        var feed_ImgName: String
        var feed_style: String
        var feed_likecnt : String
        var feed_nolikecnt : String
        var feed_userprofileImg : String

        var feedNumArr = ArrayList<String>()
        var feedIdArr = ArrayList<String>()
        var feedStyleArr = ArrayList<String>()
        var feedImgArr = ArrayList<String>()
        var feedlikecntArr = ArrayList<String>()
        var feednolikecntArr = ArrayList<String>()
        var feed_userprofileImgArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val feedObject = arr.getJSONObject(i)

                        feedNum = feedObject.getString("feedNum")
                        feed_userId = feedObject.getString("feed_userId")
                        feed_ImgName = feedObject.getString("feed_ImgName")
                        feed_style = feedObject.getString("feed_style")
                        feed_likecnt = feedObject.getString("feed_likecnt")
                        feed_nolikecnt = feedObject.getString("feed_nolikecnt")
//                        feed_userprofileImg = feedObject.getString("feed_userprofileImg")

                        feedNumArr.add(feedNum)
                        Log.d("feenNum", feedNum)
                        feedIdArr.add(feed_userId)
                        feedImgArr.add(feed_ImgName)
                        feedStyleArr.add(feed_style)
                        feedlikecntArr.add(feed_likecnt)
                        feednolikecntArr.add(feed_nolikecnt)
//                        feed_userprofileImgArr.add(feed_userprofileImg)

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                AutoFeed.setFeedNum(this, feedNumArr)
                AutoFeed.setFeedId(this, feedIdArr)
                AutoFeed.setFeedName(this, feedImgArr)
                AutoFeed.setFeedStyle(this, feedStyleArr)
                AutoFeed.setFeedLikeCnt(this, feedlikecntArr)
                AutoFeed.setFeednoLikeCnt(this, feednolikecntArr)
//                AutoFeed.setFeeduserprofileImg(this, feed_userprofileImgArr)
                Log.d("1112222", feedlikecntArr.toString())
            }
        val feedServer_Request = FeedServer_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(feedServer_Request)
    }

    fun CalendarImg(todaymonth: String) {
        Log.d("zzghghghazz22", todaymonth)
        var cuserId: String
        var calendarName: String
        var calendarYear: String
        var calendarMonth: String
        var calendarDay: String

        var calendarNameArr = ArrayList<String>()
        var calendarYearArr = ArrayList<String>()
        var calendarMonthArr = ArrayList<String>()
        var calendarDayArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    calendarNameArr.clear()
                    calendarYearArr.clear()
                    calendarMonthArr.clear()
                    calendarDayArr.clear()

                    if(arr.length() != 0) {
                        for (i in 0 until arr.length()) {
                            val codyObject = arr.getJSONObject(i)

                            calendarName = codyObject.getString("calendarName")
                            calendarYear = codyObject.getString("calendarYear")
                            calendarMonth = codyObject.getString("calendarMonth")
                            calendarDay = codyObject.getString("calendarDay")

                            calendarNameArr.add(calendarName)
                            calendarYearArr.add(calendarYear)
                            calendarMonthArr.add(calendarMonth)
                            calendarDayArr.add(calendarDay)
                        }
                    }
                    AutoCalendar.setCalendarName(this, calendarNameArr)
                    AutoCalendar.setCalendarYear(this, calendarYearArr)
                    AutoCalendar.setCalendarMonth(this, calendarMonthArr)
                    AutoCalendar.setCalendarDay(this, calendarDayArr)
                    Log.d("zzghghghazz22", calendarDayArr.toString())
                    Log.d("a호리a", "a호리a")
                    if(CalendarSaveCodyActivity.calendarSaveContext!=null) {
                        Log.d("a호리a11", "a호리a")
                        (CalendarSaveCodyActivity.calendarSaveContext as CalendarSaveCodyActivity).finish()
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val calendarCodyServer_Request = CalendarCodyServer_Request(userId,
            todaymonth,
            responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(calendarCodyServer_Request)
    }

    fun Favorite_check() {

        var cuserId: String
        var favoriteuserId: String
        var favorite_true: String
        var favoriteuserIdArr = mutableListOf<String>()

        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    Log.d("기분크기", arr.length().toString())

                    for (i in 0 until arr.length()) {
                        val Object = arr.getJSONObject(i)

                        cuserId = Object.getString("userId")
                        favoriteuserId = Object.getString("prouserId")
                        favorite_true = Object.getString("favorite_true")

                        favoriteuserIdArr.add(favoriteuserId)
                        Log.d("기분?", favoriteuserId)

                        AutoPro.setFavoriteuserId(this, favoriteuserIdArr as java.util.ArrayList<String>)

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val favoritecheck_Request = FavoriteCheck_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(favoritecheck_Request)
    }

    fun Feed_like_check() {

        var feedNum: String
        var feed_like_true: String
        var feed_like_false: String


        val responseListener: Response.Listener<String?> =
            Response.Listener<String?> { response ->
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")
                    if(arr.length() == 0 ) {
                        feedNumArr.clear()
                        feedliketrueArr.clear()
                        feedlikefalseArr.clear()
                    }
                    for (i in 0 until arr.length()) {
                        val Object = arr.getJSONObject(i)

                        feedNum = Object.getString("feedNum")
                        feed_like_true = Object.getString("feed_like_true")
                        feed_like_false = Object.getString("feed_like_false")

                        feedNumArr.add(feedNum)
                        feedliketrueArr.add(feed_like_true)
                        feedlikefalseArr.add(feed_like_false)

                    }

                    AutoFeed.setFeedNumlike(this, feedNumArr as java.util.ArrayList<String>)
                    AutoFeed.setFeedliketrue(this, feedliketrueArr as java.util.ArrayList<String>)
                    AutoFeed.setFeedlikefalse(this, feedlikefalseArr as java.util.ArrayList<String>)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        val feedLikeCheck_Request = FeedLikeCheck_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(feedLikeCheck_Request)
    }

    fun GetFeedLikeTotalcnt() {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    var jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if (success) {
                        var feedliketotalcnt = jsonObject.getString("count")

                        AutoFeed.setFeedLikeTotalcnt(this@MainActivity, feedliketotalcnt)

                        Log.d("아아잇", feedliketotalcnt.toString())
                    } else {
                        AutoFeed.setFeedLikeTotalcnt(this@MainActivity, "0")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val feedLikeTotalcnt_Request = FeedLikeTotalcnt_Request(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(feedLikeTotalcnt_Request)
    }

    fun GetColor() {
        var cColor : String
        var cCnt : String
        var cColorArr = ArrayList<String>()
        var cCntArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val proObject = arr.getJSONObject(i)

                        cColor = proObject.getString("clothesColor")
                        cCnt = proObject.getString("cnt")

                        cColorArr.add(cColor)
                        cCntArr.add(cCnt)

                        Log.d("유저색", cColor)
                        Log.d("유저색갯수", cCnt)
                        AutoCloset.setClothesColor(this@MainActivity, cColorArr)
                        AutoCloset.setColorCnt(this@MainActivity, cCntArr)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val clothescolorResponse = ClothesColor_Response(userId, responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(clothescolorResponse)
    }

    fun CodyRandom(
        sql_top: String,
        sql_bottom: String,
        sql_shoes: String,
        sql_outer: String,
        sql_bag: String,
    ) {
        Log.d("티피오2", ",,")
        var random_clothesCategory: String
        var random_clothesName: String
        var random_clothesCategory_Detail: String

        var random_clothesCategoryArr = ArrayList<String>()
        var random_clothesNameArr = ArrayList<String>()
        var random_clothesCategory_DetailArr = ArrayList<String>()

        val responseListener: Response.Listener<String?> = Response.Listener<String?> { response ->
            try {

                var jsonObject = JSONObject(response)
                var response = jsonObject.toString()

                val arr: JSONArray = jsonObject.getJSONArray("response")

                if (arr.length() == 0) {
//                        Toast.makeText(this, "등록된 옷이 부족합니다.", Toast.LENGTH_SHORT).show()
                    return@Listener
                } else {
                    for (i in 0 until arr.length()) {
                        val proObject = arr.getJSONObject(i)

                        random_clothesCategory = proObject.getString("clothesCategory")
                        random_clothesName = proObject.getString("clothesName")
                        random_clothesCategory_Detail = proObject.getString("clothesCategory_Detail")

                        random_clothesCategoryArr.add(random_clothesCategory)
                        random_clothesNameArr.add(random_clothesName)
                        random_clothesCategory_DetailArr.add(random_clothesCategory_Detail)

                    }

                    AutoHome.setRandom_clothesCategory(this, random_clothesCategoryArr)
                    AutoHome.setRandom_clothesName(this, random_clothesNameArr)
                    AutoHome.setRandom_clothesCategory_Detail(this,
                        random_clothesCategory_DetailArr)

                }

            } catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "옷을 한개이상 등록해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        val codyRandom_Request = CodyRandom_Request(sql_top,
            sql_bottom,
            sql_shoes,
            sql_outer,
            sql_bag,
            responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(codyRandom_Request)
    }


    fun Codycolor(bitmap: Bitmap) {

        val resized1 : Bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val model = ModelColor.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

        val tbuffer1 = TensorImage.fromBitmap(resized1)
        val byteBuffer1 = tbuffer1.buffer

        inputFeature0.loadBuffer(byteBuffer1)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        var doubleArr = ArrayList<Double>()
        var colorLabelArr = ArrayList<String>()
        colorLabelArr.add("Vivid")
        colorLabelArr.add("Bright")
        colorLabelArr.add("Strong")
        colorLabelArr.add("Deep")
        colorLabelArr.add("Light")
        colorLabelArr.add("Soft")
        colorLabelArr.add("Dull")
        colorLabelArr.add("Dark")
        colorLabelArr.add("Pale")
        colorLabelArr.add("Light Grayish")
        colorLabelArr.add("Grayish")
        colorLabelArr.add("Dark Grayish")
        colorLabelArr.add("Colorless")

        for(i in 0 until outputFeature0.floatArray.size) {
            doubleArr.add(outputFeature0.floatArray[i].div(255.0) * 100)
        }
        var max = doubleArr.indexOf(doubleArr.max())
        codycolorRecommend = colorLabelArr[max]
        Log.d("컬러추천", colorLabelArr[max])
        Log.d("컬러추천", doubleArr.max().toString())

        // Releases model resources if no longer used.
        model.close()

    }



}