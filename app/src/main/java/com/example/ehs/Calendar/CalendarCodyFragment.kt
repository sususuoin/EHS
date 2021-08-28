package com.example.ehs.Calendar

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.*
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_calendarcody.*
import kotlinx.android.synthetic.main.fragment_calendarcody.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.collections.ArrayList


class CalendarCodyFragment : Fragment() {

    val calendarcodyList = mutableListOf<Cody>()
    val adapter = CalendarCodyListAdapter(calendarcodyList)
    var codyArr2 = java.util.ArrayList<String>()
    lateinit var userId : String

    companion object {
        var a: Activity? = null
        const val TAG : String = "로그"
        fun newInstance() : CalendarCodyFragment { // newInstance()라는 함수를 호출하면 CodyFragment를 반환함
            return CalendarCodyFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CalendarCodyFragment - onCreate() called")

        AndroidThreeTen.init(a)
        userId = AutoLogin.getUserId(a!!)
        codyArr2 = AutoCody.getCodyName(a!!)


        var a_bitmap : Bitmap? = null

        //clothes테이블에서 나의 데이터가져오기
//        clothesResponse()

        for (i in 0 until codyArr2.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        Log.d("Cody 프래그먼터리스트123", codyArr2[i])

                        //서버에 올려둔 이미지 URL
                        val url = URL("http://13.125.7.2/img/cody/" + codyArr2[i])

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

                var cody = Cody(a_bitmap)
                calendarcodyList.add(cody)


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
        Log.d(TAG, "CalendarCodyFragment - onAttach() called")

    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "CalendarCodyFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_calendarcody, container, false)

        view.tv_clothes.setOnClickListener { view ->
            Log.d("CalendarCodyFragment", "내 옷장으로 선택 이동")
            (activity as CalendarChoiceActivity?)!!.replaceFragment(CalendarClothesFragment.newInstance())
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(a, 2)
        rv_cody.layoutManager = gridLayoutManager
        rv_cody.adapter = adapter

//        adapter.notifyDataSetChanged()
        //recylerview 이거 fashionista.xml에 있는 변수
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

//    fun clothesResponse() {
//
//        var cuserId : String
//        var cColor : String
//        var cColorArr = mutableListOf<String>()
//        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
//            override fun onResponse(response: String?) {
//                try {
//
//                    var jsonObject = JSONObject(response)
//
//                    val arr: JSONArray = jsonObject.getJSONArray("response")
//
//                    for (i in 0 until arr.length()) {
//                        val proObject = arr.getJSONObject(i)
//
//                        cuserId = proObject.getString("userId")
//                        cColor = proObject.getString("clothesColor")
//
//                        cColorArr.add(cColor)
//
//                        //이거해주면 마이페이지프래그먼트랑 겹쳐서 오토클로젯에 하나더 만들어줘야할듯
////                        AutoCloset.setClothesColor(a!!, cColorArr as ArrayList<String>)
//
//                    }
//
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        val clothesResponse = Clothes_Response(userId, responseListener)
//        val queue = Volley.newRequestQueue(a!!)
//        queue.add(clothesResponse)
//
//
//    }


}