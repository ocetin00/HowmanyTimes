package com.oguzhancetin.howmanytimes.score

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.oguzhancetin.howmanytimes.model.Player
import com.oguzhancetin.howmanytimes.Database.Repository

class ScoreViewModel(val myApplication: Application,val player: Player) : AndroidViewModel(myApplication) {

    private val _bestPlayer = MutableLiveData<Player>()
    val bestPlayer:LiveData<Player>
        get() = _bestPlayer

    val repository = Repository()
    val db = repository.database?.reference?.child("/")?.child("players")


    private fun getBestPlayer(){
        val query = db?.orderByValue()?.limitToLast(1)


            query?.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children){
                            Log.e("data",data.toString())
                            val nickname = data.key as String
                            val score = data.value as Long
                            val player = Player(nickname,score)
                            Log.e("player","${player?.nickname + player!!.skor}")

                            _bestPlayer.value = player



                    }

                }

                override fun onCancelled(error: DatabaseError) {
                   Toast.makeText(myApplication.applicationContext,"Sorry something went wrong!",Toast.LENGTH_SHORT)
                }

            })

    }

    init {
        saveScore(player)
        getBestPlayer()
    }

    private fun saveScore(player: Player){
        db?.child(player.nickname.toString())?.setValue(player.skor)
    }
}