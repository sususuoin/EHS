package com.example.ehs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.AsyncTask
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
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.fragment_closet.view.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ClosetFragment : Fragment() {
    private var a: Activity? = null
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(a!!, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(a!!, R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(a!!, R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(a!!, R.anim.to_bottom_anim)}
    private var clicked = false

    val Fragment.packageManager get() = activity?.packageManager // 패키지 매니저 적용



    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    lateinit var currentPhotoPath: String // 문자열 형태의 사진 경로 값 (초기 값을 null로 시작하고 싶을 때)

    val serverUrl = "http://54.180.101.123/upload3.php"
    lateinit var bmp : Bitmap
    lateinit var uploadImgName : String

    var clothesActivity:ClothesActivity? = null
    companion object {
        const val TAG : String = "로그"
        fun newInstance() : ClosetFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함
            return ClosetFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ClosetFragment - onCreate() called")
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
        view.asdf.setOnClickListener { view ->
            val intent = Intent(a, ClothesSaveActivity::class.java)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            intent.putExtra("clothesImg", byteArray)
            startActivityForResult(intent, 101)
        }
        return view
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
                    val photoURI : Uri = FileProvider.getUriForFile(a!!, "com.example.closet.fileprovider", it)
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
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
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
                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, fileuri)
                        img_picture.setImageBitmap(bitmap)
                        bmp = bitmap

                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(a!!.contentResolver, fileuri)
                        bitmap = ImageDecoder.decodeBitmap(decode)
                        img_picture.setImageBitmap(bitmap)
                        bmp = bitmap


                    }

                    savePhoto(bitmap)
                    if(file.exists()) {
                        file.delete()
                    }
                    if (fileuri != null){
                        fileuri = null
                    }
                }
                REQUEST_OPEN_GALLERY -> { // requestcode가 REQUEST_OPEN_GALLERY이면
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, currentImageUrl)
                        img_picture.setImageBitmap(bitmap)
                        bmp = bitmap


                        uploadBitmap(bmp)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }




//        val intent = Intent(a, ClothesSaveActivity::class.java)
//        val stream = ByteArrayOutputStream()
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val byteArray = stream.toByteArray()
//        intent.putExtra("clothesImg", byteArray)
//        startActivityForResult(intent, 101)
//
//        val intent = Intent(context, ClothesSaveActivity::class.java)
//        intent.putExtra("uploadImgName", uploadImgName)
//        startActivityForResult(intent, 101)


    }

    /**
     * 갤러리에 저장
     */
    private fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Omonemo/" // 사진폴더로 저장하기 위한 경로 선언
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"
        val folder = File(folderPath)
        if(!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir() // make diretory 줄임말로 해당 경로에 폴더를 자동으로 새로 만든다
        }
        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.close()
        Toast.makeText(a!!, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

        var resizeBitmap = resize(bitmap)
        bmp = resizeBitmap!!

        uploadBitmap(bmp)
    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }

    private fun resize(bm: Bitmap): Bitmap? {
        var bm: Bitmap? = bm
        val config: Configuration = resources.configuration
        bm = if (config.smallestScreenWidthDp >= 800)
            Bitmap.createScaledBitmap(bm!!, 400, 240, true)
        else if (config.smallestScreenWidthDp >= 600)
            Bitmap.createScaledBitmap(bm!!, 300, 180, true)
        else if (config.smallestScreenWidthDp >= 400)
            Bitmap.createScaledBitmap(bm!!, 200, 120, true)
        else if (config.smallestScreenWidthDp >= 360)
            Bitmap.createScaledBitmap(bm!!, 180, 108, true)
        else
            Bitmap.createScaledBitmap(bm!!, 160, 96, true)
        return bm
    }


    fun getFileDataFromDrawable(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


    private fun uploadBitmap(bitmap: Bitmap) {
        val clothesSaveRequest: ClothesSave_Request =
            object : ClothesSave_Request(
                Method.POST, serverUrl,
                Response.Listener<NetworkResponse> { response ->
                    try {

                        val obj = JSONObject(String(response!!.data))
                        Toast.makeText(a, obj.toString(), Toast.LENGTH_SHORT).show()

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
                    uploadImgName = imagename.toString()
                    params["image"] = DataPart("$imagename.JPEG", getFileDataFromDrawable(bitmap)!!)
                    return params
                }
            }

        //adding the request to volley
        Volley.newRequestQueue(a).add(clothesSaveRequest)


    }


}