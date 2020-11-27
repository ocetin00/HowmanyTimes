package com.oguzhancetin.howmanytimes.game

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _timeLeft = MutableLiveData<Int>()
    val  timeLeft: LiveData<Int>
        get() = _timeLeft


    //did time finised?
    private val _checkTimeStatus = MutableLiveData<Boolean>()
    val  checTimeStatus: LiveData<Boolean>
        get() = _checkTimeStatus

    //is timer keep going?
    private val _checkTimerStatus = MutableLiveData<Boolean>()
    val  checTimerStatus: LiveData<Boolean>
        get() = _checkTimerStatus



     fun startCountTime() {
        object :CountDownTimer(6000,1000){
            override fun onTick(p0: Long) {
                Log.e("kalan",p0.toString())
               _timeLeft.value = (p0.toInt()/1000)
                _checkTimerStatus.value = true
            }

            override fun onFinish() {
                _checkTimeStatus.value = true
                _checkTimerStatus.value = false
            }

        }.start()
    }

    //to clean time status
    fun cleanTimeStatus(){
        _checkTimeStatus.value = false
    }
    fun saveData(){

    }

}