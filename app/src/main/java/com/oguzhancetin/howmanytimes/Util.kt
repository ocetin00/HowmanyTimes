package com.oguzhancetin.howmanytimes

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.gms.ads.AdRequest
import com.oguzhancetin.howmanytimes.model.Player
import org.w3c.dom.Text

@BindingAdapter("setbestPlayer")
fun TextView.setBestPlayer(player: Player?){

    var nickname:String? = ""
    var score:Long? = 0
    player?.let {
        nickname = it.nickname
        score = it.skor

    }
    val string = "$nickname score: $score"

    text = string
}
@BindingAdapter("setCurrentScore")
fun TextView.setCurrentScoreTex(player: Player?){
    var string:String?
    player?.let {
       string ="Your score: ${it.skor.toString()} "
        text = string
    }

}
val adGlobalBuilder = AdRequest.Builder().build()
