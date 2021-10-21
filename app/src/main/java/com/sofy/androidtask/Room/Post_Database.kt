package com.sofy.androidtask.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database( entities = [Posts::class],version = 1)
abstract class Post_Database : RoomDatabase(){

    abstract fun postDao():PostDao


    companion object{
         var INSTANCE: Post_Database? = null

        public fun getInstance(context: Context): Post_Database {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder<Post_Database>(
                    context.applicationContext,Post_Database::class.java,"APPDB"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }


}