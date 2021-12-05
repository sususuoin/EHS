package com.example.ehs.Admin


import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.example.ehs.Admin.ManagementLevel_Activity.Companion.levelModifyActivity_Dialog
import com.example.ehs.R
import kotlinx.android.synthetic.main.fashionista.view.*
import kotlinx.android.synthetic.main.management_level.view.*
import kotlinx.android.synthetic.main.management_level.view.mName
import kotlinx.android.synthetic.main.management_level.view.mProfile
import org.json.JSONException
import org.json.JSONObject

class ManagementLevelListAdapter(private val itemList: List<ManagementLevel>)
    : RecyclerView.Adapter<ManagementLevelListAdapter.ViewHolder>()  {

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.management_level, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        val layoutParams = holder.itemView.layoutParams
        layoutParams.height = 200
        holder.itemView.requestLayout()


        if (itemList[position].level == "대기중") {
            holder.itemView.findViewById<Button>(R.id.btn_ok_ourcolor).visibility = View.VISIBLE
            holder.itemView.findViewById<Button>(R.id.btn_ok).visibility = View.GONE
        }


        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        
        holder.apply {
            bind(item)

            itemView.btn_ok_ourcolor.setOnClickListener {
                var levelup_alert = AlertDialog.Builder(holder.itemView.context)
                levelup_alert.setTitle("레벨업 확인창")
                levelup_alert.setMessage("전문가 레벨업을 승인하시겠습니까?")

                // 버튼 클릭시에 무슨 작업을 할 것인가!
                var listener = object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        when (p1) {
                            // 확인 버튼 클릭 시
                            DialogInterface.BUTTON_POSITIVE -> {

                                levelModifyActivity_Dialog!!.show()

                                saveLevelUp(holder, position)

                            }
                        }
                    }
                }
                levelup_alert.setPositiveButton("확인", listener)
                levelup_alert.setNegativeButton("취소", null)
                levelup_alert.show()
            }
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view : View = v

        fun bind(item: ManagementLevel) {
            view.mName.text = item.name
            view.mLevel.text = item.level2
            view.mProfile.setImageBitmap(item.profile)
            view.tv_cntlike.text = item.cntlike
            view.tv_cntnolike.text = item.cntnolike
        }
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener


    fun saveLevelUp(holder: ViewHolder, position: Int) {

        holder.itemView.findViewById<Button>(R.id.btn_ok_ourcolor).visibility = View.GONE
        holder.itemView.findViewById<Button>(R.id.btn_ok).visibility = View.VISIBLE

        val responseListener: Response.Listener<String?> = object : Response.Listener<String?> {
            override fun onResponse(response: String?) {
                try {
                    val jsonObject = JSONObject(response)
                    var success = jsonObject.getBoolean("success")

                    if(success) {
                        Toast.makeText(holder.itemView.context, "수정 성공", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(holder.itemView.context, "수정 실패", Toast.LENGTH_SHORT).show()

                        return
                    }
                    levelModifyActivity_Dialog!!.dismiss()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

        }

        //서버로 Volley를 이용해서 요청

        val managementLevelUp_Request = ManagementLevelUp_Request(itemList[position].name, responseListener)
        val queue = Volley.newRequestQueue(holder.itemView.context)
        queue.add(managementLevelUp_Request)

    }

}