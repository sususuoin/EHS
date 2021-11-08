package com.example.ehs.BottomSheet

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ehs.Loading
import com.example.ehs.Login.AutoLogin
import com.example.ehs.MainActivity
import com.example.ehs.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottomsheet_tpo.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 옷 등록 시 카테고리 지정할 때 하단에 올라올 바텀 시트 호출하는 코틀린 파일
 */
class BottomSheet_tpo(val itemClick: (String) -> Unit) : BottomSheetDialogFragment() {
    private var a: Activity? = null

    var tpochoice : String? = null
    var dailylookclicked = false
    var datelookclicked = false
    var officelookclicked = false
    var partylookclicked = false
    var guestlookclicked = false
    var athleisurelookclicked = false
    var speciallookclicked = false
    var resetclicked = false

    lateinit var userId: String
    var tpo_loading : Loading? = null

    companion object {
        var detail_top = ""
        var detail_bottom = ""
        var detail_outer = ""
        var detail_shoes = ""
        var detail_bag = ""

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("바텀시트", "바텀시트 - onCreate() called")

        userId = AutoLogin.getUserId(a!!)
        Log.d("바텀시트2", userId)

        tpo_loading = Loading(a!!)

        detail_top=""
        detail_bottom=""
        detail_shoes=""
        detail_outer=""
        detail_bag=""

    }

    // 프래그먼트를 안고 있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            a = context
        }
        Log.d("바텀시트", "바텀시트 - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.bottomsheet_tpo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.btn_dailylook.setOnClickListener {
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!dailylookclicked) {
                        view.btn_dailylook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_dailylook.setTextColor(Color.rgb(99,80,172))
                        dailylookclicked = true
                        tpochoice = "데일리룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('면바지', '청바지', '슬랙스', '반바지', '미니스커트', '롱스커트') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('스니커즈', '부츠', '구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!datelookclicked) {
                        view.btn_datelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_datelook.setTextColor(Color.rgb(99,80,172))
                        datelookclicked = true
                        tpochoice = "데이트룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('블라우스', '니트', '셔츠') OR  clothesCategory='원피스' AND clothesSeason!='여름' AND clothesCategory_Detail='패턴원피스' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('면바지', '청바지', '슬랙스', '반바지', '미니스커트', '롱스커트') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId'  AND clothesCategory_Detail IN('가디건', '코트', '점퍼', '수트자켓') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('스니커즈', '부츠', '구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('일반 가방', '에코백') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!officelookclicked) {
                        view.btn_officelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_officelook.setTextColor(Color.rgb(99,80,172))
                        officelookclicked = true
                        tpochoice = "오피스룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('블라우스', '셔츠') ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId' AND clothesCategory_Detail IN('슬랙스') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId' AND clothesCategory_Detail IN('코트', '수트자켓') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId' AND clothesCategory_Detail IN('구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('일반 가방') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!partylookclicked) {
                        view.btn_partylook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_partylook.setTextColor(Color.rgb(99,80,172))
                        partylookclicked = true
                        tpochoice = "파티룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('블라우스', '니트', '셔츠') OR clothesCategory='원피스' AND clothesSeason!='여름' AND clothesCategory_Detail='무지원피스' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('청바지', '반바지', '미니스커트') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId'  AND clothesCategory_Detail IN('가디건', '코트', '수트자켓') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('일반 가방') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!guestlookclicked) {
                        view.btn_guestlook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_guestlook.setTextColor(Color.rgb(99,80,172))
                        guestlookclicked = true
                        tpochoice = "" +
                                "하객룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('블라우스', '니트', '셔츠') AND clothesColor!='흰색' OR clothesCategory='원피스' AND clothesSeason!='여름' AND clothesCategory_Detail='무지원피스' AND userId='$userId' AND clothesColor!='흰색' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('면바지', '청바지', '슬랙스', '롱스커트') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId'  AND clothesCategory_Detail IN('가디건', '코트', '수트자켓') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('부츠', '구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('일반 가방') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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

        view.btn_athleisurelook.setOnClickListener {
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!athleisurelookclicked) {
                        view.btn_athleisurelook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_athleisurelook.setTextColor(Color.rgb(99,80,172))
                        athleisurelookclicked = true
                        tpochoice = "애슬레저룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('티셔츠', '후드') OR clothesCategory='원피스' AND clothesSeason!='여름' AND clothesCategory_Detail='후드원피스' AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('츄리닝') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId'  AND clothesCategory_Detail IN('패딩', '점퍼') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('스니커즈') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('백팩') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!speciallookclicked) {
                        view.btn_speciallook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_speciallook.setTextColor(Color.rgb(99,80,172))
                        speciallookclicked = true
                        tpochoice = "스페셜룩"
                        detail_top =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='상의' AND userId='$userId' AND clothesCategory_Detail IN('블라우스', '니트', '셔츠', '티셔츠') OR clothesCategory='원피스' AND clothesSeason!='여름' AND clothesCategory_Detail IN('패턴원피스', '무지원피스') AND userId='$userId' ORDER BY rand() LIMIT 1"
                        detail_bottom =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='하의' AND userId='$userId'  AND clothesCategory_Detail IN('면바지', '청바지', '반바지', '미니스커트', '롱스커트') ORDER BY rand() LIMIT 1"
                        detail_outer =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='아우터' AND userId='$userId'  AND clothesCategory_Detail IN('가디건', '코트', '점퍼', '수트자켓', '패딩') ORDER BY rand() LIMIT 1"
                        detail_shoes =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='신발' AND userId='$userId'  AND clothesCategory_Detail IN('스니커즈', '부츠', '구두') ORDER BY rand() LIMIT 1"
                        detail_bag =
                            "SELECT clothesCategory, clothesName, clothesCategory_Detail FROM clothes WHERE clothesSeason!='여름' AND clothesCategory='가방' AND userId='$userId' AND clothesCategory_Detail IN('일반 가방') ORDER BY rand() LIMIT 1"
                        (MainActivity.mContext as MainActivity).CodyRandom(detail_top, detail_bottom, detail_shoes, detail_outer, detail_bag)
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

        view.btn_reset.setOnClickListener {
            detail_top=""
            detail_bottom=""
            detail_shoes=""
            detail_outer=""
            detail_bag=""
            when (tpochoice) {
                null -> {
                    if (!resetclicked) {
                        view.btn_speciallook.setBackgroundResource(R.drawable.button_choice_background)
                        view.btn_speciallook.setTextColor(Color.rgb(99,80,172))
                        resetclicked = true
                        tpochoice = "티피오"
                        (MainActivity.mContext as MainActivity).CodyRandom(MainActivity.basic_detail_top,
                            MainActivity.basic_detail_bottom, MainActivity.basic_detail_shoes,
                            MainActivity.basic_detail_outer, MainActivity.basic_detail_bag)
                    } else {
                        view.btn_speciallook.setBackgroundResource(R.drawable.button_background)
                        view.btn_speciallook.setTextColor(Color.BLACK)
                        resetclicked = false
                        tpochoice = null
                    }
                }
                "티피오" -> {
                    view.btn_speciallook.setBackgroundResource(R.drawable.button_background)
                    view.btn_speciallook.setTextColor(Color.BLACK)
                    resetclicked = false
                    tpochoice = null
                }
                else -> {
                    Toast.makeText(activity, "하나의 항목만 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        view.btn_tpochoice.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                launch(Dispatchers.Main) {
                    tpo_loading!!.init("스타일 검색 중")
                }
                delay(1000)

                tpo_loading?.finish()
                dialog?.dismiss()
            }
            itemClick(tpochoice!!)

        }

    }

}