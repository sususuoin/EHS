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
import android.icu.lang.UCharacter.IndicPositionalCategory.TOP
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.Size
import android.view.Gravity.TOP
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.TOP
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ehs.Feed.Community
import com.example.ehs.Login.AutoLogin
import com.example.ehs.Login.LoginActivity
import com.example.ehs.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MypageFragment : Fragment() {
    private var a: Context? = null


    lateinit var tv_id: TextView
    lateinit var tv_pw: TextView
    lateinit var tv_name: TextView
    lateinit var tv_email: TextView
    lateinit var tv_birth: TextView
    lateinit var tv_gender: TextView
    lateinit var tv_level: TextView
    lateinit var iv_profileimg: ImageView

    lateinit var modifybtn: ImageButton
    lateinit var pieChart: PieChart

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
                            AutoLogin.clearUser(a!!)
                            val intent = Intent(a, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
            logoutalert.setPositiveButton("확인", listener)
            logoutalert.setNegativeButton("취소", null)
            logoutalert.show()
        }

        tv_id = view.findViewById(R.id.tv_id);
        tv_pw = view.findViewById(R.id.tv_pw);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_birth = view.findViewById(R.id.tv_birth);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_level = view.findViewById(R.id.tv_level);
        iv_profileimg = view.findViewById(R.id.iv_profileimg);

        var userId = AutoLogin.getUserId(a!!)
        var userPw = AutoLogin.getUserPw(a!!)
        var userName = AutoLogin.getUserName(a!!)
        var userEmail = AutoLogin.getUserEmail(a!!)
        var userBirth = AutoLogin.getUserBirth(a!!)
        var userGender = AutoLogin.getUserGender(a!!)
        var userLevel = AutoLogin.getUserLevel(a!!)

        var userProfileImg = AutoLogin.getUserProfileImg(a!!)
        var userProfile = StringToBitmap(userProfileImg)

//        userProfile = resize(userProfile!!)


        //fragment1의 TextView에 전달 받은 text 띄우기
        tv_id.text = userId
        tv_pw.text = userPw
        tv_name.text = userName
        tv_email.text = userEmail
        tv_birth.text = userBirth
        tv_gender.text = userGender
        tv_level.text = userLevel

//        iv_profileimg.setImageResource(R.drawable.exfirst)
        iv_profileimg.setImageBitmap(userProfile)

        setupPieChart()
        loadPieChartData()




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
//            centerText = "방가방가햄토리즈"
            setCenterTextSize(15f)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
//            setHoleColor(Color.WHITE)
            transparentCircleRadius = 61f
//            rawRotationAngle = 50f


//범례(오른쪽 상단)

            val legend: Legend = pieChart.getLegend()
//            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP // 범례
//            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // 화면과 라벨 방향
//            legend.orientation = Legend.LegendOrientation.VERTICAL
//            legend.setDrawInside(false)
            legend.isEnabled = false


        }
    }


    fun loadPieChartData() {

        val entries: ArrayList<PieEntry> = ArrayList()
        with(entries) {

            add(PieEntry(1f, "흰색"))
            add(PieEntry(2f, "크림색"))
            add(PieEntry(4f, "연회색"))
            add(PieEntry(4f, "진회색"))
            add(PieEntry(4f, "검정"))

            add(PieEntry(4f, "주황"))
            add(PieEntry(4f, "베이지"))
            add(PieEntry(4f, "노랑"))
            add(PieEntry(4f, "연두"))
            add(PieEntry(4f, "하늘"))

            add(PieEntry(4f, "분홍"))
            add(PieEntry(4f, "연분홍"))
            add(PieEntry(4f, "초록"))
            add(PieEntry(4f, "카키"))
            add(PieEntry(4f, "파랑"))

            add(PieEntry(4f, "빨강"))
            add(PieEntry(4f, "와인"))
            add(PieEntry(4f, "갈색"))
            add(PieEntry(4f, "보라"))
            add(PieEntry(4f, "네이비"))



        }
        val colors: ArrayList<Int> = ArrayList()
        val colorstwo: ArrayList<Int> = ArrayList()
        with(colorstwo) {

            add(Color.parseColor("#FFFFFFFF")) //white
            add(Color.parseColor("#fefcec")) //cream
            add(Color.parseColor("#e7e7e7")) //lightgray
            add(Color.parseColor("#7a7a7a")) //darkgray
            add(Color.parseColor("#FF000000")) //black

            add(Color.parseColor("#fe820d")) //orange
            add(Color.parseColor("#e2c79c")) //beige
            add(Color.parseColor("#ffe600")) //yellow
            add(Color.parseColor("#c4db88")) //lightgreen
            add(Color.parseColor("#c5e2ff")) //skyblue

            add(Color.parseColor("#ff8290")) //pink
            add(Color.parseColor("#fee0de")) //lightpink
            add(Color.parseColor("#00a03e")) //green
            add(Color.parseColor("#666b16")) //kaki
            add(Color.parseColor("#1f4ce2")) //blue

            add(Color.parseColor("#ed1212")) //red
            add(Color.parseColor("#9d2140")) //wine
            add(Color.parseColor("#844f1e")) //brown
            add(Color.parseColor("#7119ac")) //purple
            add(Color.parseColor("#060350")) //navy





        }


        for (color in ColorTemplate.MATERIAL_COLORS) {
            colors.add(color)
        }
        for (color in ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color)
        }

        val dataSet: PieDataSet = PieDataSet(entries, "Expense Category")


        dataSet.setColors(colorstwo)

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