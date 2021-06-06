package com.example.ehs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.ClosetFragment
import com.example.ehs.Fashionista.Fashionista
import com.example.ehs.Fashionista.FashionistaFragment
import com.example.ehs.Fashionista.FashionistaList
import com.example.ehs.Fashionista.FashionistaUser_Request
import com.example.ehs.Mypage.MypageFragment
import com.example.ehs.Feed.FeedFragment
import com.example.ehs.Home.AutoHome
import com.example.ehs.Home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {


    val TAG :String = "메인페이지"

    var getLongitude: Double? = null // 위도
    var getLatitude: Double? = null // 경도
    lateinit var city: String


    // 메인액티비티 클래스가 가지고 있는 멤버들
    private lateinit var homeFragment: HomeFragment
    private lateinit var fashionistaFragment: FashionistaFragment
    private lateinit var closetFragment: ClosetFragment
    private lateinit var feedFragment: FeedFragment
    private lateinit var mypageFragment: MypageFragment


    var userId :String? = ""
    var userPw :String? = ""
    var userName :String? = ""
    var userEmail :String? = ""
    var userBirth :String? = ""
    var userGender :String? = ""
    var userLevel :String? = ""
    val bundle = Bundle()

    private var backKeyPressedTime: Long = 0

    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private var toast: Toast? = null

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
            Toast.makeText(this, "이용해 주셔서 감사합니다.", Toast.LENGTH_LONG).show()
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());

        }
    }

    // 화면이 메모리에 올라갔을 때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)


        bundle.putString("userId", userId);
        bundle.putString("userPw", userPw);
        bundle.putString("userName", userName);
        bundle.putString("userEmail", userEmail);
        bundle.putString("userBirth", userBirth);
        bundle.putString("userGender", userGender);
        bundle.putString("userLevel", userLevel);

        // 바텀 네비게이션
        bottom_nav.setOnNavigationItemSelectedListener(onBottomNavItemSeletedListener)

        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit() // add는 프레그먼트 추가해주는 것

        //권한설정
        setPermission()
        FashionistaUser()
        getLocation()

        setDataAtFragment(homeFragment, "홈에 위도, 경도 전달")
    }


    // 바텀 네비게이션 아이템 클릭 리스너 설정
    private val onBottomNavItemSeletedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        // when은 코틀린에서 switch문
        when(it.itemId) {
            R.id.menu_home -> {
                Log.d(TAG, "MainActivity - 홈버튼 클릭!")
                homeFragment = HomeFragment.newInstance()
                replaceFragment(homeFragment)
                setDataAtFragment(homeFragment, "홈에 위도, 경도 전달")
            }
            R.id.menu_fashionista -> {
                Log.d(TAG, "MainActivity - 패셔니스타 버튼 클릭!")
                fashionistaFragment = FashionistaFragment.newInstance()
                replaceFragment(fashionistaFragment)
            }
            R.id.menu_closet -> {
                Log.d(TAG, "MainActivity - 옷장 버튼 클릭!")
                closetFragment = ClosetFragment.newInstance()
                replaceFragment(closetFragment)
            }
            R.id.menu_feed -> {
                Log.d(TAG, "MainActivity - 피드 버튼 클릭!")
                feedFragment = FeedFragment.newInstance()
                replaceFragment(feedFragment)
            }
            R.id.menu_mypage -> {
                Log.d(TAG, "MainActivity - 마이페이지 버튼 클릭!")
                mypageFragment = MypageFragment.newInstance()
                replaceFragment(mypageFragment)
                Log.d(TAG, "아이야 제발로 나와줘라" + userId)

                //mypage 프래그먼트로 번들 전달
                //프래그먼트로 번들 전달
                mypageFragment.arguments = bundle
            }
        } // when문 끝
        true
    }


    fun replaceFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_frame, fragment!!)
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment?) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragments_frame, fragment!!)
        fragmentTransaction.commit()
    }

    //깃허브테스


    //Home 프래그먼트에 데이터 전달하기
    public fun setDataAtFragment(fragment:Fragment, title:String) {
        val bundle = Bundle()
        bundle.putDouble("Latitude", getLatitude!!)
        bundle.putDouble("Longitude", getLongitude!!)
        fragment.arguments = bundle
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
                .setRationaleMessage("카메라 앱을 사용하시려면 권한을 허용해주세요.")
                .setDeniedMessage("권한을 거부하셨습니다. [앱 설정] -> [권한] 항목에서 허용해주세요.")
                .setPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                ).check()
    }

    public fun getLocation() {
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGPSEnabled: Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var isNetworkEnabled: Boolean = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        //매니페스트에 권한이 추가되어 있다해도 여기서 다시 한번 확인해야함
        if (Build.VERSION.SDK_INT >= 30 &&
            ContextCompat.checkSelfPermission(
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
                    val location =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) //인터넷기반으로 위치를 찾음
                    getLongitude = location?.longitude!!
                    getLatitude = location.latitude

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
                        Log.d("MainActivity 내 주소 ", mResultList[0].getAddressLine(0))
                        var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기
                        city = cutting[1]+" "+cutting[2]+" "+cutting[3]

                    }
                }
                isGPSEnabled -> {
                    val location =
                        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) //GPS 기반으로 위치를 찾음
                    getLongitude = location?.longitude!!
                    getLatitude = location.latitude
                    Toast.makeText(this, "현재위치를 불러옵니다.", Toast.LENGTH_SHORT).show()
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
                        Log.d("내 주소 ", mResultList[0].getAddressLine(0))
                        var cutting = city?.split(' ') // 공백을 기준으로 리스트 생성해서 필요한 주소값만 출력하기
                        city = cutting[1]+" "+cutting[2]+" "+cutting[3]

                    }
                }
                else -> {

                }
            }
            AutoHome.setLocation(this@MainActivity, city)
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




    /**
     * 전문가 리스트 출력
     */
    fun FashionistaUser()  {

        var fuserId : String
        var fuserLevel : String

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)
                    var response = jsonObject.toString()

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    Log.d("이이이이잉~~나는 언제잘수있을까 ?", response)
                    Log.d("이이이이잉~~나는 언제잘수있을까123 ?", arr.toString())


                    for (i in 0 until arr.length()) {
                        val fuserObject = arr.getJSONObject(i)
                        Log.d("이이이이잉~~나는sad12  ?", arr[i].toString())

                        fuserId = fuserObject.getString("userId")
                        fuserLevel = fuserObject.getString("userLevel")

                        var fashin = Fashionista(fuserId, fuserLevel)
                        FashionistaList.add(fashin)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val fashionistaUserRequest = FashionistaUser_Request(responseListener)
        val queue = Volley.newRequestQueue(this)
        queue.add(fashionistaUserRequest)




    }


}