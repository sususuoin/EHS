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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.AutoCloset
import com.example.ehs.Closet.AutoCody
import com.example.ehs.Fashionista.AutoPro
import com.example.ehs.Home.AutoHome
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
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MypageFragment : Fragment() {
    private var a: Context? = null


    lateinit var tv_id: TextView
    lateinit var tv_name: TextView
    lateinit var tv_name2: TextView
    lateinit var tv_email: TextView
    lateinit var tv_level: TextView
    lateinit var iv_profileimg: ImageView
    lateinit var modifybtn: ImageButton
    lateinit var pieChart: PieChart


    var userColorArr = ArrayList<String>()
    var userColorCntArr = ArrayList<String>()

    lateinit var userId :String
    lateinit var userPw :String
    lateinit var userName :String
    lateinit var userEmail :String
    lateinit var userBirth :String
    lateinit var userGender :String
    lateinit var userLevel :String
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
        userLevel = AutoLogin.getUserLevel(a!!)

        var userProfileImg = AutoLogin.getUserProfileImg(a!!)
        userProfile = StringToBitmap(userProfileImg)


        getColor()


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

        tv_id = view.findViewById(R.id.tv_id)
        tv_name = view.findViewById(R.id.tv_name)
        tv_name2 = view.findViewById(R.id.tv_name2)
        tv_email = view.findViewById(R.id.tv_email)
        tv_level = view.findViewById(R.id.tv_level)
        iv_profileimg = view.findViewById(R.id.iv_profileimg)


//        userProfile = resize(userProfile!!)


        //fragment1의 TextView에 전달 받은 text 띄우기
        tv_id.text = userId
        tv_name.text = userName
        tv_name2.text = userName
        tv_email.text = userEmail
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
//        val colors: ArrayList<Int> = ArrayList()
//
//        for (color in ColorTemplate.MATERIAL_COLORS) {
//            colors.add(color)
//        }
//        for (color in ColorTemplate.VORDIPLOM_COLORS) {
//            colors.add(color)
//        }

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


    fun getColor() {
        var cColor : String
        var cCnt : String
        var cColorArr = mutableListOf<String>()
        var cCntArr = mutableListOf<String>()

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
                        AutoCloset.setClothesColor(a!!, cColorArr as ArrayList<String>)
                        AutoCloset.setColorCnt(a!!, cCntArr as ArrayList<String>)
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val clothescolorResponse = ClothesColor_Response(userId, responseListener)
        val queue = Volley.newRequestQueue(a)
        queue.add(clothescolorResponse)

    }
}