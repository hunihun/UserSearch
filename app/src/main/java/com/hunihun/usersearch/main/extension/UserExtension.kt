package com.hunihun.usersearch.main.extension

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("bind_user_id")
fun bindUserId(view: TextView, id: String) {
    view.text = id
}