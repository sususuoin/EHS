package com.example.ehs.Home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ehs.Calendar.CalendarActivity
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat

import java.util.*


class HomeFragment : Fragment() {
    private var a: Activity? = null
    val now: LocalDateTime = LocalDateTime.now()
    var Strnow = now?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    // lateinit var text: TextView
    lateinit var calendarbtn: ImageButton
    lateinit var crecyclerview: androidx.recyclerview.widget.RecyclerView
    lateinit var crecyclerview2: androidx.recyclerview.widget.RecyclerView

    lateinit var img_weather: ImageView

    // 요일 받아오기
    var sun: String? = null
    var mon: String? = null
    var tue: String? = null
    var wed: String? = null
    var thu: String? = null
    var fri: String? = null
    var sat: String? = null


    companion object {
        const val TAG : String = "홈 프레그먼트"
        fun newInstance() : HomeFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return HomeFragment()
        }
    }

    // 프래그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
        AndroidThreeTen.init(a)

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
        //text = view.findViewById(R.id.textView)


        img_weather = view.findViewById(R.id.img_weather)

        img_weather.setImageResource(R.drawable.ic_add)

        calendarbtn = view.findViewById(R.id.btn_calendar)

        calendarbtn.setOnClickListener{
            activity?.let{
                val intent = Intent(context, CalendarActivity::class.java)
                startActivity(intent) }
        }


        /**
         * 캘린더 리사이클러뷰 객체
         */
        var calendarList = arrayListOf<Calendarlist>(
            Calendarlist(sun!!, "일", ""),
            Calendarlist(mon!!, "월", ""),
            Calendarlist(tue!!, "화", ""),
            Calendarlist(wed!!, "수", "")
        )
        var calendarList2 = arrayListOf<Calendarlist>(
            Calendarlist(thu!!, "목", ""),
            Calendarlist(fri!!, "금", ""),
            Calendarlist(sat!!, "토", ""),
            Calendarlist(" 캘린더로 이동", "", "ic_right")

        )


        /**
         * 캘린더 리사이클러 뷰1(일-수)
         */
        val cAdapter = CalendarlistAdapter(a!!, calendarList)
        crecyclerview = view.findViewById(R.id.cRecyclerView)
        crecyclerview.adapter = cAdapter

        // RecyclerView Adapter에서는 레이아웃 매니저 (LayoutManager) 를 설정
        // recyclerView에 setHasFixedSize 옵션에 true 값을 준다.
        val lm = LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false)
        crecyclerview.layoutManager = lm
        crecyclerview.setHasFixedSize(true)

        /**
         * 캘린더 리사이클러 뷰2(목-끝)
         */
        val cAdapter2 = CalendarlistAdapter(a!!, calendarList2)
        crecyclerview2 = view.findViewById(R.id.cRecyclerView2)
        crecyclerview2.adapter = cAdapter2
        // RecyclerView Adapter에서는 레이아웃 매니저 (LayoutManager) 를 설정
        // recyclerView에 setHasFixedSize 옵션에 true 값을 준다.
        val lm2 = LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false)
        crecyclerview2.layoutManager = lm2
        crecyclerview2.setHasFixedSize(true)

        return view
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