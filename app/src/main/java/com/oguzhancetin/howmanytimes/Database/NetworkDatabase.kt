package com.oguzhancetin.howmanytimes.Database

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NetworkDatabase {



    companion object{
       private var dbFirebase:FirebaseDatabase? = null

        fun getDatabase() : FirebaseDatabase? {
            if(dbFirebase == null){
                dbFirebase = Firebase.database
            }
            return dbFirebase
        }
    }
}