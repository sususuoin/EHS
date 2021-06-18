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
import kotlinx.android.synthetic.main.bottomsheet_gender.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_gender : BottomSheetDialogFragment() {
    var genderchoice : String? = null
    var maleclicked = false
    var femaleclicked = false
    lateinit var bottomSheetButtonClickListener : BottomSheetButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottomsheet_gender, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetButtonClickListener = context as BottomSheetButtonClickListener
        }catch (e: ClassCastException) {
            Log.d("BottomSheet_gender", "onAttach error")
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_choicecom)?.setOnClickListener {
            if(genderchoice==null){
                Toast.makeText(context, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }else {
                Log.d("성별", "선택사항은  -> " + genderchoice)
                bottomSheetButtonClickListener.onGenderButtonClicked(genderchoice!!)
                dismiss()
            }
        }
        view?.findViewById<Button>(R.id.btn_male)?.setOnClickListener { Maleclicked()}
        view?.findViewById<Button>(R.id.btn_female)?.setOnClickListener { Femaleclicked() }
    }

    interface BottomSheetButtonClickListener{
        fun onGenderButtonClicked(text: String)
    }


    /**
     * 함수호출 : 각 카테고리 버튼 클릭 시 색깔 바꿈 & 값 받아오기
     */
    fun Maleclicked() {
        if (!maleclicked) { // 버튼이 눌러져있지 않다면
            btn_male.setBackgroundResource(R.drawable.button_choice_background)
            btn_male.setTextColor(Color.rgb(99,80,172))
            maleclicked = true
            genderchoice = "남성"

            btn_female.setBackgroundResource(R.drawable.button_background)
            btn_female.setTextColor(Color.BLACK)
            femaleclicked = false
        } else { // 남성버튼이 눌러져 있는 경우
            btn_male.setBackgroundResource(R.drawable.button_background)
            btn_male.setTextColor(Color.BLACK)
            maleclicked = false
        }
    }

    fun Femaleclicked() {
        if (!femaleclicked) {
            btn_female.setBackgroundResource(R.drawable.button_choice_background)
            btn_female.setTextColor(Color.rgb(99,80,172))
            femaleclicked = true
            genderchoice = "여성"

            btn_male.setBackgroundResource(R.drawable.button_background)
            btn_male.setTextColor(Color.BLACK)
            maleclicked = false

        } else { // 여성 버튼이 눌러져 있는 경우
            btn_female.setBackgroundResource(R.drawable.button_background)
            btn_female.setTextColor(Color.BLACK)
            femaleclicked = false

        }
    }






}