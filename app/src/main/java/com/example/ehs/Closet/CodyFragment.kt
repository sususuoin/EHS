package com.example.ehs.Closet

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager

import com.example.ehs.MainActivity
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen

import kotlinx.android.synthetic.main.fragment_cody.*
import kotlinx.android.synthetic.main.fragment_cody.view.*
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class CodyFragment : Fragment() {

    lateinit var codyadapter: CodyAdapter
    val codydatas = mutableListOf<CodyData>()




    private var a: Activity? = null
    val Fragment.packageManager get() = activity?.packageManager // 패키지 매니저 적용

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(
        a!!,
        R.anim.rotate_open_anim
    )}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(
        a!!,
        R.anim.rotate_close_anim
    )}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(
        a!!,
        R.anim.from_bottom_anim
    )}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(
        a!!,
        R.anim.to_bottom_anim
    )}
    private var clicked = false


    companion object {
        const val TAG : String = "로그"
        fun newInstance() : CodyFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return CodyFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CodyFragment - onCreate() called")
        AndroidThreeTen.init(a)

    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "CodyFragment - onAttach() called")


        var a_bitmap : Bitmap? = null
        val uThread: Thread = object : Thread() {
            override fun run() {
                try {

                    //서버에 올려둔 이미지 URL
                    val url = URL("http://54.180.101.123/clothes/16229003541622900353767.JPEG")

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
            var clothes = CodyData(a_bitmap)
            codydatas.add(clothes)
            codydatas.add(clothes)
            codydatas.add(clothes)
            codydatas.add(clothes)
            codydatas.add(clothes)
            codydatas.add(clothes)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }




    }

    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "CodyFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_cody, container, false)
        view.tv_myclothes2.setOnClickListener {
            Log.d("CodyFragment", "내 옷으로 이동")
            (activity as MainActivity?)!!.replaceFragment(ClosetFragment.newInstance())
        }
        view.btn_add2.setOnClickListener { view ->
            Log.d("클릭!!", "플러스 버튼 클릭!!")
            onAddButtonClicked()
        }
        view.btn_addcody.setOnClickListener { view ->
            Log.d("클릭!!", "코디추가 버튼 클릭!!")
            onAddButtonClicked()
        }
        view.tv_addcody.setOnClickListener { view ->
            Log.d("클릭!!", "코디추가 텍스트 클릭!!")
            onAddButtonClicked()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = GridLayoutManager(a, 3)
        recycler_cody.layoutManager = gridLayoutManager
        initRecycler() // 리사이클러
        //recycler_cody.adapter = codyadapter
        //recylerview 이거 fashionista.xml에 있는 변수
    }

    private fun initRecycler() {
        codyadapter = CodyAdapter(a!!)
        recycler_cody.adapter = codyadapter

        codydatas.apply {
            codyadapter.datas = codydatas
            codyadapter.notifyDataSetChanged()

        }
    }

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            btn_addcody.visibility = View.VISIBLE
            tv_addcody.visibility = View.VISIBLE
        }else {
            btn_addcody.visibility = View.INVISIBLE
            tv_addcody.visibility = View.INVISIBLE
            btn_add2.backgroundTintList = AppCompatResources.getColorStateList(a!!, R.color.white)
        }
    }
    fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            btn_addcody.startAnimation(fromBottom)
            tv_addcody.startAnimation(fromBottom)
            btn_add2.startAnimation(rotateOpen)
        } else {
            btn_addcody.startAnimation(toBottom)
            tv_addcody.startAnimation(toBottom)
            btn_add2.startAnimation(rotateClose)
        }
    }

    fun setClickable(clicked: Boolean) {
        if(!clicked) {
            btn_addcody.isClickable = true
            tv_addcody.isClickable = true
        } else {
            btn_addcody.isClickable = false
            tv_addcody.isClickable = false
        }
    }


}