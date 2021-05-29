package com.example.ehs

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_category.*
import kotlinx.android.synthetic.main.bottomsheet_season.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_season : BottomSheetDialogFragment() {
    var seasonchoice : String? = null
    var springfallclicked = false
    var summerclicked = false
    var winterclicked = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottomsheet_season, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_choicecom)?.setOnClickListener {
            Seasonchoice()
            Log.d("카테고리", "선택사항은  -> " + seasonchoice)
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_springfall)?.setOnClickListener { SpringFallclicked()}
        view?.findViewById<Button>(R.id.btn_summer)?.setOnClickListener { Summerclicked() }
        view?.findViewById<Button>(R.id.btn_winter)?.setOnClickListener { Winterclicked() }
    }

    /**
     * 함수호출 : 각 카테고리 버튼 클릭 시 색깔 바꿈 & 값 받아오기
     */
    fun SpringFallclicked() {
        if (!springfallclicked) {
            btn_springfall.setBackgroundResource(R.drawable.button_choice_background)
            btn_springfall.setTextColor(Color.rgb(99,80,172))
            springfallclicked = true
        } else {
            btn_springfall.setBackgroundResource(R.drawable.button_background)
            btn_springfall.setTextColor(Color.BLACK)
            springfallclicked = false
        }
    }

    fun Summerclicked() {
        if (!summerclicked) {
            btn_summer.setBackgroundResource(R.drawable.button_choice_background)
            btn_summer.setTextColor(Color.rgb(99,80,172))
            summerclicked = true
        } else {
            btn_summer.setBackgroundResource(R.drawable.button_background)
            btn_summer.setTextColor(Color.BLACK)
            summerclicked = false
        }
    }

    fun Winterclicked() {
        if (!winterclicked) {
            btn_winter.setBackgroundResource(R.drawable.button_choice_background)
            btn_winter.setTextColor(Color.rgb(99,80,172)) // 우리색
            winterclicked = true
        } else {
            btn_winter.setBackgroundResource(R.drawable.button_background)
            btn_winter.setTextColor(Color.BLACK)
            winterclicked = false
        }
    }

    fun Seasonchoice() {
        if(springfallclicked && summerclicked && winterclicked) {
            seasonchoice = "봄·가을·여름·겨울"
        }
        if(springfallclicked && summerclicked && !winterclicked){
            seasonchoice = "봄·가을·여름"
        }
        if(springfallclicked && !summerclicked && winterclicked){
            seasonchoice = "봄·가을·겨울"
        }
        if(springfallclicked && !summerclicked && !winterclicked){
            seasonchoice = "봄·가을"
        }
        if(!springfallclicked && summerclicked && winterclicked){
            seasonchoice = "여름·겨울"
        }
        if(!springfallclicked && summerclicked && !winterclicked){
            seasonchoice = "여름"
        }
        if(!springfallclicked && !summerclicked && winterclicked){
            seasonchoice = "겨울"
        }


    }




}