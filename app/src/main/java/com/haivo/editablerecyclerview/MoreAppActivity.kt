package com.haivo.editablerecyclerview

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
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
        commonAppsAdapter = CommonAppsAdapter()

        binding.rvApps.apply {
            val gridLayoutManager =
                GridLayoutManager(this@MoreAppActivity, 6, LinearLayoutManager.VERTICAL, false)

            layoutManager = gridLayoutManager
            addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = 230
                }

            })
            adapter = commonAppsAdapter
        }

        enableDragItem()

        setClick()
    }

    private fun setClick() {
        binding.tvEditApps.setOnClickListener {
            viewModel.deleteAll()
            viewModel.insertList(commonAppsAdapter.data)
            commonAppsAdapter.notifyDataSetChanged()

            ToastUtils.showShort("保存成功")
        }
    }

    private fun enableDragItem() {
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
                return true

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun canDropOver(
                recyclerView: RecyclerView,
                current: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun isLongPressDragEnabled() = true
        })


        itemTouchHelper.attachToRecyclerView(binding.rvApps)

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
    }
}