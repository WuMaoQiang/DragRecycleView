package com.haivo.editablerecyclerview.viewmodel

import androidx.lifecycle.*
import com.haivo.editablerecyclerview.bean.AppBean
import com.haivo.editablerecyclerview.room.AppsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 更多App,ViewModel处理界面与Model交互
 *
 * @author wumaoqiang
 * @date 2021/12/25 13:31
 */

class AppsViewModel(private val repository: AppsRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val commonApps: LiveData<List<AppBean>> = repository.commonAppsFlow.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertList(list: List<AppBean>) = viewModelScope.launch {
        //TODO [MoreAppActivity]点击完成，先执行删除马上执行插入操作导致插入不成功，故延迟100ms
        delay(100)
        repository.insertList(list)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}

class AppViewModelFactory(private val repository: AppsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}