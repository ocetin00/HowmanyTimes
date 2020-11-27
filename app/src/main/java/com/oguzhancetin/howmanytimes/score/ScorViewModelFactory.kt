package com.oguzhancetin.howmanytimes.score

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.oguzhancetin.howmanytimes.model.Player
import java.lang.IllegalArgumentException

class ScorViewModelFactory(val application: Application,val player: Player): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ScoreViewModel::class.java)){
            return ScoreViewModel(application,player) as T
        }
        throw IllegalArgumentException("it's not valid")
    }
}