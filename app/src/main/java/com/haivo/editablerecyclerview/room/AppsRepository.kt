package com.haivo.editablerecyclerview.room

import com.haivo.editablerecyclerview.bean.AppBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

/**
 * 更多App,Model类
 *
 * @author wumaoqiang
 * @date 2021/12/25 13:28
 */
class AppsRepository(private val appDao: AppDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val commonAppsFlow: Flow<List<AppBean>> = appDao.getCommonApps().distinctUntilChanged()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    suspend fun insertList(list: List<AppBean>) {
        withContext(Dispatchers.IO) {
            appDao.insertList(list)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            appDao.deleteAll()
        }
    }
}