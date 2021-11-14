package com.example.ehs.Closet

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.ehs.BottomSheet.BottomSheet_category
import com.example.ehs.BottomSheet.BottomSheet_color
import com.example.ehs.BottomSheet.BottomSheet_season
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.example.ehs.ml.*
import kotlinx.android.synthetic.main.activity_clothes_save.*
import kotlinx.android.synthetic.main.activity_main_ai.*
import kotlinx.android.synthetic.main.bottomsheet_category.*
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class ClothesSaveActivity : AppCompatActivity(), BottomSheet_category.BottomSheetButtonClickListener, BottomSheet_color.BottomSheetButtonClickListener, BottomSheet_season.BottomSheetButtonClickListener {


    companion object {
        val TAG: String = "옷저장하는 화면"

        var clothesSaveContext: Context? = null
        var clothesSaveActivity_Dialog : ProgressDialog? = null
    }


    lateinit var clothesName : String
    lateinit var clothesImg : Bitmap
    lateinit var clothesCategory : String
    lateinit var clothesCategory_Detail : String

    var tvcategory : String = ""
    var tvcolor : String = ""
    var tvseason : String = ""

    val serverUrl = "http://13.125.7.2/upload3.php"
    var originURL :String = "http://13.125.7.2/img/clothes/origin/"
    lateinit var realURL : String

    lateinit var mProgressDialog: ProgressDialog
    lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothes_save)
        clothesSaveContext=this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = true
            this.window.statusBarColor = ContextCompat.getColor(this,R.color.white)
        }

        //사용자 아이디받아오기
        userId = AutoLogin.getUserId(this@ClothesSaveActivity)

        val intent = intent
        val originImgName = intent.getStringExtra("originImgName")
        Log.d(TAG, originImgName!!)
        realURL = originURL+originImgName


        var task = back()
        task.execute(realURL);


        /**
         * 액션바 대신 툴바를 사용하도록 설정
         */
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        //뒤로 가기 버튼 생성
        ab.setDisplayHomeAsUpEnabled(true) // 툴바 설정 완료
        
        // 처음에 컬러버튼 안보이게
        btn_colorview.visibility = View.INVISIBLE


        tv_category.setOnClickListener {
            val bottomSheet: BottomSheet_category = BottomSheet_category()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        ll_color.setOnClickListener {
            val bottomSheet = BottomSheet_color()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        tv_season.setOnClickListener {
            val bottomSheet = BottomSheet_season()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        // 에디트 버튼 클릭
        btn_edit_clothes.setOnClickListener {

        }


        //완료하기 버튼클릭
        btn_complete.setOnClickListener {
            Log.d(TAG, "서버에 저장을 시작합니다")

            if(tvcolor == "" || tvcolor == null) {
                Toast.makeText(this, "색상을 선택해주세요", Toast.LENGTH_LONG).show()
            }else if(tvcategory == "" || tvcategory == null) {
                Toast.makeText(this, "카테고리를 선택해주세요", Toast.LENGTH_LONG).show()
            }else if(tvseason == "" || tvseason == null ) {
                Toast.makeText(this, "계절을 선택해주세요", Toast.LENGTH_LONG).show()
            } else {

                clothesSaveActivity_Dialog = ProgressDialog(this)
                clothesSaveActivity_Dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                clothesSaveActivity_Dialog!!.setMessage("업로드 중입니다.")
                clothesSaveActivity_Dialog!!.setCanceledOnTouchOutside(false)
                clothesSaveActivity_Dialog!!.show()
                (BasicClothesActivity.basicClothesContext as BasicClothesActivity).finish()

                clothesCategory = tv_category.text as String
                category_detial(clothesImg)

            }

        }

//onCreate() 끝

    }

    /**
     * 툴바 뒤로가기 버튼 기능 구현
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


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun category_detial(clothesImg : Bitmap) {
        when(clothesCategory) {
            "상의" -> {
                Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelTop.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var topdoubleArr = ArrayList<Double>()
                var topLabelArr = ArrayList<String>()
                topLabelArr.add("블라우스")
                topLabelArr.add("후드")
                topLabelArr.add("니트")
                topLabelArr.add("티셔츠")
                topLabelArr.add("셔츠")

                for(i in 0 until outputFeature.floatArray.size) {
                    topdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = topdoubleArr.indexOf(topdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", topLabelArr[max])
                clothesCategory_Detail = topLabelArr[max]
                Log.d("세부카테고리", topdoubleArr.max().toString())

//                uploadBitmap(clothesImg)

                model.close()
            }

            "하의" -> {
               Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelBottom.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var bottomdoubleArr = ArrayList<Double>()
                var bottomLabelArr = ArrayList<String>()
                bottomLabelArr.add("면바지")
                bottomLabelArr.add("청바지")
                bottomLabelArr.add("슬랙스")
                bottomLabelArr.add("츄리닝")
                bottomLabelArr.add("반바지")
                bottomLabelArr.add("미니스커트")
                bottomLabelArr.add("롱스커트")

                for(i in 0 until outputFeature.floatArray.size) {
                    bottomdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = bottomdoubleArr.indexOf(bottomdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", bottomLabelArr[max])
                clothesCategory_Detail = bottomLabelArr[max]
                Log.d("세부카테고리", bottomdoubleArr.max().toString())

//                uploadBitmap(clothesImg)

                model.close()
            }

            "아우터" -> {
                Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelOuter.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var outerdoubleArr = ArrayList<Double>()
                var outerLabelArr = ArrayList<String>()
                outerLabelArr.add("가디건")
                outerLabelArr.add("코트")
                outerLabelArr.add("점퍼")
                outerLabelArr.add("패딩")
                outerLabelArr.add("수트자켓")

                for(i in 0 until outputFeature.floatArray.size) {
                    outerdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = outerdoubleArr.indexOf(outerdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", outerLabelArr[max])
                clothesCategory_Detail = outerLabelArr[max]
                Log.d("세부카테고리", outerdoubleArr.max().toString())

//                uploadBitmap(clothesImg)

                model.close()
            }

            "원피스" -> {
                Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelOnepiece.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var outerdoubleArr = ArrayList<Double>()
                var outerLabelArr = ArrayList<String>()
                outerLabelArr.add("무지원피스")
                outerLabelArr.add("패턴원피스")
                outerLabelArr.add("후드원피스")

                for(i in 0 until outputFeature.floatArray.size) {
                    outerdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = outerdoubleArr.indexOf(outerdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", outerLabelArr[max])
                clothesCategory_Detail = outerLabelArr[max]
                Log.d("세부카테고리", outerdoubleArr.max().toString())

//                uploadBitmap(clothesImg)

                model.close()
            }

            "신발" -> {
                Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelShoes.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var shoesdoubleArr = ArrayList<Double>()
                var shoesLabelArr = ArrayList<String>()
                shoesLabelArr.add("스니커즈")
                shoesLabelArr.add("부츠")
                shoesLabelArr.add("구두")
                shoesLabelArr.add("슬리퍼")

                for(i in 0 until outputFeature.floatArray.size) {
                    shoesdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = shoesdoubleArr.indexOf(shoesdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", shoesLabelArr[max])
                clothesCategory_Detail= shoesLabelArr[max]
                Log.d("세부카테고리", shoesdoubleArr.max().toString())

//                uploadBitmap(clothesImg)

                model.close()
            }

            "가방" -> {
                Log.d("세부카테고리222", clothesCategory)
                val resized : Bitmap = Bitmap.createScaledBitmap(clothesImg, 224, 224, true)
                val model = ModelBag.newInstance(this@ClothesSaveActivity)

                //input
                val inputFeature = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
                val tbuffer = TensorImage.fromBitmap(resized)
                val byteBuffer = tbuffer.buffer
                inputFeature.loadBuffer(byteBuffer)

                //output
                val outputs = model.process(inputFeature)
                val outputFeature = outputs.outputFeature0AsTensorBuffer

                var bagdoubleArr = ArrayList<Double>()
                var bagLabelArr = ArrayList<String>()
                bagLabelArr.add("스니커즈")
                bagLabelArr.add("부츠")
                bagLabelArr.add("구두")
                bagLabelArr.add("슬리퍼")

                for(i in 0 until outputFeature.floatArray.size) {
                    bagdoubleArr.add(outputFeature.floatArray[i].div(255.0)*100)
                }
                var max = bagdoubleArr.indexOf(bagdoubleArr.max())
                Log.d("세부카테고리", max.toString())
                Log.d("세부카테고리", bagLabelArr[max])
                clothesCategory_Detail = bagLabelArr[max]
                Log.d("세부카테고리", bagdoubleArr.max().toString())

                model.close()
            }
        }
        clothesSaveActivity_Dialog?.dismiss()
        uploadBitmap(clothesImg)

    }


    fun uploadBitmap(bitmap: Bitmap) {
        val clothesUploadRequest: ClothesUpload_Request =
            object : ClothesUpload_Request(
                Method.POST, serverUrl,
                Response.Listener<NetworkResponse> { response ->
                    try {

                        val obj = JSONObject(String(response!!.data))
//                        Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show()
                        clothesName = obj.getString("file_name")

                        Log.d("은정이와 수인이는 호롤로로 ", clothesName)

                        uploadDB(userId)

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
//                    clothesName = "$imagename.PNG"

                    params["image"] = DataPart("$imagename.PNG", getFileDataFromDrawable(bitmap)!!)
                    return params
                }
            }

        //adding the request to volley
        Volley.newRequestQueue(this).add(clothesUploadRequest)

    }


    fun uploadDB(userId: String) {
        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
//                        Toast.makeText(this@ClothesSaveActivity, jsonObject.toString(), Toast.LENGTH_LONG).show()
                        Log.d(TAG, "서버에 저장을 완료했다다")
                        //다른 액티비티함수 사용할때
                        (MainActivity.mContext as MainActivity).ClosetImg()


                    } else {
                        Toast.makeText(this@ClothesSaveActivity, "실패 두둥탁", Toast.LENGTH_LONG).show()

                        return
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }


        var clothesColor : String = tv_color.text as String

        var clothesSeason : String = tv_season.text as String

        Log.e("옷컬러검색", clothesColor)
        val clothesPath = "http://13.125.7.2/img/clothes/"
        val clothesSave_Request = ClothesSave_Request(userId,
            clothesPath,
            clothesName,
            clothesColor,
            clothesCategory,
            clothesCategory_Detail,
            clothesSeason,
            responseListener)
        val queue = Volley.newRequestQueue(this@ClothesSaveActivity)
        queue.add(clothesSave_Request)
    }


     open inner class back : AsyncTask<String?, Int?, Bitmap>() {

         lateinit var fileuri : Uri
         override fun onPreExecute() {

             // Create a progressdialog
             mProgressDialog = ProgressDialog(this@ClothesSaveActivity)
             mProgressDialog.setTitle("Loading...")
             mProgressDialog.setMessage("Image uploading...")
             mProgressDialog.setCanceledOnTouchOutside(false)
             mProgressDialog.setIndeterminate(false)
             mProgressDialog.show()
         }


        override fun doInBackground(vararg urls: String?): Bitmap {
            try {
                val myFileUrl = URL(urls[0])

                val file = File(urls[0].toString())
                fileuri = Uri.fromFile(file)

                val conn: HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.connect()
                val iss: InputStream = conn.inputStream
                clothesImg = BitmapFactory.decodeStream(iss)

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return clothesImg

        }

        override fun onPostExecute(img: Bitmap) {
            mProgressDialog.dismiss()
            Glide.with(this@ClothesSaveActivity).load(img).into(iv_clothes)
//            iv_clothes.setImageBitmap(img)
        }


    }


    // 바텀시트에서 선택한거 적용
    override fun onCategoryButtonClicked(text: String) {
        tv_category.text = text
        tvcategory = text
    }
    override fun onColorButtonClicked(text: String) {
        tv_color.text = text
        tvcolor = text
        btn_colorview.visibility = View.VISIBLE
        if(text == "흰색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.white))
        }
        if(text == "크림"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.cream))
        }
        if(text == "연회색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightgray))
        }
        if(text == "진회색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.darkgray))
        }
        if(text == "검정"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.black))
        }
        if(text == "주황"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.orange))
        }
        if(text == "베이지"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.beige))
        }
        if(text == "노랑"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.yellow))
        }
        if(text == "연두"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightgreen))
        }
        if(text == "하늘"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.skyblue))
        }
        if(text == "분홍"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.pink))
        }
        if(text == "연분홍"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.lightpink))
        }
        if(text == "초록"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.green))
        }
        if(text == "카키"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.kaki))
        }
        if(text == "파랑"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.blue))
        }
        if(text == "빨강"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.red))
        }
        if(text == "와인"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.wine))
        }
        if(text == "갈색"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.brown))
        }
        if(text == "보라"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.purple))
        }
        if(text == "네이비"){
            btn_colorview.setBackgroundColor(this.resources.getColor(R.color.navy))
        }
    }
    override fun onSeasonButtonClicked(text: String) {
        tv_season.text = text
        tvseason = text
    }


}
