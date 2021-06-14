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


/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_category : BottomSheetDialogFragment() {
    var choice : String? = null
    var topclicked = false
    var outerclicked = false
    var bottomclicked = false
    var dressclicked = false
    var shoesclicked = false
    var capclicked = false
    var bagclicked = false
    var etcclicked = false
    lateinit var bottomSheetButtonClickListener : BottomSheetButtonClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.bottomsheet_category, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bottomSheetButtonClickListener = context as BottomSheetButtonClickListener
        }catch (e: ClassCastException) {
            Log.d("BottomSheet_category", "onAttach error")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<Button>(R.id.btn_choicecom)?.setOnClickListener {
            Log.d("카테고리", "선택사항은" + choice)
            bottomSheetButtonClickListener.onCategoryButtonClicked(choice!!)
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_top)?.setOnClickListener { Topclicked() }
        view?.findViewById<Button>(R.id.btn_outer)?.setOnClickListener { Outerclicked() }
        view?.findViewById<Button>(R.id.btn_bottom)?.setOnClickListener { Bottomclicked() }
        view?.findViewById<Button>(R.id.btn_dress)?.setOnClickListener { Dressclicked() }
        view?.findViewById<Button>(R.id.btn_shoes)?.setOnClickListener { Shoesclicked() }
        view?.findViewById<Button>(R.id.btn_cap)?.setOnClickListener { Capclicked() }
        view?.findViewById<Button>(R.id.btn_bag)?.setOnClickListener { Bagclicked() }
        view?.findViewById<Button>(R.id.btn_etc)?.setOnClickListener { Etcclicked() }
    }


    interface BottomSheetButtonClickListener{
        fun onCategoryButtonClicked(text: String)
    }



    /**
     * 함수호출 : 각 카테고리 버튼 클릭 시 색깔 바꿈 & 값 받아오기
     */

    fun Topclicked() {
        when (choice) {
            null -> {
                if (!topclicked) {
                    btn_top.setBackgroundResource(R.drawable.button_choice_background)
                    btn_top.setTextColor(Color.rgb(99,80,172))
                    topclicked = true
                    choice = "상의"
                } else {
                    btn_top.setBackgroundResource(R.drawable.button_background)
                    btn_top.setTextColor(Color.BLACK)
                    topclicked = false
                    choice = null
                }
            }
            "상의" -> {
                btn_top.setBackgroundResource(R.drawable.button_background)
                btn_top.setTextColor(Color.BLACK)
                topclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Outerclicked() {
        when (choice) {
            null -> {
                if (!outerclicked) {
                    btn_outer.setBackgroundResource(R.drawable.button_choice_background)
                    btn_outer.setTextColor(Color.rgb(99,80,172))
                    outerclicked = true
                    choice = "아우터"
                } else {
                    btn_outer.setBackgroundResource(R.drawable.button_background)
                    btn_outer.setTextColor(Color.BLACK)
                    outerclicked = false
                    choice = null
                }
            }
            "아우터" -> {
                btn_outer.setBackgroundResource(R.drawable.button_background)
                btn_outer.setTextColor(Color.BLACK)
                outerclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Bottomclicked() {
        when (choice) {
            null -> {
                if (!bottomclicked) {
                    btn_bottom.setBackgroundResource(R.drawable.button_choice_background)
                    btn_bottom.setTextColor(Color.rgb(99,80,172))
                    bottomclicked = true
                    choice = "하의"
                } else {
                    btn_bottom.setBackgroundResource(R.drawable.button_background)
                    btn_bottom.setTextColor(Color.BLACK)
                    bottomclicked = false
                    choice = null
                }
            }
            "하의" -> {
                btn_bottom.setBackgroundResource(R.drawable.button_background)
                btn_bottom.setTextColor(Color.BLACK)
                bottomclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Dressclicked() {
        when (choice) {
            null -> {
                if (!dressclicked) {
                    btn_dress.setBackgroundResource(R.drawable.button_choice_background)
                    btn_dress.setTextColor(Color.rgb(99,80,172))
                    dressclicked = true
                    choice = "원피스"
                } else {
                    btn_dress.setBackgroundResource(R.drawable.button_background)
                    btn_dress.setTextColor(Color.BLACK)
                    dressclicked = false
                    choice = null
                }
            }
            "원피스" -> {
                btn_dress.setBackgroundResource(R.drawable.button_background)
                btn_dress.setTextColor(Color.BLACK)
                dressclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Shoesclicked() {
        when (choice) {
            null -> {
                if (!shoesclicked) {
                    btn_shoes.setBackgroundResource(R.drawable.button_choice_background)
                    btn_shoes.setTextColor(Color.rgb(99,80,172))
                    shoesclicked = true
                    choice = "신발"
                } else {
                    btn_shoes.setBackgroundResource(R.drawable.button_background)
                    btn_shoes.setTextColor(Color.BLACK)
                    shoesclicked = false
                    choice = null
                }
            }
            "신발" -> {
                btn_shoes.setBackgroundResource(R.drawable.button_background)
                btn_shoes.setTextColor(Color.BLACK)
                shoesclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Capclicked() {
        when (choice) {
            null -> {
                if (!capclicked) {
                    btn_cap.setBackgroundResource(R.drawable.button_choice_background)
                    btn_cap.setTextColor(Color.rgb(99,80,172))
                    capclicked = true
                    choice = "모자"
                } else {
                    btn_cap.setBackgroundResource(R.drawable.button_background)
                    btn_cap.setTextColor(Color.BLACK)
                    capclicked = false
                    choice = null
                }
            }
            "모자" -> {
                btn_cap.setBackgroundResource(R.drawable.button_background)
                btn_cap.setTextColor(Color.BLACK)
                capclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Bagclicked() {
        when (choice) {
            null -> {
                if (!bagclicked) {
                    btn_bag.setBackgroundResource(R.drawable.button_choice_background)
                    btn_bag.setTextColor(Color.rgb(99,80,172))
                    bagclicked = true
                    choice = "가방"
                } else {
                    btn_bag.setBackgroundResource(R.drawable.button_background)
                    btn_bag.setTextColor(Color.BLACK)
                    bagclicked = false
                    choice = null
                }
            }
            "가방" -> {
                btn_bag.setBackgroundResource(R.drawable.button_background)
                btn_bag.setTextColor(Color.BLACK)
                bagclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun Etcclicked() {
        when (choice) {
            null -> {
                if (!etcclicked) {
                    btn_etc.setBackgroundResource(R.drawable.button_choice_background)
                    btn_etc.setTextColor(Color.rgb(99,80,172))
                    etcclicked = true
                    choice = "기타"
                } else {
                    btn_etc.setBackgroundResource(R.drawable.button_background)
                    btn_etc.setTextColor(Color.BLACK)
                    etcclicked = false
                    choice = null
                }
            }
            "기타" -> {
                btn_etc.setBackgroundResource(R.drawable.button_background)
                btn_etc.setTextColor(Color.BLACK)
                etcclicked = false
                choice = null
            }
            else -> {
                Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

}