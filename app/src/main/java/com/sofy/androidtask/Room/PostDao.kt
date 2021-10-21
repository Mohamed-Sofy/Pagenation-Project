package com.sofy.androidtask.Room


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofy.androidtask.Model.post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(posts_list:ArrayList<Posts>)


    @Query("SELECT * FROM post_table")
    fun getAllPosts(): List<Posts>

    @Query("DELETE FROM post_table")
    fun deleteAllPosts()
}
