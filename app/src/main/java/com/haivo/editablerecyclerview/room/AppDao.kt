package com.haivo.editablerecyclerview.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haivo.editablerecyclerview.bean.AppBean
import kotlinx.coroutines.flow.Flow

/**
 * 更多App数据库操作接口
 *
 * @author wumaoqiang
 * @date 2021/12/25 11:50
 */
@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list: List<AppBean>)

    @Query("SELECT * FROM apps_table")
    fun getCommonApps(): Flow<List<AppBean>>

    @Query("DELETE FROM apps_table")
    fun deleteAll()
}
