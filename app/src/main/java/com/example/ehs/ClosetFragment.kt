package com.example.ehs

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
import kotlinx.android.synthetic.main.fragment_closet.*
import kotlinx.android.synthetic.main.fragment_closet.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
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

    // 서버로 업로드할 파일관련 변수
    var uploadFilePath: String? = null
    var uploadFileName: String? = null

    // 파일을 업로드 하기 위한 변수 선언
    private var serverResponseCode = 0

    companion object {
        const val TAG : String = "옷장 프래그먼트"
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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
                    uploadFilePath = currentPhotoPath


                    if (Build.VERSION.SDK_INT < 28) { // 안드로이드 9.0 (Pie) 버전보다 낮을 경우
                        bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, Uri.fromFile(file))

                        activity?.let {
                            val intent = Intent(context, ClothesSaveActivity::class.java)
                            startActivity(intent)
                        }

//                        img_picture.setImageBitmap(bitmap)


                    } else { // 안드로이드 9.0 (Pie) 버전보다 높을 경우
                        val decode = ImageDecoder.createSource(
                                a!!.contentResolver,
                                Uri.fromFile(file)
                        )
                        bitmap = ImageDecoder.decodeBitmap(decode)

                        activity?.let {
                            val intent = Intent(context, ClothesSaveActivity::class.java)
                            startActivity(intent)
                        }

//                        img_picture.setImageBitmap(bitmap)
                    }
                    savePhoto(bitmap)
                }

                // requestcode가 REQUEST_OPEN_GALLERY이면
                //갤러리에서 가져오는 버튼누르면
                REQUEST_OPEN_GALLERY -> {
                    val currentImageUrl: Uri? = data?.data // data의 data형태로 들어옴
                    val path = getPath(currentImageUrl)
                    val name = getName(currentImageUrl)
                    uploadFilePath = path
                    uploadFileName = name
                    Log.i(TAG, "[onActivityResult] uploadFilePath:$uploadFilePath, uploadFileName:$uploadFileName")

                    try {
                        val bit = BitmapFactory.decodeFile(path)

                        val bitmap = MediaStore.Images.Media.getBitmap(a!!.contentResolver, currentImageUrl)
//                        img_picture.setImageBitmap(bitmap)


//                        //서버에 저장시키기
                        val uploadimagetoserver = UploadImageToServer()
                        uploadimagetoserver.execute("http://54.180.101.123/upload.php")


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }


        if(uploadFileName!=null) {
            val intent = Intent(context, ClothesSaveActivity::class.java)
            intent.putExtra("uploadFileName", uploadFileName)
            startActivityForResult(intent, 101)
        }
        else {
            Toast.makeText(activity, "사진을 서버에 보내는 데 실패했음", Toast.LENGTH_SHORT).show()
        }


    }


    /**
     * 갤러리에 저장
     */
    fun savePhoto(bitmap: Bitmap) {
        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/Pictures/Omonemo/" // 사진폴더로 저장하기 위한 경로 선언
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fileName = "${timestamp}.jpeg"



        uploadFilePath=folderPath
        uploadFileName=fileName

        val folder = File(folderPath)
        if(!folder.isDirectory) { // 현재 해당 경로에 폴더가 존재하지 않는다면
            folder.mkdir() // make diretory 줄임말로 해당 경로에 폴더를 자동으로 새로 만든다
        }
        // 실제적인 저장처리
        val out = FileOutputStream(folderPath + fileName)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.close()
        Toast.makeText(a!!, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()


        //카메라로 찍은 사진을 갤러리에 저장 시킨 후 서버에도 저장하기
        val uploadimagetoserver = UploadImageToServer()
        uploadimagetoserver.execute("http://54.180.101.123/upload.php")
    }

    /**
     * 갤러리 오픈 함수
     */
    fun openGallery() {
        val intent = Intent() //기기 기본 갤러리 접근
        intent.type = MediaStore.Images.Media.CONTENT_TYPE //구글 갤러리 접근
        intent.action = Intent.ACTION_GET_CONTENT

//        val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_OPEN_GALLERY)
    }

    /** 갤러리에 저장되어있던 사진에서 경로명이랑 이름찾기  */
    // 실제 경로 찾기
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





    /**
     * 사진 배경 지우는 서버로 보내는 함수
     */
    fun backRemove() {

    }

    // ============================== 사진을 서버에 전송하기 위한 스레드 ========================
    private inner class UploadImageToServer : AsyncTask<String?, String?, String?>() {
        var mProgressDialog: ProgressDialog? = null
        var fileName = uploadFilePath
        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        var lineEnd = "\r\n"
        var twoHyphens = "--"
        var boundary = "*****"
        var bytesRead = 0
        var bytesAvailable = 0
        var bufferSize = 0
        var buffer: ByteArray? = null
        var maxBufferSize = 1 * 10240 * 10240
        var sourceFile = File(uploadFilePath)



        protected override fun onPreExecute() {

            // Create a progressdialog
            mProgressDialog = ProgressDialog(a)
            mProgressDialog!!.setTitle("Loading...")
            mProgressDialog!!.setMessage("Image uploading...")
            mProgressDialog!!.setCanceledOnTouchOutside(false)
            mProgressDialog!!.setIndeterminate(false)
            mProgressDialog!!.show()
        }

        protected override fun doInBackground(vararg serverUrl: String?): String? {
            return if (!sourceFile.isFile) {
                a?.runOnUiThread(java.lang.Runnable {
                    Log.i(TAG, "[UploadImageToServer] Source File not exist :$uploadFilePath")
                })
                null
            } else {
                try {

                    // open a URL connection to the Servlet
                    val fileInputStream = FileInputStream(sourceFile)
                    val url = URL(serverUrl[0])


                    // Open a HTTP  connection to  the URL
                    conn = url.openConnection() as HttpURLConnection
                    conn!!.doInput = true // Allow Inputs
                    conn!!.doOutput = true // Allow Outputs
                    conn!!.useCaches = false // Don't use a Cached Copy
                    conn!!.requestMethod = "POST"
                    conn!!.setRequestProperty("Connection", "Keep-Alive")
                    conn!!.setRequestProperty("ENCTYPE", "multipart/form-data")
                    conn!!.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
                    conn!!.setRequestProperty("uploaded_file", fileName)
                    Log.i(TAG, "fileName: $fileName")
                    dos = DataOutputStream(conn!!.outputStream)


                    // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다.
                    dos!!.writeBytes(twoHyphens + boundary + lineEnd)
                    dos!!.writeBytes("Content-Disposition: form-data; name=\"clothes\"" + lineEnd);
                    dos!!.writeBytes(lineEnd)
                    dos!!.writeBytes("newImage")
                    dos!!.writeBytes(lineEnd)


                    // 이미지 전송
                    dos!!.writeBytes(twoHyphens + boundary + lineEnd)
                    dos!!.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"$fileName\"$lineEnd")
                    dos!!.writeBytes(lineEnd)


                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available()
                    bufferSize = Math.min(bytesAvailable, maxBufferSize)
                    buffer = ByteArray(bufferSize)


                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    while (bytesRead > 0) {
                        dos!!.write(buffer, 0, bufferSize)
                        bytesAvailable = fileInputStream.available()
                        bufferSize = Math.min(bytesAvailable, maxBufferSize)
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize)
                    }


                    // send multipart form data necesssary after file data...
                    dos!!.writeBytes(lineEnd)
                    dos!!.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)


                    // Responses from the server (code and message)
                    serverResponseCode = conn!!.responseCode
                    val serverResponseMessage = conn!!.responseMessage
                    Log.d("아 왠안되냐", serverResponseMessage)
                    Log.i(TAG, "[UploadImageToServer] HTTP Response is : $serverResponseMessage: $serverResponseCode")
                    if (serverResponseCode == 200) {
                        a?.runOnUiThread(java.lang.Runnable {
                            Toast.makeText(a, "File Upload Completed", Toast.LENGTH_SHORT).show()
                        })
                    }

                    //close the streams //
                    fileInputStream.close()
                    dos!!.flush()
                    dos!!.close()
                } catch (ex: MalformedURLException) {
                    ex.printStackTrace()
                    a?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(a, "MalformedURLException", Toast.LENGTH_SHORT).show()
                    })
                    Log.i(TAG, "[UploadImageToServer] error: " + ex.message, ex)
                } catch (e: Exception) {
                    e.printStackTrace()
                    a?.runOnUiThread(java.lang.Runnable {
                        Toast.makeText(a, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show()
                    })


                    Log.i(TAG, "[UploadImageToServer] Upload file to server Exception Exception : " + e.message, e)
                }
                Log.i(TAG, "[UploadImageToServer] Finish   " + uploadFileName)
                null
            } // End else block
        }

        protected override fun onPostExecute(s: String?) {
            mProgressDialog?.dismiss()
        }
    }


}