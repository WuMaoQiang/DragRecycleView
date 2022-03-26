package com.haivo.editablerecyclerview.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ActivityUtils
import com.haivo.editablerecyclerview.R
import com.haivo.editablerecyclerview.bean.AppBean


/**
 * 所有Apps的Adapter
 *
 * @author wumaoqiang
 * @date 2021/12/25 15:42
 */
//class AllAppsAdapter : RecyclerView.Adapter<AppsHolder>() {
//    var isInEditing = false //是否处于编辑状态, 编辑状态时点击不能跳转
//
//    @Volatile
//    var data: MutableList<AppBean> = mutableListOf()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//    var onAddBtnClickListener: OnAddBtnClickListener? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppsHolder {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_home_apps, parent, false)
//        return AppsHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }
//
//    override fun onBindViewHolder(holder: AppsHolder, position: Int) {
//        val appBean = data[holder.adapterPosition]
//        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
//        holder.binding.ivOption.setImageResource(when (appBean.option) {
//            "1" -> {
//                holder.binding.ivOption.visibility = View.VISIBLE
//                holder.binding.ivOption.setOnClickListener {
//                    onAddBtnClickListener?.onClick(it, appBean)
//                }
//                R.drawable.ic_add
//            }
//            "2" -> {
//                holder.binding.ivOption.visibility = View.VISIBLE
//                holder.binding.ivOption.setOnClickListener {
//                    data.removeAt(holder.adapterPosition)
//                    notifyItemRemoved(holder.adapterPosition)
//                }
//                R.drawable.ic_remove
//            }
//            "3" -> {
//                holder.binding.ivOption.visibility = View.GONE
//                R.drawable.null_palceholder
//            }
//            else -> R.drawable.ic_add
//
//        })
//        holder.binding.ivAppIcon.setImageResource(appBean.iconRes)
//        holder.binding.tvAppName.text = appBean.name
//        holder.funcUrl = appBean.uid
//        holder.binding.root.setOnClickListener {
//            if (appBean.action != null && !isInEditing) {
//                val packageName = appBean.action?.let {
//                    val substring = it.substring(0, it.lastIndexOf("."))
//                    substring
//                }
//                Log.i("xiaoqiang", "packageName: $packageName ")
//                ActivityUtils.getTopActivity()
//                    .startActivity(Intent().setClassName(packageName!!, appBean.action!!))
//            }
//        }
//    }
//}
//
//interface OnAddBtnClickListener {
//    fun onClick(view: View, appBean: AppBean)
//}