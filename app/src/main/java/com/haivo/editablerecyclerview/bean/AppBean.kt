package com.haivo.editablerecyclerview.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haivo.editablerecyclerview.R


/**
 * AppBean
 *
 * @author wumaoqiang
 * @date 2021/12/25 11:22
 */
@Entity(tableName = "apps_table")
class AppBean() : Parcelable {

    var name: String = ""

    @PrimaryKey
    var uid: String = ""
    var iconRes: Int = -1
        get() = if (getFunctionDrawable(uid) != -1) getFunctionDrawable(uid) else R.drawable.photobadge

    // 图标跳转
    var action: String? = ""
        get() = getFunctionActionByUrl(uid)

    constructor(parcel: Parcel) : this() {
        name = parcel.readString() ?: ""
        uid = parcel.readString() ?: ""
        iconRes = parcel.readInt()
        action = parcel.readString() ?: ""
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<AppBean> {
            override fun createFromParcel(parcel: Parcel): AppBean {
                return AppBean(parcel)
            }

            override fun newArray(size: Int): Array<AppBean?> {
                return arrayOfNulls(size)
            }
        }

        fun getFunctionDrawable(appUid: String): Int {
            val map = mapOf(
                Pair("func_uid_001", R.drawable.ic_person_car_query), // 人车查询
                Pair("func_uid_002", R.drawable.ic_visitor_record), // 访客登记
                Pair("func_uid_003", R.drawable.ic_event_report), // 事件上报
                Pair("func_uid_004", R.drawable.ic_passport), // 通行证
                Pair("func_uid_005", R.drawable.ic_cloud_print), // 云打印
                Pair("func_uid_006", R.drawable.ic_most_once), // 最多跑一次
                Pair("func_uid_007", R.drawable.ic_garbage), // 垃圾分类
                Pair("func_uid_008", R.drawable.ic_health_code), // 健康码
                Pair("func_uid_009", R.drawable.ic_visitor_order), //  访客预约
                Pair("func_uid_010", R.drawable.ic_park_monitor), // 园区看护
                Pair("func_uid_011", R.drawable.ic_wiki), // 物管知识库
                Pair("func_uid_012", R.drawable.ic_survy), // 调查问卷
                Pair("func_uid_013", R.drawable.ic_emergence_report), // 应急上报
                Pair("func_leadership_view", R.drawable.ic_leadership_view), // 领导视图
                Pair("func_query_parking", R.drawable.ic_query_parking) // 车位查询
            )
            return map[appUid] ?: -1
        }

        fun getFunctionActionByUrl(funcUrl: String): String? {
            val map = mapOf(
                // 应用部分
                Pair("func_uid_001", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_002", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_003", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_004", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_005", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_006", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_007", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_008", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_009", "com.haivo.editablerecyclerview.QueryPersonCarActivity"),
                Pair("func_uid_010", null),
                Pair("func_uid_011", null),
                Pair("func_uid_012", null),
                Pair("func_uid_013", null),
                Pair("func_leadership_view", null),
                Pair("func_query_parking", null),

                Pair("func_uid_101", null),
                Pair("func_uid_102", null),
                Pair("func_uid_103", null),
                Pair("func_uid_104", null),
            )
            return map[funcUrl]
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(uid)
        parcel.writeInt(iconRes)
        parcel.writeString(action)
    }

    override fun describeContents(): Int {
        return 0
    }

}