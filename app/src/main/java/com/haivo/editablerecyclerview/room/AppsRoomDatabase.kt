package com.haivo.editablerecyclerview.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.haivo.editablerecyclerview.bean.AppBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * 更多App数据库，初始化会创建默认的CommonApps存入数据库
 *
 * @author wumaoqiang
 * @date 2021/12/25 11:46
 */

@Database(
    entities = [AppBean::class],
    version = 1,
    exportSchema = false
)
abstract class AppsRoomDatabase : RoomDatabase() {

    abstract fun getCommonAppsDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppsRoomDatabase? = null

        fun getDatabase(
            context: Context,
        ): AppsRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppsRoomDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private class WordDatabaseCallback : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val applicationScope = CoroutineScope(SupervisorJob())
                INSTANCE?.let { database ->
                    applicationScope.launch(Dispatchers.IO) {
                        populateDatabase(database.getCommonAppsDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        fun populateDatabase(appDao: AppDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            appDao.deleteAll()

            val commonApps = mutableListOf(
                AppBean().apply {
                    uid = "func_uid_001"
                    name = "人车查询"
                },
                AppBean().apply {
                    uid = "func_uid_002"
                    name = "访客登记"
                },
                AppBean().apply {
                    uid = "func_uid_003"
                    name = "事件上报"
                },
                AppBean().apply {
                    uid = "func_uid_004"
                    name = "通行证"
                },
                AppBean().apply {
                    uid = "func_uid_005"
                    name = "云打印"
                })
            appDao.insertList(commonApps)
        }
    }
}