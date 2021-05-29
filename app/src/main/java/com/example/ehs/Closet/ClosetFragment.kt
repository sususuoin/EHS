package com.example.ehs.Closet

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.fragment_closet.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ClosetFragment : Fragment() {
    private var a: Activity? = null
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

    val Fragment.packageManager get() = activity?.packageManager // 패키지 매니저 적용



    val REQUEST_IMAGE_CAPTURE = 1 // 카메라 사진 촬영 요청코드, 한번 지정되면 값이 바뀌지 않음
    val REQUEST_OPEN_GALLERY = 2

    lateinit var currentPhotoPath: String // 문자열 형태의 사진 경로 값 (초기 값을 null로 시작하고 싶을 때)


    lateinit var bmp : Bitmap
    lateinit var uploadImgName : String
    lateinit var originImgName : String

    lateinit var mProgressDialog: ProgressDialog
    lateinit var clothesImg : Bitmap


    val clothesList = mutableListOf(
        Clothes(null)
    )


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

        AndroidThreeTen.init(a)

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

        view.mPlusButton.setOnClickListener { view ->

            var task = back()
            task.execute("http://54.180.101.123/clothes/"+originImgName)




        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = ClothesListAdapter(clothesList)
        recyclerView.adapter = adapter
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

//
//        GlobalScope.launch(Dispatchers.Main) {
//            uploadBitmap(bmp)
//
//
//            delay(2000L)
//
//        }
//        val intent = Intent(a, ClothesSaveActivity::class.java)
//        intent.putExtra("originImgName", originImgName);
//        Log.d(TAG, originImgName)
//        startActivity(intent)


        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.Main) {
                uploadBitmap(bmp)
            }

            delay(5000L)

            val intent = Intent(a, ClothesSaveActivity::class.java)
            intent.putExtra("originImgName", originImgName);
            Log.d(TAG, originImgName)
            startActivity(intent)


        }

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
        val cursor = activity!!.managedQuery(uri, projection, null, null, null)
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    // 파일명 찾기
    private fun getName(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.ImageColumns.DISPLAY_NAME)
        val cursor = activity!!.managedQuery(uri, projection, null, null, null)
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


    fun uploadBitmap(bitmap: Bitmap) {
        val clothesUploadRequest: ClothesUpload_Request =
            object : ClothesUpload_Request(
                Method.POST, "http://54.180.101.123/upload4.php",
                Response.Listener<NetworkResponse> { response ->
                    try {

                        val obj = JSONObject(String(response!!.data))
                        originImgName = obj.get("file_name") as String

                        Log.d("서버에 저장되어진 파일이름", originImgName)

                        Toast.makeText(a, originImgName, Toast.LENGTH_SHORT).show()


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
                        "$uploadImgName.JPEG",
                        getFileDataFromDrawable(bitmap)!!
                    )
                    return params
                }
            }

        //adding the request to volley
        Volley.newRequestQueue(a).add(clothesUploadRequest)

    }


    open inner class back : AsyncTask<String?, Int?, Bitmap>() {


        protected override fun onPreExecute() {

            // Create a progressdialog
            mProgressDialog = ProgressDialog(a)
            mProgressDialog.setTitle("Loading...")
            mProgressDialog.setMessage("Image uploading...")
            mProgressDialog.setCanceledOnTouchOutside(false)
            mProgressDialog.setIndeterminate(false)
            mProgressDialog.show()
        }


        override fun doInBackground(vararg urls: String?): Bitmap {
            try {
                val myFileUrl = URL(urls[0])
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

            var clothes = Clothes(clothesImg)
            clothesList.add(clothes)

        }

    }

}