package com.sofy.androidtask.Room


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sofy.androidtask.Model.post

@Dao
interface PostDao {

    @Insert
    fun insertAllPosts(list:MutableList<post>)


    @Query("SELECT * FROM post_table")
    fun getAllPosts(): MutableList<post>

 //   @Query("DELETE FROM post_table")
 //   suspend fun deleteAllPosts()
}
