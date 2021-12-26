package com.haivo.editablerecyclerview.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
    // 最大item个数, 超出个数不予显示
    var maxCount = 8

    // 最小item个数,小于此书不做操作
    var minCount = 4
    var onRemoveBtnClickListener: OnRemoveBtnClickListener? = null
    var isInEditing = false //是否处于编辑状态, 编辑状态时点击不能跳转

    @Volatile
    var data: MutableList<AppBean> = mutableListOf()
        set(value) {
            field = if (value.size > maxCount) {
                value.take(maxCount).toMutableList()
            } else {
                value
            }
            notifyDataSetChanged()
        }
    var dragOverListener: DragOverListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_apps, parent, false)
        return AppsHolder(view)
    }

    override fun getItemCount(): Int {
        return if (data.size > maxCount) maxCount else data.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
        val appBean = data[holder.adapterPosition]
        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
        holder.binding.ivOption.setImageResource(when (appBean.option) {
            "1" -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                R.drawable.ic_add
            }
            "2" -> {
                holder.binding.ivOption.visibility = View.VISIBLE
                holder.binding.ivOption.setOnClickListener {
                    onRemoveBtnClickListener?.onClick(it, appBean, position)
                }
                R.drawable.ic_remove
            }
            "3" -> {
                holder.binding.ivOption.visibility = View.GONE
                R.drawable.null_palceholder
            }
            else -> R.drawable.ic_add

        })

        holder.binding.tvAppName.text = appBean.name
        holder.funcUrl = appBean.uid
        holder.binding.root.setOnClickListener {
            if (appBean.action != null && !isInEditing) {
                val packageName = appBean.action?.let {
                    val substring = it.substring(0, it.lastIndexOf("."))
                    substring
                }
                Log.i("xiaoqiang", "packageName: $packageName ")
                ActivityUtils.getTopActivity()
                    .startActivity(Intent().setClassName(packageName!!, appBean.action!!))
            }
        }

        // drag item with onTouch
        if (dragOverListener != null) {
            holder.binding.root.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragOverListener?.startDragItem(holder)
                }
                return@setOnTouchListener false
            }
        }
    }
}

class AppsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemHomeAppsBinding.bind(itemView)
    var funcUrl = ""
}

interface DragOverListener {
    fun startDragItem(holder: RecyclerView.ViewHolder)
}

interface OnRemoveBtnClickListener {
    fun onClick(view: View, appBean: AppBean, position: Int)
}