package com.oguzhancetin.howmanytimes.util

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("setTime")
fun TextView.setTime(left: Int){
    var timeString = "sec: $left"
    text = timeString
}