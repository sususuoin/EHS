package com.example.ehs.Fashionista

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.ehs.MainActivity
import com.example.ehs.Login.AutoLogin
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_fashionista.*
import kotlinx.android.synthetic.main.fragment_favorite.view.tv_favorite


class FashionistaFragment : Fragment() {
    private var a: Activity? = null

    var fuserIdArr2 = ArrayList<String>()
    var fuserLevelArr2 = ArrayList<String>()
    var fuserProImgArr2 = ArrayList<String>()

    val FashionistaList = mutableListOf<Fashionista>()

    companion object {
        const val TAG : String = "패셔니스타 프래그먼트"
        fun newInstance() : FashionistaFragment { // newInstance()라는 함수를 호출하면 HomeFragment를 반환함

            return FashionistaFragment()
        }
    }

    // 프레그먼트가 메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "FashionistaFragment - onCreate() called")

        fuserIdArr2 = AutoPro.getProuserId(a!!)
        Log.d(TAG+"내맘", fuserIdArr2.toString())
        fuserLevelArr2 = AutoPro.getProuserLevel(a!!)
        fuserProImgArr2 = AutoPro.getProuserProImg(a!!)

        var fuserProfile : Bitmap?
        for (i in 0 until fuserIdArr2.size) {

            fuserProfile = AutoLogin.StringToBitmap(fuserProImgArr2[i])
            var fashin = Fashionista(fuserIdArr2[i], fuserLevelArr2[i], fuserProfile)
            FashionistaList.add(fashin)
            fuserProfile == null
        }


    }
    // 프레그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d(TAG, "FashionistaFragment - onAttach() called")
    }
    // 뷰가 생성되었을 때 화면과 연결
    // 프레그먼트와 레이아웃을 연결시켜주는 부분이다.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "FashionistaFragment - onCreateView() called")
        val view = inflater.inflate(R.layout.fragment_fashionista, container, false)
        
        
        view.tv_favorite.setOnClickListener {
            Log.d("FashionistaFragment", "피드로 이동")
            (activity as MainActivity?)!!.replaceFragment(FavoriteFragment.newInstance())
        }



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = FashionistaListAdapter(FashionistaList)
        mRecyclerView.adapter = adapter

    }








}