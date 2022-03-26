package com.haivo.editablerecyclerview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.haivo.editablerecyclerview.adapter.*
import com.haivo.editablerecyclerview.bean.AppBean
import com.haivo.editablerecyclerview.databinding.ActivityMoreAppsBinding
import com.haivo.editablerecyclerview.room.*
import com.haivo.editablerecyclerview.viewmodel.AppViewModelFactory
import com.haivo.editablerecyclerview.viewmodel.AppsViewModel
import java.util.*

/**
 * 更多应用进入的界面，包含两个recyclerView
 *
 * @author wumaoqiang
 * @date 2021/12/25 15:40
 */
private const val TAG = "MoreAppActivity"

class MoreAppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMoreAppsBinding
    private lateinit var commonAppsAdapter: CommonAppsAdapter

    private val viewModel: AppsViewModel by viewModels {
        AppViewModelFactory(
            AppsRepository(
                AppsRoomDatabase.getDatabase(this).getCommonAppsDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ----------------- 列表1 常用应用------------------
        commonAppsAdapter = CommonAppsAdapter()
        binding.rvApps.apply {
            val gridLayoutManager =
                GridLayoutManager(this@MoreAppActivity, 6, LinearLayoutManager.VERTICAL, false)

            gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == 0 || position == 7) {
                        6
                    } else 1
                }
            }
            layoutManager = gridLayoutManager

            adapter = commonAppsAdapter
        }

        enableDragItem(true)

        setClick()
    }

    private fun setClick() {
        binding.tvEditApps.setOnClickListener {
            commonAppsAdapter.isInEditing = false
            binding.tvEditApps.setTextColor(Color.parseColor("#A0A0A0"))
            commonAppsAdapter.data.onEach { it1 -> it1.option = "3" }
            viewModel.deleteAll()
            viewModel.insertList(commonAppsAdapter.data)
            commonAppsAdapter.notifyDataSetChanged()

            ToastUtils.showShort("保存成功")
        }
    }

    private fun enableDragItem(enable: Boolean) {
        if (enable) {
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return makeMovementFlags(
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
                        0
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    oldHolder: RecyclerView.ViewHolder,
                    targetHolder: RecyclerView.ViewHolder
                ): Boolean {

                    if (targetHolder is AppsHolder) {
                        Log.i(
                            TAG,
                            "onMove: oldHolder.adapterPosition=${oldHolder.adapterPosition}..${targetHolder.adapterPosition}"
                        )
                        if (targetHolder.adapterPosition != 0 || targetHolder.adapterPosition != 7) {
                            Log.i(
                                TAG,
                                "onMove: 222oldHolder.adapterPosition=${oldHolder.adapterPosition}..${targetHolder.adapterPosition}"
                            )
                            commonAppsAdapter.notifyItemMoved(
                                oldHolder.adapterPosition,
                                targetHolder.adapterPosition
                            )

                            // 在每次移动后, 将界面上图标的顺序同步到appsAdapter.data中
                            val newData = mutableListOf<Pair<String, Int>>()
                            commonAppsAdapter.data.forEachIndexed { index, _ ->
                                val holder =
                                    recyclerView.findViewHolderForAdapterPosition(index)
                                if (holder is AppsHolder) {
                                    newData.add(Pair(holder.funcUrl, index))
                                }
                            }
                            for (i in newData) {
                                val sameFuncIndex =
                                    commonAppsAdapter.data.indexOfFirst { i.first == it.uid }
                                Collections.swap(commonAppsAdapter.data, i.second, sameFuncIndex)
                            }
                        }
                        return true
                    } else {
                        return false
                    }
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

                override fun canDropOver(
                    recyclerView: RecyclerView,
                    current: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = true

                override fun isLongPressDragEnabled() = false
            })

            commonAppsAdapter.dragOverListener = object : DragOverListener {
                override fun startDragItem(holder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(holder)
                }
            }
            itemTouchHelper.attachToRecyclerView(binding.rvApps)
        } else {
            commonAppsAdapter.dragOverListener = null
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.commonApps.observe(this, { list ->
            if (list.isNotEmpty()) {
                list.forEach {
                    Log.i(TAG, "commonApps: ${it.name}")
                }
                commonAppsAdapter.data = list as MutableList<AppBean>
            }
        })
//
//        allAppsAdapter.data = mutableListOf(
//            AppBean().apply {
//                uid = "func_uid_001"
//                name = "人车查询"
//            },
//            AppBean().apply {
//                uid = "func_uid_002"
//                name = "访客登记"
//            },
//            AppBean().apply {
//                uid = "func_uid_003"
//                name = "事件上报"
//            },
//            AppBean().apply {
//                uid = "func_uid_004"
//                name = "通行证"
//            },
//            AppBean().apply {
//                uid = "func_uid_005"
//                name = "云打印"
//            },
//            AppBean().apply {
//                uid = "func_uid_006"
//                name = "最多跑一次"
//            },
//            AppBean().apply {
//                uid = "func_uid_007"
//                name = "垃圾分类"
//            },
//            AppBean().apply {
//                uid = "func_uid_008"
//                name = "健康码"
//            },
//            AppBean().apply {
//                uid = "func_uid_009"
//                name = "访客预约"
//            },
//            AppBean().apply {
//                uid = "func_uid_010"
//                name = "园区看护"
//            },
//            AppBean().apply {
//                uid = "func_leadership_view"
//                name = "领导视图"
//            },
//            AppBean().apply {
//                uid = "func_query_parking"
//                name = "车位查询"
//            },
//        )
    }
}