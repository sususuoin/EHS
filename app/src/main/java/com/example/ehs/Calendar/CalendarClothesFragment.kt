package com.example.ehs.Calendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.AutoCloset
import com.example.ehs.Closet.Clothes
import com.example.ehs.Closet.Clothes_Response
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_calendarclothes.*
import kotlinx.android.synthetic.main.fragment_calendarclothes.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class CalendarClothesFragment : Fragment() {
    val calendarclothesList = mutableListOf<Clothes>()
    val adapter = CalendarClothesListAdapter(calendarclothesList)
    var clothesArr2 = ArrayList<String>()

    var clickList = ArrayList<String>() // 선택된 옷 이름 배열
    var clickimgList = ArrayList<Bitmap>() // 선택된 옷 이미지 배열



    lateinit var userId: String

    companion object {
        var a: Activity? = null
        const val TAG: String = "로그"
        fun newInstance(): CalendarClothesFragment { // newInstance()라는 함수를 호출하면 ClosetFragment를 반환함
            return CalendarClothesFragment()
        }


    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CalendarClothesFragment - onCreate() called")

        AndroidThreeTen.init(a)
        userId = AutoLogin.getUserId(a!!)
        clothesArr2 = AutoCloset.getClothesName(a!!)
        Log.d("111111", clothesArr2.toString())

        var a_bitmap: Bitmap? = null


        //clothes테이블에서 나의 데이터가져오기
        clothesResponse()

        for (i in 0 until clothesArr2.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        Log.d("Closet프래그먼터리스트123", clothesArr2[i])

                        //서버에 올려둔 이미지 URL
                        val url = URL("http://13.125.7.2/img/clothes/" + clothesArr2[i])

                        //Web에서 이미지 가져온 후 ImageView에 지정할 Bitmap 만들기
                        /* URLConnection 생성자가 protected로 선언되어 있으므로
                         개발자가 직접 HttpURLConnection 객체 생성 불가 */
                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        /* openConnection()메서드가 리턴하는 urlConnection 객체는
                        HttpURLConnection의 인스턴스가 될 수 있으므로 캐스팅해서 사용한다*/

                        conn.setDoInput(true) //Server 통신에서 입력 가능한 상태로 만듦
                        conn.connect() //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)
                        val iss: InputStream = conn.getInputStream() //inputStream 값 가져오기
                        a_bitmap = BitmapFactory.decodeStream(iss) // Bitmap으로 반환


                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            uThread.start() // 작업 Thread 실행


            try {

                //메인 Thread는 별도의 작업을 완료할 때까지 대기한다!
                //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다림
                //join() 메서드는 InterruptedException을 발생시킨다.
                uThread.join()

                //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
                //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정

                var clothes = Clothes(a_bitmap)
                calendarclothesList.add(clothes)


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

    }

    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "ClosetFragment - onAttach() called")

    }

    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d(TAG, "ClosetFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_calendarclothes, container, false)

        view.tv_cody.setOnClickListener { view ->
            Log.d("ClosetFragment", "내 코디에서 선택으로 이동")
            (activity as CalendarChoiceActivity?)!!.replaceFragment(CalendarCodyFragment.newInstance())
        }

        view.btn_cancel.setOnClickListener {
            activity?.finish() // 달력에서 옷 만드는 액티비티 끝내기
        }
        return view
    }


    // recyclerview item 간격
    class ItemDecorator(private val divHeight: Int) : RecyclerView.ItemDecoration() {

        @Override
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
            outRect.bottom = divHeight
            outRect.left = divHeight
            outRect.right = divHeight
        }
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_choiceitem.setBackgroundColor(ContextCompat.getColor(a!!, R.color.lightgray)) // 버튼 색깔을 연회색으로 지정
        btn_choiceitem.text = "총 " + clickList.size + "개 아이템 선택"
        btn_choiceitem.isEnabled = false



        val gridLayoutManager = GridLayoutManager(activity, 6)
        gridLayoutManager.setSpanSizeLookup(object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val gridPosition = position % 5
                when (gridPosition) {
                    0, 1, 2, 3, 4 -> return 2
                }
                return 0
            }
        })
        rv_clothes.layoutManager = gridLayoutManager
        rv_clothes.adapter = adapter
        rv_clothes.addItemDecoration(ItemDecorator(10))
        adapter.notifyDataSetChanged()

        // 선택버튼 디폴트 값
        btn_choiceitem.setBackgroundColor(ContextCompat.getColor(a!!, R.color.lightgray)) // 버튼 색깔을 연회색으로 지정
        btn_choiceitem.setTextColor(ContextCompat.getColor(a!!, R.color.darkgray)) // 버튼 텍스트 진회색
        btn_choiceitem.isEnabled = false // 버튼 비활성화
        btn_choiceitem.text = "총 " + clickList.size + "개 아이템 선택"

        /**
         * 리사이클러뷰 아이템 클릭 시 이벤트 발생
         */
        adapter.setItemClickListener(object :
            CalendarClothesListAdapter.OnItemClickListener { // 리사이클러뷰 아이템 클릭 시
            override fun onClick(
                v: View,
                position: Int,
                holder: CalendarClothesListAdapter.ViewHolder,
            ) {
                val item = calendarclothesList[position]
                if (clickList.size == 0) { // 배열 사이즈가 0이라면
                    clickList.add(item.toString()) // 배열에 추가
                    clickimgList.add(item.clothes!!) // 이미지 배열에 추가
                    //BitmapToByteArray(item.clothes!!)?.let { clickListimg.add(it) } // 이미지 배열에 추가
                    holder.itemView.setBackgroundResource(R.drawable.cody_background) // 해제

                    if (clickList.size != 0) {
                        btn_choiceitem.setBackgroundColor(ContextCompat.getColor(a!!,
                            R.color.ourcolor)) // 버튼 색깔 보라색으로 지정
                        btn_choiceitem.setTextColor(ContextCompat.getColor(a!!,
                            R.color.white)) // 버튼 텍스트 하얀색
                        btn_choiceitem.text = "총 " + clickList.size + "개 아이템 선택"
                        btn_choiceitem.isEnabled = true // 버튼 활성화
                    }

                } else {
                    btn_choiceitem.setBackgroundColor(ContextCompat.getColor(a!!,
                        R.color.ourcolor)) // 버튼 색깔 보라색으로 지정
                    btn_choiceitem.setTextColor(ContextCompat.getColor(a!!,
                        R.color.white)) // 버튼 텍스트 하얀색
                    btn_choiceitem.isEnabled = true // 버튼 활성화

                    if (clickList.contains(item.toString())) { // 선택한 배열 안에 클릭 아이템을 포함하고 있다면
                        holder.itemView.setBackgroundResource(R.drawable.button_background) // 테두리 해제
                        holder.itemView.setBackgroundColor(Color.parseColor("#E7E7E7")) // 테두리 하얗게
                        clickList.remove(item.toString()) // 배열에서 삭제
                        clickimgList.remove(item.clothes!!) // 이미지 배열에 추가
                        // BitmapToByteArray(item.clothes!!)?.let { clickListimg.remove(it) } // 이미지 배열에 추가
                        btn_choiceitem.text = "총 " + clickList.size + "개 아이템 선택"
                        if (clickList.size == 0) {
                            btn_choiceitem.setBackgroundColor(ContextCompat.getColor(a!!,
                                R.color.lightgray)) // 버튼 색깔을 연회색으로 지정
                            btn_choiceitem.setTextColor(ContextCompat.getColor(a!!,
                                R.color.darkgray)) // 버튼 텍스트 진회색
                            btn_choiceitem.isEnabled = false // 버튼 비활성화
                        }
                    } else {
                        holder.itemView.setBackgroundResource(R.drawable.cody_background) // 보라 테두리 생성
                        clickList.add(item.toString()) // 배열에 추가
                        clickimgList.add(item.clothes!!) // 이미지 배열에 추가
                        //BitmapToByteArray(item.clothes!!)?.let { clickListimg.add(it) } // 이미지 배열에 추가
                        btn_choiceitem.text = "총 " + clickList.size + "개 아이템 선택"
                    }
                }


            }
        })

        btn_choiceitem.setOnClickListener { // 옷 선택 버튼 클릭 시 액티비티로 이동
            var StringList = ArrayList<String>()
            for(i in 0 until clickimgList.size){
                StringList.add(BitmapToString(clickimgList[i])!!)
            }
            AutoCalendar.setCalendarchoiceImg(a!!, StringList)
            Log.d("로그", StringList.size.toString())

            val intent = Intent(activity, CalendarMakeCodyActivity::class.java)
            startActivity(intent)

        }

    }

    fun BitmapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun getPath(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = a!!.managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // 파일명 찾기
    private fun getName(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val cursor = a!!.managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor
            .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }



    fun clothesResponse() {

        var cuserId: String
        var cColor: String
        var cColorArr = mutableListOf<String>()
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {

                    var jsonObject = JSONObject(response)

                    val arr: JSONArray = jsonObject.getJSONArray("response")

                    for (i in 0 until arr.length()) {
                        val proObject = arr.getJSONObject(i)

                        cuserId = proObject.getString("userId")
                        cColor = proObject.getString("clothesColor")

                        cColorArr.add(cColor)

                        //이거해주면 마이페이지프래그먼트랑 겹쳐서 오토클로젯에 하나더 만들어줘야할듯
//                        AutoCloset.setClothesColor(a!!, cColorArr as ArrayList<String>)

                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
        val clothesResponse = Clothes_Response(userId, responseListener)
        val queue = Volley.newRequestQueue(a!!)
        queue.add(clothesResponse)


    }


}
