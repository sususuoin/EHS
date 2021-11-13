package com.example.ehs.Closet

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowInsetsControllerCompat
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import kotlinx.android.synthetic.main.activity_basic_clothes_detail.*
import kotlinx.android.synthetic.main.fragment_closet.*
import org.json.JSONException
import org.json.JSONObject
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException


class BasicClothesDetailActivity : AppCompatActivity() {

    var mDefaultColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_clothes_detail)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }

        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar_basicdetail) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        // 툴바에 뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 여기까지 툴바 설정 완료


        if (intent.hasExtra("a")) {
            Log.d("이미지1, ", "ㅎ2" + intent.getStringExtra("a").toString())
            var one = intent.getStringExtra("a").toString()
            iv_basicdetail.setImageResource(one.toInt())
            Log.d("이미지2, ", "ㅎ2" + intent.getStringExtra("이미지").toString())

        }

        iv_basicdetail.setOnClickListener {
            Log.d("클릭", "1")
            iv_basicdetail.setColorFilter(Color.parseColor("#40ff0000"))
        }
        btn_nocolor.setOnClickListener {
            Log.d("색변경", "원래대로")
            iv_basicdetail.colorFilter = null
        }
        btn_orange.setOnClickListener {
            Log.d("색변경", "orange")
            var color = getColor(R.color.orange)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_pink.setOnClickListener {
            Log.d("색변경", "pink")
            var color = getColor(R.color.pink)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_red.setOnClickListener {
            Log.d("색변경", "red")
            var color = getColor(R.color.red)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#99$color1"))
        }
        btn_cream.setOnClickListener {
            Log.d("색변경", "cream")
            var color = getColor(R.color.cream)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_beige.setOnClickListener {
            Log.d("색변경", "beige")
            var color = getColor(R.color.beige)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_lightpink.setOnClickListener {
            Log.d("색변경", "lightpink")
            var color = getColor(R.color.lightpink)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_brown.setOnClickListener {
            Log.d("색변경", "brown")
            var color = getColor(R.color.brown)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_lightgray.setOnClickListener {
            Log.d("색변경", "lightgray")
            var color = getColor(R.color.lightgray)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_yellow.setOnClickListener {
            Log.d("색변경", "yellow")
            var color = getColor(R.color.yellow)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_green.setOnClickListener {
            Log.d("색변경", "green")
            var color = getColor(R.color.green)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_purple.setOnClickListener {
            Log.d("색변경", "purple")
            var color = getColor(R.color.purple)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_darkgray.setOnClickListener {
            Log.d("색변경", "darkgray")
            var color = getColor(R.color.darkgray)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_lightgreen.setOnClickListener {
            Log.d("색변경", "lightgreen")
            var color = getColor(R.color.lightgreen)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_kaki.setOnClickListener {
            Log.d("색변경", "kaki")
            var color = getColor(R.color.kaki)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_navy.setOnClickListener {
            Log.d("색변경", "navy")
            var color = getColor(R.color.navy)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_black.setOnClickListener {
            Log.d("색변경", "black")
            var color = getColor(R.color.black)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_skyblue.setOnClickListener {
            Log.d("색변경", "skyblue")
            var color = getColor(R.color.skyblue)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_blue.setOnClickListener {
            Log.d("색변경", "blue")
            var color = getColor(R.color.blue)
            var color1 = onColorChanged(color).replace("#", "")
            Log.d("색변경2", color1)

            iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))
        }
        btn_morecolor.setOnClickListener {
            Log.d("색변경", "다른색")
            openColorPicker()
        }
        btn_basicok.setOnClickListener {
            Log.d("기본템등록", "확인")
            iv_basicdetail.setDrawingCacheEnabled(true)
            iv_basicdetail.buildDrawingCache()
            val bitmap = iv_basicdetail.getDrawingCache()
            uploadBitmap(bitmap)
        }
    }

    fun openColorPicker() {

        val colorPicker = AmbilWarnaDialog(this, mDefaultColor, object : OnAmbilWarnaListener {
            override fun onCancel(dialog: AmbilWarnaDialog) {}
            override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
                mDefaultColor = color

                var color1 = onColorChanged(mDefaultColor).replace("#", "")

                iv_basicdetail.setColorFilter(Color.parseColor("#80$color1"))


                Log.d("무슨색?!", onColorChanged(mDefaultColor))

            }
        })
        colorPicker.show()
    }

    /**
     * 툴바 뒤로가기 버튼 액션 설정
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        iv_basicdetail.setImageResource(0)
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    } // 툴바 뒤로가기 액션 설정 끝

    fun onColorChanged(color: Int): String {
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        Log.d("testPrint", hexColor)

        return hexColor
    }

    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun uploadBitmap(bitmap: Bitmap) {
        val clothesUploadRequest: ClothesUpload_Request = object : ClothesUpload_Request(
            Method.POST, "http://13.125.7.2/upload4.php",
            Response.Listener<NetworkResponse> { response ->
                try {

                    val obj = JSONObject(String(response!!.data))
                    var originImgName = obj.get("file_name") as String

                    Log.d("서버에 저장되어진 파일이름", originImgName)

                    val intent = Intent(this, ClothesSaveActivity::class.java)
                    intent.putExtra("originImgName", originImgName);
                    Log.d("originImgName", originImgName)
                    startActivity(intent)
                    finish()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                Log.e("GotError", "" + error.message)
            }) {
            override fun getByteData(): Map<String, DataPart>? {
                val params: MutableMap<String, DataPart> = HashMap()
                val imagename = System.currentTimeMillis()
                val uploadImgName = imagename.toString()
                Log.d("은정이는 민재이모", uploadImgName)
                params["image"] = DataPart(
                    "$uploadImgName.PNG",
                    getFileDataFromDrawable(bitmap)!!
                )
                return params
            }
        }

        //adding the request to volley
        Volley.newRequestQueue(this).add(clothesUploadRequest)

    }

}

