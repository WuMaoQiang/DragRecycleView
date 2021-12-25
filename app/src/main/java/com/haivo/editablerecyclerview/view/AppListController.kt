package com.haivo.editablerecyclerview.view

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ActivityUtils
import com.haivo.editablerecyclerview.MoreAppActivity
import com.haivo.editablerecyclerview.R
import com.haivo.editablerecyclerview.room.AppsRepository
import com.haivo.editablerecyclerview.room.AppsRoomDatabase
import com.haivo.editablerecyclerview.viewmodel.AppViewModelFactory
import com.haivo.editablerecyclerview.viewmodel.AppsViewModel

/**
 * 聚合页AppList的相关操作都在这里执行
 *
 * @author wumaoqiang
 * @date 2021/12/25 16:22
 */
private const val TAG = "AppListController"

class AppListController private constructor(
    rootView: View,
    lifecycleOwner: AppCompatActivity,
    appsViewModel: AppsViewModel
) {

    private var horizontalItemsLayout =
        rootView.findViewById<HorizontalItemsLayout>(R.id.horizontal_items)

    companion object {
        fun inject(
            rootView: View,
            lifecycleOwner: AppCompatActivity,
        ): AppListController {
            val viewModel: AppsViewModel by lifecycleOwner.viewModels {
                AppViewModelFactory(
                    AppsRepository(
                        AppsRoomDatabase.getDatabase(lifecycleOwner).getCommonAppsDao()
                    )
                )
            }
            return AppListController(rootView, lifecycleOwner, viewModel)
        }
    }

    init {

        horizontalItemsLayout.setOnLeftSlideListener {
            lifecycleOwner.startActivity(
                Intent(ActivityUtils.getTopActivity(), MoreAppActivity::class.java)
            )
        }

        appsViewModel.commonApps.observe(lifecycleOwner, {
            Log.i(TAG, "commonApps.size= ${it.size}")
            horizontalItemsLayout.setList(it)
        })
    }

}