package com.example.ehs.BottomSheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ehs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_tpo.view.*

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_tpo(val itemClick: (String) -> Unit) :
    BottomSheetDialogFragment() {

    var tpochoice : String? = null
    var dailylookclicked = false
    var datelookclicked = false
    var officelookclicked = false
    var partylookclicked = false
    var guestlookclicked = false
    var hikinglookclicked = false
    var athleisurelookclicked = false
    var speciallookclicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.bottomsheet_tpo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btn_dailylook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!dailylookclicked) {
                        view.btn_dailylook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_dailylook.setTextColor(Color.rgb(99,80,172))
                        dailylookclicked = true
                        tpochoice = "데일리룩"
                    } else {
                        view.btn_dailylook.setBackgroundResource(R.drawable.button_background)
                        view.btn_dailylook.setTextColor(Color.BLACK)
                        dailylookclicked = false
                        tpochoice = null
                    }
                }
                "데일리룩" -> {
                    view.btn_dailylook.setBackgroundResource(R.drawable.button_background)
                    view.btn_dailylook.setTextColor(Color.BLACK)
                    dailylookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        view.btn_datelook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!datelookclicked) {
                        view.btn_datelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_datelook.setTextColor(Color.rgb(99,80,172))
                        datelookclicked = true
                        tpochoice = "데이트룩"
                    } else {
                        view.btn_datelook.setBackgroundResource(R.drawable.button_background)
                        view.btn_datelook.setTextColor(Color.BLACK)
                        datelookclicked = false
                        tpochoice = null
                    }
                }
                "데이트룩" -> {
                    view.btn_datelook.setBackgroundResource(R.drawable.button_background)
                    view.btn_datelook.setTextColor(Color.BLACK)
                    datelookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_officelook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!officelookclicked) {
                        view.btn_officelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_officelook.setTextColor(Color.rgb(99,80,172))
                        officelookclicked = true
                        tpochoice = "오피스룩"
                    } else {
                        view.btn_officelook.setBackgroundResource(R.drawable.button_background)
                        view.btn_officelook.setTextColor(Color.BLACK)
                        officelookclicked = false
                        tpochoice = null
                    }
                }
                "오피스룩" -> {
                    view.btn_officelook.setBackgroundResource(R.drawable.button_background)
                    view.btn_officelook.setTextColor(Color.BLACK)
                    officelookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_partylook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!partylookclicked) {
                        view.btn_partylook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_partylook.setTextColor(Color.rgb(99,80,172))
                        partylookclicked = true
                        tpochoice = "파티룩"
                    } else {
                        view.btn_partylook.setBackgroundResource(R.drawable.button_background)
                        view.btn_partylook.setTextColor(Color.BLACK)
                        partylookclicked = false
                        tpochoice = null
                    }
                }
                "파티룩" -> {
                    view.btn_partylook.setBackgroundResource(R.drawable.button_background)
                    view.btn_partylook.setTextColor(Color.BLACK)
                    partylookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_guestlook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!guestlookclicked) {
                        view.btn_guestlook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_guestlook.setTextColor(Color.rgb(99,80,172))
                        guestlookclicked = true
                        tpochoice = "하객룩"
                    } else {
                        view.btn_guestlook.setBackgroundResource(R.drawable.button_background)
                        view.btn_guestlook.setTextColor(Color.BLACK)
                        guestlookclicked = false
                        tpochoice = null
                    }
                }
                "하객룩" -> {
                    view.btn_guestlook.setBackgroundResource(R.drawable.button_background)
                    view.btn_guestlook.setTextColor(Color.BLACK)
                    guestlookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_hikinglook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!hikinglookclicked) {
                        view.btn_hikinglook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_hikinglook.setTextColor(Color.rgb(99,80,172))
                        hikinglookclicked = true
                        tpochoice = "등산룩"
                    } else {
                        view.btn_hikinglook.setBackgroundResource(R.drawable.button_background)
                        view.btn_hikinglook.setTextColor(Color.BLACK)
                        hikinglookclicked = false
                        tpochoice = null
                    }
                }
                "등산룩" -> {
                    view.btn_hikinglook.setBackgroundResource(R.drawable.button_background)
                    view.btn_hikinglook.setTextColor(Color.BLACK)
                    hikinglookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_athleisurelook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!athleisurelookclicked) {
                        view.btn_athleisurelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_athleisurelook.setTextColor(Color.rgb(99,80,172))
                        athleisurelookclicked = true
                        tpochoice = "애슬레저룩"
                    } else {
                        view.btn_athleisurelook.setBackgroundResource(R.drawable.button_background)
                        view.btn_athleisurelook.setTextColor(Color.BLACK)
                        athleisurelookclicked = false
                        tpochoice = null
                    }
                }
                "애슬레저룩" -> {
                    view.btn_athleisurelook.setBackgroundResource(R.drawable.button_background)
                    view.btn_athleisurelook.setTextColor(Color.BLACK)
                    athleisurelookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_speciallook.setOnClickListener {
            when (tpochoice) {
                null -> {
                    if (!speciallookclicked) {
                        view.btn_speciallook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_speciallook.setTextColor(Color.rgb(99,80,172))
                        speciallookclicked = true
                        tpochoice = "스페셜룩"
                    } else {
                        view.btn_speciallook.setBackgroundResource(R.drawable.button_background)
                        view.btn_speciallook.setTextColor(Color.BLACK)
                        speciallookclicked = false
                        tpochoice = null
                    }
                }
                "스페셜룩" -> {
                    view.btn_speciallook.setBackgroundResource(R.drawable.button_background)
                    view.btn_speciallook.setTextColor(Color.BLACK)
                    speciallookclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }
        view.btn_tpochoice.setOnClickListener{
            itemClick(tpochoice!!)
            dialog?.dismiss()
        }
    }
}