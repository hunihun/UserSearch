package com.hunihun.usersearch.main.extension

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("bind_user_id")
fun bindUserId(view: TextView, id: String) {
    view.text = id
}

@BindingAdapter("bind_update_time")
fun bindUpdateTime(view: TextView, time: String) {
    if (time.isEmpty()) return
    //현재시간 Date
    var curDate = Date(System.currentTimeMillis())

    //formater
    val curDateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    //요청시간을 Date로 parsing 후 time가져오기
    val reqDate = serverDateFormat.parse(time)
    val reqDateTime = reqDate.time

    val date = curDateFormat.format(reqDate)
    val curDateString = curDateFormat.format(curDate)

    //현재시간을 요청시간의 형태로 format 후 time 가져오기
    curDate = curDateFormat.parse(curDateFormat.format(curDate))!!
    val curDateTime = curDate.time

    //분으로 표현
    val minute = (curDateTime - reqDateTime) / 60000

    if (curDateString.substring(0, 8) == date.substring(0, 8)) {
        when {
            date.substring(8, 10).toInt() > 12 -> {
                view.text = "오후 " + (date.substring(8, 10).toInt()-12) + ":" + date.substring(10, 12)
            }
            date.substring(8, 10).toInt() == 12 -> {
                view.text = "오후 12:" + date.substring(10, 12)
            }
            else -> {
                view.text = "오전 " + date.substring(8, 10) + ":" + date.substring(10, 12)
            }
        }
    } else if ((curDateString.substring(0, 6) == date.substring(0, 6)) && (curDateString.substring(6, 8).toInt() - date.substring(6, 8).toInt() == 1)) {
        view.text = "어제"
    } else if ((curDateString.substring(0, 4) == date.substring(0, 4)) && (curDateString.substring(4, 6).toInt() - date.substring(4, 6).toInt() == 1)) {
        if (minute < 1440) {
            view.text = "어제"
        } else {
            view.text = date.substring(4, 6).toInt().toString() + "월 " + date.substring(6, 8).toInt().toString() + "일"
        }
    } else {
        if (minute < 1440) {
            view.text = "어제"
        } else {
            view.text = date.substring(4, 6).toInt().toString() + "월 " + date.substring(6, 8).toInt().toString() + "일"
        }
    }
}