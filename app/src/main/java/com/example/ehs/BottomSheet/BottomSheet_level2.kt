package com.example.ehs.BottomSheet

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ehs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_level2.view.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_level2 : BottomSheetDialogFragment() {
    var levelchoice : String? = null
    var btn_1 = false
    var btn_2 = false
    var btn_3 = false
    var btn_4 = false
    var btn_5 = false
    lateinit var bottomSheetButtonClickListener2 : BottomSheetButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_level2, container, false)

        view.btn_choicecom.setOnClickListener {
            bottomSheetButtonClickListener2.onLevel2ButtonClicked(levelchoice!!)
            dismiss()
        }
        view.btn_1.setOnClickListener {
            btn_1clicked()
        }
        view.btn_2.setOnClickListener {
            btn_2clicked()
        }
        view.btn_3.setOnClickListener {
            btn_3clicked()
        }
        view.btn_4.setOnClickListener {
            btn_4clicked()
        }
        view.btn_5.setOnClickListener {
            btn_5clicked()
        }


        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetButtonClickListener2 = context as BottomSheetButtonClickListener
        }catch (e: ClassCastException) {
            Log.d("BottomSheet_fashion", "onAttach error")
        }
    }

    interface BottomSheetButtonClickListener{
        fun onLevel2ButtonClicked(text: String)
    }

    fun btn_1clicked() {
        when (levelchoice) {
            null -> {
                if (!btn_1) {
                    view?.btn_1!!.setBackgroundResource(R.drawable.button_choice_background)
                    view?.btn_1!!.setTextColor(Color.rgb(99,80,172))
                    btn_1 = true
                    levelchoice = "1단계"
                } else {
                    view?.btn_1!!.setBackgroundResource(R.drawable.button_background)
                    view?.btn_1!!.setTextColor(Color.BLACK)
                    btn_1 = false
                    levelchoice = null
                }
            }
            "1단계" -> {
                view?.btn_1!!.setBackgroundResource(R.drawable.button_background)
                view?.btn_1!!.setTextColor(Color.BLACK)
                btn_1 = false
                levelchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun btn_2clicked() {
        when (levelchoice) {
            null -> {
                if (!btn_2) {
                    view?.btn_2!!.setBackgroundResource(R.drawable.button_choice_background)
                    view?.btn_2!!.setTextColor(Color.rgb(99,80,172))
                    btn_2 = true
                    levelchoice = "2단계"
                } else {
                    view?.btn_2!!.setBackgroundResource(R.drawable.button_background)
                    view?.btn_2!!.setTextColor(Color.BLACK)
                    btn_2 = false
                    levelchoice = null
                }
            }
            "2단계" -> {
                view?.btn_2!!.setBackgroundResource(R.drawable.button_background)
                view?.btn_2!!.setTextColor(Color.BLACK)
                btn_2 = false
                levelchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun btn_3clicked() {
        when (levelchoice) {
            null -> {
                if (!btn_3) {
                    view?.btn_3!!.setBackgroundResource(R.drawable.button_choice_background)
                    view?.btn_3!!.setTextColor(Color.rgb(99,80,172))
                    btn_3 = true
                    levelchoice = "3단계"
                } else {
                    view?.btn_3!!.setBackgroundResource(R.drawable.button_background)
                    view?.btn_3!!.setTextColor(Color.BLACK)
                    btn_3 = false
                    levelchoice = null
                }
            }
            "3단계" -> {
                view?.btn_3!!.setBackgroundResource(R.drawable.button_background)
                view?.btn_3!!.setTextColor(Color.BLACK)
                btn_3 = false
                levelchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun btn_4clicked() {
        when (levelchoice) {
            null -> {
                if (!btn_4) {
                    view?.btn_4!!.setBackgroundResource(R.drawable.button_choice_background)
                    view?.btn_4!!.setTextColor(Color.rgb(99,80,172))
                    btn_4 = true
                    levelchoice = "4단계"
                } else {
                    view?.btn_4!!.setBackgroundResource(R.drawable.button_background)
                    view?.btn_4!!.setTextColor(Color.BLACK)
                    btn_3 = false
                    levelchoice = null
                }
            }
            "4단계" -> {
                view?.btn_4!!.setBackgroundResource(R.drawable.button_background)
                view?.btn_4!!.setTextColor(Color.BLACK)
                btn_4 = false
                levelchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun btn_5clicked() {
        when (levelchoice) {
            null -> {
                if (!btn_5) {
                    view?.btn_5!!.setBackgroundResource(R.drawable.button_choice_background)
                    view?.btn_5!!.setTextColor(Color.rgb(99,80,172))
                    btn_5 = true
                    levelchoice = "5단계"
                } else {
                    view?.btn_5!!.setBackgroundResource(R.drawable.button_background)
                    view?.btn_5!!.setTextColor(Color.BLACK)
                    btn_5 = false
                    levelchoice = null
                }
            }
            "5단계" -> {
                view?.btn_5!!.setBackgroundResource(R.drawable.button_background)
                view?.btn_5!!.setTextColor(Color.BLACK)
                btn_5 = false
                levelchoice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}