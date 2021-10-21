package com.sofy.androidtask.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database( entities = [Posts::class],version = 1 ,exportSchema = false)
abstract class Post_Database : RoomDatabase(){

    abstract fun postDao():PostDao
}