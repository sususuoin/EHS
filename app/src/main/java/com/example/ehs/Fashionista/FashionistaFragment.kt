package com.example.ehs.Fashionista

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.ehs.MainActivity
import androidx.fragment.app.FragmentTransaction
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.R
import kotlinx.android.synthetic.main.fragment_closet.view.*
import kotlinx.android.synthetic.main.fragment_fashionista.*
import kotlinx.android.synthetic.main.fragment_fashionista.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.tv_favorite
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class FashionistaFragment : Fragment() {
    private var a: Activity? = null


//    val FashionistaList = mutableListOf<Fashionista>(
//        Fashionista("john", "#데일리"),
//        Fashionista("mir", "#빈티지"),
//        Fashionista("delp", "캐쥬얼")
//    )


    private var pDialog: ProgressDialog? = null

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

//        FashionistaUser()

//        var fashin = Fashionista("ghgh", "gkgk")
//        FashionistaList.add(fashin)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = FashionistaListAdapter(FashionistaList)
        mRecyclerView.adapter = adapter

    }






}