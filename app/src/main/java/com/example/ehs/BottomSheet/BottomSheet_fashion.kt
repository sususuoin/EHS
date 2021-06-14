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
import kotlinx.android.synthetic.main.bottomsheet_category.*
import kotlinx.android.synthetic.main.bottomsheet_fashion.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_fashion : BottomSheetDialogFragment() {
    var fashionchoice : String? = null
    var dailyclicked = false
    var casualclicked = false
    var streetclicked = false
    var outdoorclicked = false
    var vintageclicked = false
    var amekajiclicked = false
    lateinit var bottomSheetButtonClickListener : BottomSheet_fashion.BottomSheetButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottomsheet_fashion, container, false)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetButtonClickListener = context as BottomSheet_fashion.BottomSheetButtonClickListener
        }catch (e: ClassCastException) {
            Log.d("BottomSheet_fashion", "onAttach error")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_choicecom)?.setOnClickListener {
            bottomSheetButtonClickListener.onFashionButtonClicked(fashionchoice!!)
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_daily)?.setOnClickListener { Dailyclicked() }
        view?.findViewById<Button>(R.id.btn_casual)?.setOnClickListener { Casualclicked() }
        view?.findViewById<Button>(R.id.btn_street)?.setOnClickListener { Streetclicked() }
        view?.findViewById<Button>(R.id.btn_outdoor)?.setOnClickListener { Outdoorclicked() }
        view?.findViewById<Button>(R.id.btn_vintage)?.setOnClickListener { Vintageclicked() }
        view?.findViewById<Button>(R.id.btn_Amekaji)?.setOnClickListener { Amekajiclicked() }
    }

    interface BottomSheetButtonClickListener{
        fun onFashionButtonClicked(text: String)
    }

    fun Dailyclicked() {
        when (fashionchoice) {
            null -> {
                if (!dailyclicked) {
                    btn_daily.setBackgroundResource(R.drawable.button_choice_background)
                    btn_daily.setTextColor(Color.rgb(99,80,172))
                    dailyclicked = true
                    fashionchoice = "데일리"
                } else {
                    btn_daily.setBackgroundResource(R.drawable.button_background)
                    btn_daily.setTextColor(Color.BLACK)
                    dailyclicked = false
                    fashionchoice = null
                }
            }
            "상의" -> {
                btn_daily.setBackgroundResource(R.drawable.button_background)
                btn_daily.setTextColor(Color.BLACK)
                dailyclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Casualclicked() {
        when (fashionchoice) {
            null -> {
                if (!casualclicked) {
                    btn_casual.setBackgroundResource(R.drawable.button_choice_background)
                    btn_casual.setTextColor(Color.rgb(99,80,172))
                    casualclicked = true
                    fashionchoice = "캐주얼"
                } else {
                    btn_casual.setBackgroundResource(R.drawable.button_background)
                    btn_casual.setTextColor(Color.BLACK)
                    casualclicked = false
                    fashionchoice = null
                }
            }
            "캐주얼" -> {
                btn_casual.setBackgroundResource(R.drawable.button_background)
                btn_casual.setTextColor(Color.BLACK)
                casualclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun Streetclicked() {
        when (fashionchoice) {
            null -> {
                if (!streetclicked) {
                    btn_street.setBackgroundResource(R.drawable.button_choice_background)
                    btn_street.setTextColor(Color.rgb(99,80,172))
                    streetclicked = true
                    fashionchoice = "스트릿"
                } else {
                    btn_street.setBackgroundResource(R.drawable.button_background)
                    btn_street.setTextColor(Color.BLACK)
                    streetclicked = false
                    fashionchoice = null
                }
            }
            "스트릿" -> {
                btn_street.setBackgroundResource(R.drawable.button_background)
                btn_street.setTextColor(Color.BLACK)
                streetclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun Outdoorclicked() {
        when (fashionchoice) {
            null -> {
                if (!outdoorclicked) {
                    btn_outdoor.setBackgroundResource(R.drawable.button_choice_background)
                    btn_outdoor.setTextColor(Color.rgb(99,80,172))
                    outdoorclicked = true
                    fashionchoice = "아웃도어"
                } else {
                    btn_outdoor.setBackgroundResource(R.drawable.button_background)
                    btn_outdoor.setTextColor(Color.BLACK)
                    outdoorclicked = false
                    fashionchoice = null
                }
            }
            "아웃도어" -> {
                btn_outdoor.setBackgroundResource(R.drawable.button_background)
                btn_outdoor.setTextColor(Color.BLACK)
                outdoorclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun Vintageclicked() {
        when (fashionchoice) {
            null -> {
                if (!vintageclicked) {
                    btn_vintage.setBackgroundResource(R.drawable.button_choice_background)
                    btn_vintage.setTextColor(Color.rgb(99,80,172))
                    vintageclicked = true
                    fashionchoice = "빈티지"
                } else {
                    btn_vintage.setBackgroundResource(R.drawable.button_background)
                    btn_vintage.setTextColor(Color.BLACK)
                    vintageclicked = false
                    fashionchoice = null
                }
            }
            "빈티지" -> {
                btn_vintage.setBackgroundResource(R.drawable.button_background)
                btn_vintage.setTextColor(Color.BLACK)
                vintageclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun Amekajiclicked() {
        when (fashionchoice) {
            null -> {
                if (!amekajiclicked) {
                    btn_Amekaji.setBackgroundResource(R.drawable.button_choice_background)
                    btn_Amekaji.setTextColor(Color.rgb(99,80,172))
                    amekajiclicked = true
                    fashionchoice = "아메카지"
                } else {
                    btn_Amekaji.setBackgroundResource(R.drawable.button_background)
                    btn_Amekaji.setTextColor(Color.BLACK)
                    amekajiclicked = false
                    fashionchoice = null
                }
            }
            "아메카지" -> {
                btn_Amekaji.setBackgroundResource(R.drawable.button_background)
                btn_Amekaji.setTextColor(Color.BLACK)
                amekajiclicked = false
                fashionchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}