package com.example.ehs.Closet

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ehs.Fashionista.FavoriteFragment
import com.example.ehs.MainActivity
import com.example.ehs.databinding.ActivityCodyMakeBinding
import kotlinx.android.synthetic.main.activity_cody_make.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class CodyMakeActivity : AppCompatActivity(), View.OnTouchListener {

    private lateinit var binding : ActivityCodyMakeBinding
    var clickCount = 0

    private var Position_X = 0
    private var Position_Y = 0
    var startTime: Long = 0

    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    private lateinit var iv : ImageView // 우선 이미지 만들어 놓기 나중에 이걸 터치 된 걸로 바꿔야 하는데 지금은 모름

    val codyMakeList = mutableListOf<Clothes>()
    val adapter = CodyMakeListAdapter(codyMakeList)
    var clothesArr2 = ArrayList<String>()

    lateinit var saveBitmap : Bitmap

    companion object {
        var codyContext: Context? = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodyMakeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        codyContext = this

        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.rvCodymake.layoutManager = gridLayoutManager
        binding.rvCodymake.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.setItemClickListener(object : CodyMakeListAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                // 옷 클릭 시 화면에 선택한 옷 추가
                var newclothes: Bitmap? = codyMakeList[position].clothes
                if (newclothes != null) {
                    Add_image(newclothes)
                }

            }
        })

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        setSupportActionBar(toolbar_codymake)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료

        btn_codymade.setOnClickListener{
            ll_codymake.setDrawingCacheEnabled(true)
            ll_codymake.buildDrawingCache()

            //조합한 코디를 캡쳐하여 비트맵으로 변경
            saveBitmap = ll_codymake.getDrawingCache()

            val intent = Intent(this, CodySaveActivity::class.java)
            var stream = ByteArrayOutputStream()
            saveBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            var byteArray: ByteArray = stream.toByteArray()
            intent.putExtra("saveBitmap", byteArray)
            startActivity(intent)
//            finish()

        }

        // 스케일제스쳐 디텍터 인스턴스
        mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        clickCount = 0


        //recylerview 이거 fashionista.xml에 있는 변수

        clothesArr2 = AutoCloset.getClothesName(this)
        Log.d("111111", clothesArr2.toString())

        var a_bitmap : Bitmap? = null
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
                codyMakeList.add(clothes)


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

    }


    private fun Add_image(newclothes: Bitmap) { // 이미지 추가

        try {
            // 만들어진 Bitmap 객체를 이미지뷰에 표시.
            iv = ImageView(this)
            iv!!.setImageBitmap(newclothes)
            val layoutParams = RelativeLayout.LayoutParams(400, 400)
            iv!!.layoutParams = layoutParams
            ll_codymake!!.addView(iv, layoutParams)

            iv!!.setOnTouchListener(this)
            mScaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    
    /**
     * 툴바 뒤로가기 기능
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val X = event.rawX.toInt()
        val Y = event.rawY.toInt()
        val pointerCount = event.pointerCount
        mScaleGestureDetector!!.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
                Position_X = X - layoutParams.leftMargin
                Position_Y = Y - layoutParams.topMargin
            }
            MotionEvent.ACTION_UP -> if (startTime == 0L) {
                startTime = System.currentTimeMillis()
            } else { // 두 번 누르면 삭제
                if (System.currentTimeMillis() - startTime < 200) {
                    val builder = AlertDialog.Builder(this@CodyMakeActivity)
                    builder.setMessage("선택하신 옷을 삭제하시겠습니까?")
                    builder.setPositiveButton("삭제") { dialog, which -> view.visibility = View.GONE }
                    builder.setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
                startTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
            // 이미지 움직이기
            MotionEvent.ACTION_MOVE -> {
                if (pointerCount == 1) {
                    view.y = (event.rawY - view.height / 1).toFloat()
                    view.x = (event.rawX - view.width / 2).toFloat()
                }
            }
        }

// Schedules a repaint for the root Layout.
        ll_codymake!!.invalidate()
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            // ScaleGestureDetector에서 factor를 받아 변수로 선언한 factor에 넣고
            mScaleFactor *= scaleGestureDetector.scaleFactor

            // 최대 10배, 최소 10배 줌 한계 설정
            mScaleFactor = Math.max(0.1f,
                Math.min(mScaleFactor, 10.0f))

            // 이미지뷰 스케일에 적용
            iv!!.scaleX = mScaleFactor
            iv!!.scaleY = mScaleFactor
            return true
        }
    }
}