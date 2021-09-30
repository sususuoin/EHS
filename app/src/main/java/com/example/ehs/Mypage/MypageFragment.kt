package com.example.ehs.Mypage

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.Calendar.AutoCalendar
import com.example.ehs.Closet.AutoCloset
import com.example.ehs.Closet.AutoCody
import com.example.ehs.Fashionista.AutoPro
import com.example.ehs.Feed.AutoFeed
import com.example.ehs.Home.AutoHome
import com.example.ehs.Login.AutoLogin
import com.example.ehs.Login.LoginActivity
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MypageFragment : Fragment() {
    private var a: Context? = null


    lateinit var tv_id: TextView
    lateinit var tv_name: TextView
    lateinit var tv_name2: TextView
    lateinit var tv_name3: TextView
    lateinit var tv_name3_2: TextView
    lateinit var tv_email: TextView
    lateinit var tv_level: TextView
    lateinit var tv_level5: TextView
    lateinit var iv_profileimg: ImageView
    lateinit var modifybtn: ImageButton
    lateinit var pieChart: PieChart
    lateinit var progressbar: ProgressBar
    lateinit var tv_percent: TextView

    lateinit var iv_beforeLV : ImageView
    lateinit var tv_beforeLV: TextView

    lateinit var iv_afterLV : ImageView
    lateinit var tv_afterLV: TextView

    var userColorArr = ArrayList<String>()
    var userColorCntArr = ArrayList<String>()

    lateinit var userId :String
    lateinit var userPw :String
    lateinit var userName :String
    lateinit var userEmail :String
    lateinit var userBirth :String
    lateinit var userGender :String
    lateinit var userLevel2 :String
    lateinit var userLevel :String
    var totallikecnt : Int = 0

    var userProfile : Bitmap? =null


    companion object {
        const val TAG: String = "마이페이지 프레그먼트"
        fun newInstance(): MypageFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return MypageFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MypageFragment - onCreate() called")

        userColorArr = AutoCloset.getClothesColor(a!!)
        userColorCntArr = AutoCloset.getColorCnt(a!!)
        Log.d("처음배열1", userColorArr.toString())
        Log.d("처음배열1", userColorCntArr.toString())

        userId = AutoLogin.getUserId(a!!)
        userPw = AutoLogin.getUserPw(a!!)
        userName = AutoLogin.getUserName(a!!)
        userEmail = AutoLogin.getUserEmail(a!!)
        userBirth = AutoLogin.getUserBirth(a!!)
        userGender = AutoLogin.getUserGender(a!!)
        userLevel2 = AutoLogin.getUserLevel2(a!!)
        userLevel = AutoLogin.getUserLevel(a!!)
        totallikecnt = AutoFeed.getFeedLikeTotalcnt(a!!).toInt()

        var userProfileImg = AutoLogin.getUserProfileImg(a!!)
        userProfile = StringToBitmap(userProfileImg)

        (activity as MainActivity).GetColor()
    }

    override fun onResume() {
        super.onResume()


    }


    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "MypageFragment - onAttach() called")

        if (context is Activity) {
            a = context
        }
    }


    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "MypageFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_mypage, container, false)
        pieChart = view.findViewById(R.id.piechart_mypage)


        modifybtn = view.findViewById(R.id.btn_modify)
        modifybtn.setOnClickListener {
            activity?.let {
                val intent = Intent(context, UserModifyActivity::class.java)
                startActivity(intent)
            }

        }


        view.btn_logout.setOnClickListener { view ->
            Log.d("클릭!!", "로그아웃 버튼 클릭!!")
            var logoutalert = AlertDialog.Builder(a!!)
            logoutalert.setTitle("로그아웃 확인창")
            logoutalert.setMessage("'Onmonemo'에서 로그아웃 하시겠습니까?")

            // 버튼 클릭시에 무슨 작업을 할 것인가!
            var listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    when (p1) {
                        // 확인 버튼 클릭 시
                        DialogInterface.BUTTON_POSITIVE -> {
                            // 로그아웃 설정

                            AutoLogin.setUserId(a!!, null)
                            AutoLogin.clearUser(a!!)
                            AutoHome.clearHome(a!!)
                            AutoPro.clearPro(a!!)
                            AutoCody.clearCody(a!!)
                            AutoCloset.clearCloset(a!!)
                            AutoFeed.clearFeed(a!!)
                            AutoCalendar.clearCalendar(a!!)

                            //액티비티는 finish() 프래그먼트는 밑에처럼
                            activity?.supportFragmentManager?.beginTransaction()?.remove(this@MypageFragment)?.commit()
                            (MainActivity.mContext as MainActivity).finish()
                            val myintent = Intent(a, LoginActivity::class.java)
                            startActivity(myintent)
                        }
                    }
                }
            }
            logoutalert.setPositiveButton("확인", listener)
            logoutalert.setNegativeButton("취소", null)
            logoutalert.show()
        }

        tv_id = view.findViewById(R.id.tv_id)
        tv_name = view.findViewById(R.id.tv_name)
        tv_name2 = view.findViewById(R.id.tv_name2)
        tv_email = view.findViewById(R.id.tv_email)
        tv_level = view.findViewById(R.id.tv_level)
        iv_profileimg = view.findViewById(R.id.iv_profileimg)
        tv_name3 = view.findViewById(R.id.tv_name3)
        tv_name3_2 = view.findViewById(R.id.tv_name3_2)
        progressbar = view.findViewById(R.id.progressbar)
        tv_level5 = view.findViewById(R.id.tv_level5)
        tv_percent = view.findViewById(R.id.tv_percent)

        iv_beforeLV = view.findViewById(R.id.iv_beforeLV)
        tv_beforeLV = view.findViewById(R.id.tv_beforeLV)
        iv_afterLV = view.findViewById(R.id.iv_afterLV)
        tv_afterLV = view.findViewById(R.id.tv_afterLV)

        //fragment1의 TextView에 전달 받은 text 띄우기
        tv_id.text = userId
        tv_name.text = userName
        tv_name2.text = userName
        tv_name3.text = userName
        tv_email.text = userEmail
        tv_level.text = userLevel

//        iv_profileimg.setImageResource(R.drawable.exfirst)
        iv_profileimg.setImageBitmap(userProfile)

        setupPieChart()
        loadPieChartData()


        Thread {
            for (i in 0..totallikecnt) {
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                tv_name3.post(Runnable {
                    progressbar.progress = i
                    tv_percent.text = "$i%"
                    if (i == 100) {
                        tv_name3_2.isVisible=false
                        tv_name3.text = "레벨업!! 축하드립니다"
                    }
                })
            }
        }.start()

        if(userLevel2 == "LV1") {
            iv_beforeLV.setImageResource(R.drawable.lv1)
            iv_afterLV.setImageResource(R.drawable.lv2)

        } else if(userLevel2 == "LV2") {
            iv_beforeLV.setImageResource(R.drawable.lv2)
            tv_beforeLV.text = "LV2"
            iv_afterLV.setImageResource(R.drawable.lv3)
            tv_afterLV.text = "LV3"

        } else if(userLevel2 == "LV3") {
            iv_beforeLV.setImageResource(R.drawable.lv3)
            tv_beforeLV.text = "LV3"
            iv_afterLV.setImageResource(R.drawable.lv4)
            tv_afterLV.text = "LV4"

        } else if(userLevel2 == "LV4") {
            iv_beforeLV.setImageResource(R.drawable.lv4)
            tv_beforeLV.text = "LV4"
            iv_afterLV.setImageResource(R.drawable.lv5)
            tv_afterLV.text = "LV5"

        } else if(userLevel2 == "LV5"){

            tv_level5.isVisible=true

            tv_percent.isVisible=false
            progressbar.isVisible=false
            iv_beforeLV.isVisible=false
            tv_beforeLV.isVisible=false
            iv_afterLV.isVisible=false
            tv_afterLV.isVisible=false
        }



        return view
    }

    // Fragment 새로고침
    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
        var ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commit()
    }

    fun StringToBitmap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString,
                Base64.DEFAULT) // String 화 된 이미지를  base64방식으로 인코딩하여 byte배열을 만듬
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size) //만들어진 bitmap을 return
        } catch (e: Exception) {
            e.message
            null
        }
    }

    private fun resize(bm: Bitmap): Bitmap {
        var bm: Bitmap = bm
        val config: Configuration = resources.configuration
        bm =
            if (config.smallestScreenWidthDp >= 800)
                Bitmap.createScaledBitmap(bm, 400, 240, true)
            else if (config.smallestScreenWidthDp >= 600)
                Bitmap.createScaledBitmap(bm, 300, 180, true)
            else if (config.smallestScreenWidthDp >= 400)
                Bitmap.createScaledBitmap(bm, 200, 120, true)
            else if (config.smallestScreenWidthDp >= 360)
                Bitmap.createScaledBitmap(bm, 180, 108, true)
            else
                Bitmap.createScaledBitmap(bm, 160, 96, true)
        return bm
    }


    private fun setupPieChart() {
        with(pieChart) {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(10f)
            setEntryLabelColor(Color.BLACK)
            setCenterTextSize(15f)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            transparentCircleRadius = 61f

            val legend: Legend = pieChart.getLegend()
            legend.isEnabled = false


        }
    }


    fun loadPieChartData() {

        val colorstwo: ArrayList<Int> = ArrayList()

        lateinit var colorname : String

        val entries: ArrayList<PieEntry> = ArrayList()
        with(entries) {

            for (i in 0 until userColorArr.size) {

                val f: Float = userColorCntArr[i].toFloat()
                add(PieEntry(f, userColorArr[i]))

                when(userColorArr[i]) {
                    "흰색" -> colorname = "#FFFFFF"
                    "크림" -> colorname = "#fefcec"
                    "연회색" -> colorname = "#e7e7e7"
                    "진회색" -> colorname = "#7a7a7a"
                    "검정" -> colorname = "#000000"

                    "주황" -> colorname = "#fe820d"
                    "베이지" -> colorname = "#e2c79c"
                    "노랑" -> colorname = "#ffe600"
                    "연두" -> colorname = "#c4db88"
                    "하늘" -> colorname = "#c5e2ff"

                    "분홍" -> colorname = "#ff8290"
                    "연분홍" -> colorname = "#fee0de"
                    "초록" -> colorname = "#00a03e"
                    "카키" -> colorname = "#666b16"
                    "파랑" -> colorname = "#1f4ce2"

                    "빨강" -> colorname = "#ed1212"
                    "와인" -> colorname = "#9d2140"
                    "갈색" -> colorname = "#844f1e"
                    "보라" -> colorname = "#7119ac"
                    "네이비" -> colorname = "#060350"
                }


                with(colorstwo) {
                    add(Color.parseColor(colorname))
                }


            }

        }

        val dataSet: PieDataSet = PieDataSet(entries, "Expense Category")


        dataSet.colors = colorstwo

        val data: PieData = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.BLACK)

        pieChart.setData(data)
        pieChart.invalidate()

        // 애니메이션
        pieChart.animateY(1400, Easing.EaseInOutQuad)


    }


}