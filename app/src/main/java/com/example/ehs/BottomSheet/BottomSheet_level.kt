package com.example.ehs.BottomSheet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.ehs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_level.*
import kotlinx.android.synthetic.main.bottomsheet_level.view.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_level : BottomSheetDialogFragment() {
    var levelchoice : String? = null
    var normalclicked = false
    var specialclicked = false
    lateinit var bottomSheetButtonClickListener : BottomSheetButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_level, container, false)


        view.btn_choicecom.setOnClickListener {
            if(levelchoice==null){
                Toast.makeText(context, "레벨을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                Log.d("레벨", "선택사항은  -> " + levelchoice)
                bottomSheetButtonClickListener.onLevelButtonClicked(levelchoice!!)
                dismiss()
            }
        }
        view.btn_normal.setOnClickListener {
            normalclicked()
        }
        view.btn_special.setOnClickListener {
            specialclicked()
        }

        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetButtonClickListener = context as BottomSheetButtonClickListener
        }catch (e: ClassCastException) {
            Log.d("BottomSheet_gender", "onAttach error")
        }
    }

    interface BottomSheetButtonClickListener{
        fun onLevelButtonClicked(text: String)
    }


    /**
     * 함수호출 : 각 카테고리 버튼 클릭 시 색깔 바꿈 & 값 받아오기
     */
    fun normalclicked() {
        if (!normalclicked) { // 버튼이 눌러져있지 않다면
            btn_normal.setBackgroundResource(R.drawable.button_choice_background)
            btn_normal.setTextColor(Color.rgb(99,80,172))
            normalclicked = true
            levelchoice = "일반인"

            btn_special.setBackgroundResource(R.drawable.button_background)
            btn_special.setTextColor(Color.BLACK)
            specialclicked = false
        } else { // 남성버튼이 눌러져 있는 경우
            btn_normal.setBackgroundResource(R.drawable.button_background)
            btn_normal.setTextColor(Color.BLACK)
            normalclicked = false
        }
    }

    fun specialclicked() {
        if (!specialclicked) {
            btn_special.setBackgroundResource(R.drawable.button_choice_background)
            btn_special.setTextColor(Color.rgb(99,80,172))
            specialclicked = true
            levelchoice = "전문가"

            btn_normal.setBackgroundResource(R.drawable.button_background)
            btn_normal.setTextColor(Color.BLACK)
            normalclicked = false

        } else { // 여성 버튼이 눌러져 있는 경우
            btn_special.setBackgroundResource(R.drawable.button_background)
            btn_special.setTextColor(Color.BLACK)
            specialclicked = false

        }
    }






}