package com.example.ehs.Mypage

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
        Log.d("마이페지이1111", userId)
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

        MainActivity.homeProgressDialog?.dismiss()

        setupPieChart()
        loadPieChartData()
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

        view.btn_modify.setOnClickListener {
            val intent = Intent(context, UserModifyActivity::class.java)
            startActivity(intent)
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

        //fragment1의 TextView에 전달 받은 text 띄우기
        view.tv_id.text = userId
        view.tv_name.text = userName
        view.tv_name2.text = userName
        view.tv_name3.text = userName
        view.tv_email.text = userEmail
        view.tv_level.text = userLevel

//        iv_profileimg.setImageResource(R.drawable.exfirst)
        view.iv_profileimg.setImageBitmap(userProfile)

        Thread {
            for (i in 0..totallikecnt) {
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                view.tv_name3.post(Runnable {
                    view.progressbar.progress = i
                    view.tv_percent.text = "$i%"
                    if (i == 100) {
                        view.tv_name3_2.isVisible=false
                        view.tv_name3.text = "레벨업!! 축하드립니다"
                    }
                })
            }
        }.start()

        if(userLevel2 == "LV1") {
            view.iv_beforeLV.setImageResource(R.drawable.lv1)
            view.iv_afterLV.setImageResource(R.drawable.lv2)

        } else if(userLevel2 == "LV2") {
            view.iv_beforeLV.setImageResource(R.drawable.lv2)
            view.tv_beforeLV.text = "LV2"
            view.iv_afterLV.setImageResource(R.drawable.lv3)
            view.tv_afterLV.text = "LV3"

        } else if(userLevel2 == "LV3") {
            view.iv_beforeLV.setImageResource(R.drawable.lv3)
            view.tv_beforeLV.text = "LV3"
            view.iv_afterLV.setImageResource(R.drawable.lv4)
            view.tv_afterLV.text = "LV4"

        } else if(userLevel2 == "LV4") {
            view.iv_beforeLV.setImageResource(R.drawable.lv4)
            view.tv_beforeLV.text = "LV4"
            view.iv_afterLV.setImageResource(R.drawable.lv5)
            view.tv_afterLV.text = "LV5"

        } else if(userLevel2 == "LV5"){

            view.tv_level5.isVisible=true

            view.tv_percent.isVisible=false
            view.progressbar.isVisible=false
            view.iv_beforeLV.isVisible=false
            view.tv_beforeLV.isVisible=false
            view.iv_afterLV.isVisible=false
            view.tv_afterLV.isVisible=false
        }

        return view
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

    private fun setupPieChart() {
        view?.piechart_mypage!!.isDrawHoleEnabled = true
        view?.piechart_mypage!!.setUsePercentValues(true)
        view?.piechart_mypage!!.setEntryLabelTextSize(10f)
        view?.piechart_mypage!!.setEntryLabelColor(Color.BLACK)
        view?.piechart_mypage!!.setCenterTextSize(15f)
        view?.piechart_mypage!!.description.isEnabled = false
        view?.piechart_mypage!!.setExtraOffsets(5f, 10f, 5f, 5f)
        view?.piechart_mypage!!.transparentCircleRadius = 61f

        val legend: Legend = view?.piechart_mypage!!.legend
        legend.isEnabled = false

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

        val dataSet = PieDataSet(entries, "Expense Category")


        dataSet.colors = colorstwo

        val data: PieData = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(view?.piechart_mypage!!))
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.BLACK)

        view?.piechart_mypage!!.setData(data)
        view?.piechart_mypage!!.invalidate()

        // 애니메이션
        view?.piechart_mypage!!.animateY(1400, Easing.EaseInOutQuad)


    }


}