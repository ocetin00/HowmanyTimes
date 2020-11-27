package com.oguzhancetin.howmanytimes.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Player(
  var nickname:String? = "",
  var skor:Long? =0
) : Parcelable
