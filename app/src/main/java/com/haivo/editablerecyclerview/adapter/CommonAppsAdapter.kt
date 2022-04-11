package com.haivo.editablerecyclerview.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.haivo.editablerecyclerview.R
import com.haivo.editablerecyclerview.bean.AppBean
import com.haivo.editablerecyclerview.databinding.ItemHomeAppsBinding


/**
 * 常用Apps的适配器
 *
 * @author wumaoqiang
 * @date 2021/12/25 15:12
 */


class CommonAppsAdapter : RecyclerView.Adapter<AppsHolder>() {
    @Volatile
    var data: MutableList<AppBean> = mutableListOf()
        set(value) {
            notifyDataSetChanged()
            field = value
        }
//    var dragOverListener: DragOverListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_apps, parent, false)
        return AppsHolder(view)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
        val appBean = data[holder.adapterPosition]
        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)

        holder.binding.tvAppName.text = appBean.name
        holder.funcUrl = appBean.uid
        holder.binding.root.setOnClickListener {
            if (appBean.action != null) {
                val packageName = appBean.action?.let {
                    val substring = it.substring(0, it.lastIndexOf("."))
                    substring
                }
                Log.i("xiaoqiang", "packageName: $packageName ")
                ActivityUtils.getTopActivity()
                    .startActivity(Intent().setClassName(packageName!!, appBean.action!!))
            }
        }
    }
}

class AppsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemHomeAppsBinding.bind(itemView)
    var funcUrl = ""
}
