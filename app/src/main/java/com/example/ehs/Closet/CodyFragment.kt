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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.Volley
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

    val codyList = mutableListOf<Cody>()
    var codyArr = ArrayList<String>()

    val adapter = CodyListAdapter(codyList)

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
        Log.d("ㅁㅁㅁㅁㅁ새로고침222", codyArr.toString())

        var a_bitmap : Bitmap? = null
        for (i in 0 until codyArr.size) {
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

                var cody = Cody(a_bitmap)
                codyList.add(cody)


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
            val intent = Intent(a, CodyMakeActivity::class.java)
            startActivity(intent)
        }
        view.tv_addcody.setOnClickListener { view ->
            Log.d("클릭!!", "코디추가 텍스트 클릭!!")
            onAddButtonClicked()
            val intent = Intent(context, CodyMakeActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridLayoutManager = GridLayoutManager(a, 2)
        recycler_cody.layoutManager = gridLayoutManager

        recycler_cody.adapter = adapter
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
                        bitmap = MediaStore.Images.Media.getBitmap(ClosetFragment.a!!.contentResolver, fileuri)

                        bmp = bitmap

                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(ClosetFragment.a!!.contentResolver, fileuri)
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
                            ClosetFragment.a!!.contentResolver,
                            currentImageUrl
                        )

                        bmp = bitmap


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }


        GlobalScope.launch(Dispatchers.Main) {
            launch(Dispatchers.Main) {
                uploadBitmap(bmp)
            }

            delay(3000L)

            val intent = Intent(ClosetFragment.a, ClothesSaveActivity::class.java)
            intent.putExtra("originImgName", originImgName);
            Log.d(ClosetFragment.TAG, originImgName)
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
        Toast.makeText(ClosetFragment.a!!, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()

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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
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

                    Toast.makeText(ClosetFragment.a, originImgName, Toast.LENGTH_SHORT).show()


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(ClosetFragment.a, error.message, Toast.LENGTH_LONG).show()
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
        Volley.newRequestQueue(ClosetFragment.a).add(clothesUploadRequest)

    }







}