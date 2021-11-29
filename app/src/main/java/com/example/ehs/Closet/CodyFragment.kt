package com.example.ehs.Closet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Calendar.CalendarClothesFragment
import com.example.ehs.Calendar.CalendarClothesListAdapter
import com.example.ehs.Calendar.CalendarCodyFragment
import com.example.ehs.Closet.CodySaveActivity.Companion.codysaveActivity_Dialog
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_cody.*
import kotlinx.android.synthetic.main.fragment_cody.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class CodyFragment : Fragment() {

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

    val codyList = mutableListOf<Cody>()
    var codyArr = ArrayList<String>()
    var codyStyleArr = ArrayList<String>()

    val adapter = CodyListAdapter(codyList)

    var before_page : Int = 0
    var after_page : Int = 0

    companion object {
        var a: Activity? = null
        const val TAG : String = "로그"
        fun newInstance() : CodyFragment { // newInstance()라는 함수를 호출하면 CodyFragment를 반환함
            return CodyFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "CodyFragment - onCreate() called")
        AndroidThreeTen.init(a)

    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "새로고침 실행")
        codyList.clear()
        codysaveActivity_Dialog?.dismiss()

        codyArr = AutoCody.getCodyName(a!!)
        codyStyleArr = AutoCody.getCodyStyle(a!!)

        if (codyArr.size <= 8) {
            after_page = codyArr.size
        } else {
            after_page = 8
        }
        parseResult(before_page, after_page)
        Log.d("ㅁㅁㅁㅁㅁ새로고침222", codyArr.toString())
    }



    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "CodyFragment - onAttach() called")
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

        view.tv_mycody.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    (MainActivity.mContext as MainActivity).CodyImg()
                    MainActivity.homeProgressDialog!!.show()
                }
                delay(500L)
                (activity as MainActivity?)!!.replaceFragment(newInstance())
            }
            Log.d("FeedFragment", "새로고침")
        }

        view.tv_myclothes2.setOnClickListener {
            Log.d("CodyFragment", "내 옷으로 이동")
            if (requireFragmentManager().findFragmentByTag("closet") != null) {
                requireFragmentManager().beginTransaction().show(requireFragmentManager().findFragmentByTag("closet")!!).commit()
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    launch(Dispatchers.Main) {
                        MainActivity.homeProgressDialog!!.show()
                    }
                    delay(1000L)

                    requireFragmentManager().beginTransaction().add(R.id.fragments_frame, ClosetFragment(), "closet").commit()
                }
            }
            if (requireFragmentManager().findFragmentByTag("cody") != null) {
                requireFragmentManager().beginTransaction().hide(requireFragmentManager().findFragmentByTag("cody")!!).commit()
            }
        }
        view.btn_add2.setOnClickListener { view ->
            Log.d("클릭!!", "플러스 버튼 클릭!!")
            onAddButtonClicked()
        }
        view.btn_addcody.setOnClickListener { view ->
            Log.d("클릭!!", "코디추가 버튼 클릭!!")
            onAddButtonClicked()
            val intent = Intent(a, CodyMakeActivity::class.java)
            startActivity(intent)
        }
        view.tv_addcody.setOnClickListener { view ->
            Log.d("클릭!!", "코디추가 텍스트 클릭!!")
            onAddButtonClicked()
            val intent = Intent(context, CodyMakeActivity::class.java)
            startActivity(intent)
        }

        view.nsview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.d("피드갯수", "스크롤")
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                Log.d("피드갯수1", before_page.toString())
                Log.d("피드갯수2", after_page.toString())
                before_page += 8
                after_page += 8
                Log.d("피드갯수3", before_page.toString())
                Log.d("피드갯수4", after_page.toString())
                view.nsprogress.visibility = View.VISIBLE
                if(codyArr.size <= after_page) {
                    Log.d("피드갯수5", codyArr.size.toString())
                    Log.d("피드갯수6", after_page.toString())
                    after_page = codyArr.size
                }
                parseResult(before_page, after_page)

            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(a, 6)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val gridPosition = position % 5
                when (gridPosition) {
                    0, 1, 2, 3, 4 -> return 3
                }
                return 0
            }
        })
        recycler_cody.layoutManager = gridLayoutManager

        recycler_cody.adapter = adapter
        recycler_cody.addItemDecoration(CalendarClothesFragment.ItemDecorator(10))
        adapter.notifyDataSetChanged()
        //recylerview 이거 fashionista.xml에 있는 변수
    }

    private fun parseResult(before_page: Int, after_page: Int) {
        var a_bitmap : Bitmap? = null
        for (i in before_page until after_page) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {
                        Log.d("Closet프래그먼터리스트123", codyArr[i])

                        val url = URL("http://13.125.7.2/img/cody/" + codyArr[i])

                        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                        conn.setDoInput(true)
                        conn.connect()
                        val iss: InputStream = conn.getInputStream()
                        a_bitmap = BitmapFactory.decodeStream(iss)

                    } catch (e: MalformedURLException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            uThread.start() // 작업 Thread 실행

            try {

                uThread.join()

                var cody = Cody(a_bitmap, codyStyleArr[i])
                codyList.add(cody)


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        view?.nsprogress?.visibility = View.GONE
        adapter.notifyDataSetChanged()
        MainActivity.homeProgressDialog?.dismiss()

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