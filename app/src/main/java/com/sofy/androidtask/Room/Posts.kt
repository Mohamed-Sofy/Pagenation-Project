package com.sofy.androidtask.Room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "post_table")
data class Posts (
    @PrimaryKey(autoGenerate = false)  var id:Int,
    @ColumnInfo(name = "userId") var userId:Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "body") var body:String
    )


