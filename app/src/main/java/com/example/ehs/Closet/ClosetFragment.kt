package com.example.ehs.Closet

import android.app.Activity
import android.app.ProgressDialog
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
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Closet.ClothesSaveActivity.Companion.clothesSaveActivity_Dialog
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.fragment_closet.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ClosetFragment : Fragment() {

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



    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    lateinit var currentPhotoPath: String // 문자열 형태의 사진 경로 값 (초기 값을 null로 시작하고 싶을 때)


    lateinit var bmp : Bitmap
    lateinit var uploadImgName : String
    lateinit var originImgName : String

    val clothesList = mutableListOf<Clothes>()

    lateinit var userId : String

    val adapter = ClothesListAdapter(clothesList)

    companion object {
        var a: Activity? = null
        const val TAG : String = "클로젯 프레그먼트"
        var clothesArr = ArrayList<String>()

        fun newInstance() : ClosetFragment { // newInstance()라는 함수를 호출하면 ClosetFragment를 반환함
            return ClosetFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ClosetFragment - onCreate() called")
        AndroidThreeTen.init(a)

        userId = AutoLogin.getUserId(a!!)

        //clothes테이블에서 나의 데이터가져오기
        //현재는 날씨
        clothesResponse()

    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "새로고침 실행")
        clothesList.clear()

        clothesSaveActivity_Dialog?.dismiss()
        clothesArr = AutoCloset.getClothesName(a!!)
        Log.d("ㅁㅁㅁㅁㅁ새로고침222", clothesArr.toString())

        var a_bitmap : Bitmap? = null
        for (i in 0 until clothesArr.size) {
            val uThread: Thread = object : Thread() {
                override fun run() {
                    try {

                        Log.d("Closet프래그먼터리스트123", clothesArr[i])

                        //서버에 올려둔 이미지 URL
                        val url = URL("http://13.125.7.2/img/clothes/" + clothesArr[i])

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
                clothesList.add(clothes)


            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        adapter.notifyDataSetChanged()
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
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "ClosetFragment - onCreateView() called")
        val view: View = inflater!!.inflate(R.layout.fragment_closet, container, false)

        view.tv_mycody.setOnClickListener { view ->
            Log.d("ClosetFragment", "내 코디로 이동")
            (activity as MainActivity?)!!.replaceFragment(CodyFragment.newInstance())
        }

        view.btn_add.setOnClickListener { view ->
            Log.d("클릭!!", "플러스 버튼 클릭!!")
            onAddButtonClicked()
        }
        view.btn_gallery.setOnClickListener { view ->
            Log.d("클릭!!", "갤러리 버튼 클릭!!")
            openGallery()
            onAddButtonClicked()
        }
        view.tv_gallery.setOnClickListener { view ->
            Log.d("클릭!!", "갤러리 텍스트 클릭!!")
            openGallery()
            onAddButtonClicked()
        }
        view.btn_camera.setOnClickListener { view ->
            Log.d("클릭!!", "카메라 버튼 클릭!!")
            takeCapture() // 기본 카메라 앱을 실행하여 사진 촬영
            onAddButtonClicked()
        }
        view.tv_camera.setOnClickListener { view ->
            Log.d("클릭!!", "카메라 텍스트 클릭!!")
            takeCapture() // 기본 카메라 앱을 실행하여 사진 촬영
            onAddButtonClicked()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(a, 3)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        //recylerview 이거 fashionista.xml에 있는 변수
    }

    fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.visibility = View.VISIBLE
            btn_camera.visibility = View.VISIBLE
            tv_camera.visibility = View.VISIBLE
            tv_gallery.visibility = View.VISIBLE
        }else {
            btn_gallery.visibility = View.INVISIBLE
            btn_camera.visibility = View.INVISIBLE
            tv_camera.visibility = View.INVISIBLE
            tv_gallery.visibility = View.INVISIBLE
            btn_add.backgroundTintList = AppCompatResources.getColorStateList(a!!, R.color.white)
        }
    }
    fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.startAnimation(fromBottom)
            btn_camera.startAnimation(fromBottom)
            tv_gallery.startAnimation(fromBottom)
            tv_camera.startAnimation(fromBottom)
            btn_add.startAnimation(rotateOpen)
        } else {
            btn_gallery.startAnimation(toBottom)
            btn_camera.startAnimation(toBottom)
            tv_gallery.startAnimation(toBottom)
            tv_camera.startAnimation(toBottom)
            btn_add.startAnimation(rotateClose)
        }
    }

    fun setClickable(clicked: Boolean) {
        if(!clicked) {
            btn_gallery.isClickable = true
            btn_camera.isClickable = true
            tv_camera.isClickable = true
            tv_gallery.isClickable = true
        } else {
            btn_gallery.isClickable = false
            btn_camera.isClickable = false
            tv_camera.isClickable = false
            tv_gallery.isClickable = false
        }
    }




    fun takeCapture() {
        // 기본 카메라 앱 실행
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager!!)?.also {
                val photoFile: File? = try{
                    createImageFile()
                } catch (ex: IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        a!!,
                        "com.example.closet.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }




    /**
     * 이미지 파일 생성
     */
    private fun createImageFile(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getActivity()?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PNG_${timestamp}_", ".png", storageDir)
            .apply { currentPhotoPath = absolutePath }
    }



    // startAcitivityForResult를 통해서 기본 카메라 앱으로부터 받아온 사진 결과 값
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            when(requestCode) { //resultCode가 Ok이고
                REQUEST_IMAGE_CAPTURE -> { // requestcode가 REQUEST_IMAGE_CAPTURE이면
                    val bitmap: Bitmap
                    val file = File(currentPhotoPath)
                    var fileuri = Uri.fromFile(file)
                    uploadImgName = getName(fileuri)
                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, fileuri)

                        bmp = bitmap

                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(a!!.contentResolver, fileuri)
                        bitmap = ImageDecoder.decodeBitmap(decode)

                        bmp = bitmap


                    }

                    savePhoto(bitmap)
                    if (file.exists()) {
                        file.delete()
                    }
                    if (fileuri != null) {
                        fileuri = null
                    }
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
                    uploadImgName = getName(currentImageUrl)

                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            a!!.contentResolver,
                            currentImageUrl
                        )

                        bmp = bitmap


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        uploadBitmap(bmp)

    }

    /**
     * 갤러리에 저장
     */
    private fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Omonemo/" // 사진폴더로 저장하기 위한 경로 선언
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.png"
        val folder = File(folderPath)
        if(!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir() // make diretory 줄임말로 해당 경로에 폴더를 자동으로 새로 만든다
        }
        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()
        Toast.makeText(a!!, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
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


    fun uploadBitmap(bitmap: Bitmap) {
        val clothesUploadRequest: ClothesUpload_Request = object : ClothesUpload_Request(
            Method.POST, "http://13.125.7.2/upload4.php",
            Response.Listener<NetworkResponse> { response ->
                try {

                    val obj = JSONObject(String(response!!.data))
                    originImgName = obj.get("file_name") as String

                    Log.d("서버에 저장되어진 파일이름", originImgName)

                    Toast.makeText(a, originImgName, Toast.LENGTH_SHORT).show()

                    val intent = Intent(a, ClothesSaveActivity::class.java)
                    intent.putExtra("originImgName", originImgName);
                    Log.d(TAG, originImgName)
                    startActivity(intent)


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(a, error.message, Toast.LENGTH_LONG).show()
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
        Volley.newRequestQueue(a).add(clothesUploadRequest)

    }


    fun clothesResponse() {

        var cuserId : String
        var cColor : String
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